package fight.algorithm;

import fight.utils.ThreadPoolUtils;

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

        Object syncBean = new Object();

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
        public boolean tryAcquire(boolean isWait) {
            synchronized (syncBean){
                long l = System.currentTimeMillis();
                if(l>nextTime){
                    double v = (l - nextTime) / takeSpace;
                    currentCount= (currentCount+v)>=maxCount?maxCount:currentCount+v;
//                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)+Thread.currentThread().getName()
//                    +"now:"+l +"original:"+nextTime+"v:"+v+"currentCount:"+currentCount);
                }
                nextTime=l;
                if(!isWait){
                    if(currentCount<1){
                        return false;
                    }
                    return currentCount-->0;
                }
                if(currentCount>1){
                    return currentCount-->0;
                }
                while (true){
                    try {
//                        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)+Thread.currentThread().getName()
//                                +"睡了"+takeSpace+"currentCount:"+currentCount);
                        MILLISECONDS.sleep(new Double(takeSpace).longValue());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return tryAcquire(true);
                }
            }
        }
    }

    public static void main(String[] args) {

        //每秒允许10个请求通过
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
