package fight.utils;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 实现对单独key加锁工具
 * @author XE-陈正健
 * @version 1.0
 * @date 2022/2/21 15:48
 */
public class HashLockUtil<T> {
    /**
     * 默认非公平锁
     */
    private boolean isFair = false;
    /**
     *  分段锁
     */
    private  final SegmentLock<T> segmentLock = new SegmentLock<>();

    private final static ThreadLocal<Boolean> threadLock=new ThreadLocal<>();

    private  final ConcurrentHashMap<T, LockInfo> lockMap = new ConcurrentHashMap<>();

        
    public HashLockUtil() {
    }

    public HashLockUtil(boolean fair) {
        isFair = fair;
    }

    public  void lock(T key) {
        LockInfo lockInfo;
        //分段锁，为了给获取本身锁对象加锁，避免并发获取锁对象
        segmentLock.lock(key);
        try {
            lockInfo = lockMap.get(key);
            if (lockInfo == null) {
                lockInfo = new LockInfo(isFair);
                lockMap.put(key, lockInfo);
            } else {
                lockInfo.count.incrementAndGet();
            }
        } finally {
            segmentLock.unlock(key);
        }
        lockInfo.lock();
    }
    public void unlock(T key) {
        LockInfo lockInfo = lockMap.get(key);
        if(lockInfo==null){
            //已经解锁
            return;
        }
        if (lockInfo.count.get() == 1) {
            segmentLock.lock(key);
            try {
                //双保险
                if (lockInfo.count.get() == 1) {
                    lockMap.remove(key);
                }
            } finally {
                segmentLock.unlock(key);
            }
        }
        lockInfo.count.decrementAndGet();
        lockInfo.unlock();
    }

    /**
     * 锁对象实现
     */
    public static class LockInfo {
        public ReentrantLock lock;
        public AtomicInteger count = new AtomicInteger(1);

        public LockInfo(boolean fair) {
            this.lock = new ReentrantLock(fair);
        }

        public void lock() {
            try {
                //超时直接返回，默认等待1500毫秒，拿不到锁的话就正常走流程
                boolean b = this.lock.tryLock(1500, TimeUnit.MILLISECONDS);
                threadLock.set(b);
            } catch (InterruptedException e) {
                //线程被中断，一般不会发生,打印下信息吧
                e.printStackTrace();
            }
        }

        public void unlock() {
            try {
                if(threadLock.get()){
                    this.lock.unlock();
                }
            }finally {
                threadLock.remove();
            }
        }
    }

}

/**
 * 分段锁逻辑
 * @param <T>
 */
class SegmentLock<T> {
    /**
     * 默认分段数量，空间和效率综合考虑
     */
    private Integer segments = 1024;
    private final HashMap<Integer, ReentrantLock> lockMap = new HashMap<>();

    public SegmentLock() {
        init(null, false);
    }

    public SegmentLock(Integer counts, boolean fair) {
        init(counts, fair);
    }

    private void init(Integer counts, boolean fair) {
        if (counts != null) {
            segments = counts;
        }
        for (int i = 0; i < segments; i++) {
            lockMap.put(i, new ReentrantLock(fair));
        }
    }

    public void lock(T key) {
        ReentrantLock lock = lockMap.get(Math.abs(key.hashCode() % segments));
        lock.lock();
    }

    public void unlock(T key) {
        ReentrantLock lock = lockMap.get(Math.abs(key.hashCode() % segments));
        lock.unlock();
    }
}
