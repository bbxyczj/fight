package fight.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @Author XE-CZJ
 * @Date 2022/10/27 10:33
 */
public class TokenBucketUtil {

    private static final Map<String,BucketBean> CACHE=new ConcurrentHashMap<>(8);


    public static BucketBean getRoteLimit(String key,double limit){
        if(limit<0){
            throw new RuntimeException("limit must be more 0");
        }
        CACHE.putIfAbsent(key, new BucketBean(limit));
        return CACHE.get(key);
    }



    public interface RoteLimit{

        boolean tryAcquire();
        boolean tryAcquire(boolean isWait);
    }


    private static final class BucketBean implements RoteLimit{
        private double maxCount;
        private double currentCount;
        private double takeSpace;
        private long nextTime;

        public BucketBean(double maxCount) {
            this.maxCount = maxCount;
            this.takeSpace = 1000/(maxCount);
            this.nextTime = 0;
        }


        @Override
        public  boolean tryAcquire() {
            return tryAcquire(false);
        }

        @Override
        public synchronized boolean tryAcquire(boolean isWait) {
            long l = System.currentTimeMillis();
            if(l>nextTime){
                double v = (l - nextTime) / takeSpace;
                currentCount= (currentCount+v)>=maxCount?maxCount:currentCount+v;
            }
            nextTime=l;
            if(!isWait){
                if(currentCount<1){
                    return false;
                }
                return currentCount-->=0;
            }
            while (currentCount<1){
                try {
                    MILLISECONDS.sleep(new Double(takeSpace).longValue());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tryAcquire(true);
            }
            return currentCount-->=0;
        }
    }

    public static void main(String[] args) {

        BucketBean me = TokenBucketUtil.getRoteLimit("me", 10);
            for(int i=0;i<100;i++){
                int finalI = i;
                ThreadPoolUtils.myThreadPool.execute(() -> {
                    long l = System.currentTimeMillis();
                    boolean b = me.tryAcquire(true);
//                    try {
//                        SECONDS.sleep(1);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)+"第"+ finalI +"次执行结果"+ b+" 耗时:"+(System.currentTimeMillis()-l));
                });
            }
            System.out.println("执行结束");
    }
}
