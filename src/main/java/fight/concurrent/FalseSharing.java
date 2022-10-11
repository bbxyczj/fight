package fight.concurrent;

import fight.concurrent.bean.VolatileLong;

/**
 * @Author XE-CZJ
 * @Date 2022/8/5 10:36
 */
public final class FalseSharing implements Runnable {

    public final static int NUM_THREADS = 12; // change

    public final static long ITERATIONS = 500L * 1000L * 1000L;

    private final int arrayIndex;


    private static VolatileLong[] longs = new VolatileLong[NUM_THREADS];

    static {

        for (int i = 0; i < longs.length; i++) {

            longs[i] = new VolatileLong();

        }

    }


    public FalseSharing(final int arrayIndex) {

        this.arrayIndex = arrayIndex;

    }


    public static void main(final String[] args) throws Exception {

        final long start = System.currentTimeMillis();

        runTest();

        System.out.println("duration = " + (System.currentTimeMillis() - start)+"ms");

    }


    private static void runTest() throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new FalseSharing(i));
        }
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
    }


    public void run() {
        long i = ITERATIONS + 1;
        while (0 != --i) {
            longs[arrayIndex].value = i;
        }
    }


}