package fight.algorithm;

/**
 * @ Author     ：czj.
 * @ Date       ：Created in 9:58 AM 2021/3/16
 * @ Modified By：
 */
public class PrimeSort {


    // 埃氏筛法
    public static void printPrime2(int num) {
        boolean[] isPrimes = new boolean[num + 1];
        for (int i = 2; i < isPrimes.length; i++) {
            isPrimes[i] = true;
        }
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (isPrimes[i] == true) {
                for (int j = 2; i * j <= num; j++) {
                    isPrimes[i * j] = false;
                }
            }
        }

        System.out.print("-质数有: ");
        for (int i = 2; i < isPrimes.length; i++) {
            if (isPrimes[i]) {
                System.out.print(i + " ");
            }
        }
        System.out.print("-非质数有: ");
        for (int i = 2; i < isPrimes.length; i++) {
            if (!isPrimes[i]) {
                System.out.print(i + " ");
            }
        }
    }

    // sqrt 法
    public static void printPrime1(int num) {
        boolean[] isPrimes = new boolean[num + 1];
        for (int i = 2; i < isPrimes.length; i++) {
            isPrimes[i] = true;
        }

        for (int i = 3; i <= num; i++) {
            for (int j = 2; j <= Math.sqrt(i); j++) {
                if (i % j == 0) {
                    isPrimes[i] = false;
                    break;
                }
            }
        }

        System.out.print("质数有: ");
        for (int i = 2; i < isPrimes.length; i++) {
            if (isPrimes[i]) {
                System.out.print(i + " ");
            }
        }
        System.out.println("");
        System.out.print("非质数有: ");
        for (int i = 2; i < isPrimes.length; i++) {
            if (!isPrimes[i]) {
                System.out.print(i + " ");
            }
        }
    }

    public static void main(String[] args) {
        long timePoint1 = System.currentTimeMillis();
        printPrime1(1000);
        long timePoint2 = System.currentTimeMillis();
        System.out.println();
        printPrime2(1000);
        long timePoint3 = System.currentTimeMillis();

        System.out.println();
        System.out.println("sqrt法耗时: " + String.valueOf(timePoint2 - timePoint1)); // 390 ms
        System.out.println("埃氏筛法耗时: " + String.valueOf(timePoint3 - timePoint2)); // 297 ms
    }
}
