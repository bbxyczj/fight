package fight.concurrent.bean;

/**
 * @Author XE-CZJ
 * @Date 2022/8/26 10:35
 */
//@sun.misc.Contended
public class VolatileLong {
    //VM- -XX:-RestrictContended
    public volatile long value = 0L;

//    public long p1, p2, p3, p4, p5, p6,p7; // comment out
}
