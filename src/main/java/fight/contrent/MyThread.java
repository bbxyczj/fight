package fight.contrent;

import fight.algorithm.SnowflakeIdWorker;
import org.slf4j.MDC;

/**
 * @author XE-陈正健
 * @version 1.0
 * @date 2022/3/29 14:07
 */
public class MyThread extends Thread{
    public MyThread(Runnable target) {
        super(target);
    }

    @Override
    public void run() {
//        super.run();

        SnowflakeIdWorker idWorker = SnowflakeIdWorker.getInstance();
        String s = String.valueOf(idWorker.nextId());
        MDC.put("std",s);
        System.out.println(Thread.currentThread().getName()+"start-------------"+s);
        try {
            super.run();
        } finally {
            System.out.println(Thread.currentThread().getName()+"end-------------"+s);
            MDC.remove("std");
        }
    }
}
