package fight.utils;

import fight.algorithm.SnowflakeIdWorker;
import fight.contrent.MyThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author XE-陈正健
 * @version 1.0
 * @date 2022/3/29 10:46
 */
public class SemaphoreTest {
    private static final Logger log= LoggerFactory.getLogger(SemaphoreTest.class);
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(16);
        SnowflakeIdWorker idWorker = SnowflakeIdWorker.getInstance();
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(5, 10, 5
                        , TimeUnit.SECONDS, new ArrayBlockingQueue<>(10),new ThreadFactoryToolkit("aaa")){
                    @Override
                    protected void beforeExecute(Thread t, Runnable r) {
                       log.info("-------------------"+((Thread) r).getName());


                    }
                };
        try {
            for (int i=0 ;i<100;i++){
                semaphore.acquire();
//                threadPoolExecutor.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
//                        MDC.put("std",String.valueOf(idWorker.nextId()));
//                        log.info("当前队列数量：{}"+semaphore.getQueueLength()+"可用数量：{}" +semaphore.availablePermits());
//                        try {
//                            Thread.sleep(100L);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        log.info("释放信号"+Thread.currentThread().getName());
//                        semaphore.release();
//                    }
//                });
                threadPoolExecutor.execute(new MyThread(() -> {
//                    String s = String.valueOf(idWorker.nextId());
//                    MDC.put("std",s);
                    try {
                        log.info(MDC.get("std")+"-当前队列数量：{}"+semaphore.getQueueLength()+"可用数量：{}" +semaphore.availablePermits());
                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        log.info(MDC.get("std")+"-释放信号"+Thread.currentThread().getName());
                        semaphore.release();
                    } finally {
                        MDC.remove("std");
                    }
                }));
                log.info(MDC.get("std")+"线程数据 活动线程数量{}"+threadPoolExecutor.getActiveCount()+"队列数量"+threadPoolExecutor.getQueue().size());
            }
            Thread.sleep(5000L);
            log.info("最终队列数量：{}"+semaphore.getQueueLength()+"可用数量：{}" +semaphore.availablePermits());
            log.info("最终线程数据 活动线程数量{}"+threadPoolExecutor.getActiveCount()+"队列数量"+threadPoolExecutor.getQueue().size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
