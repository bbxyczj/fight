package fight.utils;

import java.util.concurrent.*;

/**
 * @author 钱多多-陈正健
 * @version 1.0
 * @date 2021/7/20 15:47
 */
public class ThreadPoolUtils {
    public static ThreadPoolExecutor myThreadPool = new ThreadPoolExecutor(2000,
            3000,10, TimeUnit.SECONDS, new LinkedTransferQueue<>(),
            new ThreadFactoryToolkit("myThreadPool"),new ThreadPoolExecutor.DiscardPolicy());


    public static void main(String[] args) {
        for(int i=0;i<2000;i++){

            int finalI = i;
            myThreadPool.execute(() -> {
                System.out.println("第"+ finalI +"次执行");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("线程池参数"+myThreadPool.toString());
        }
        System.out.println("执行结束");

    }
}
