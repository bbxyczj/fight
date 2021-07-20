package fight.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * thread工厂工具类
 * 
 * @author XE组--宋浩
 *
 */
public class ThreadFactoryToolkit implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private String namePrefix;


    public ThreadFactoryToolkit(String name) {
        this.namePrefix = name + "-" + poolNumber.getAndIncrement() + "-thread-";
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, namePrefix + threadNumber.getAndIncrement());
    }
}
