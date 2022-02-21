package fight.xePerformance.config;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 钱多多-陈正健
 * @version 1.0
 * @date 2022/1/14 13:49
 */
@Data
public class XePerformanceConfig {

    /**
     * 团队人数
     */
    public static Integer peopleNum;
    /**
     * bug价格
     */
    public static BigDecimal bugFee;
    /**
     * 改进点价格
     */
    public static BigDecimal betterFee;

    /**
     * bug系数
     */
    public static Map<Integer, BigDecimal> bugFactor = new HashMap<>();

    /**
     * 改进系数
     */
    public static Map<Integer, BigDecimal> betterFactor = new HashMap<>();


    public static void init() {
        peopleNum = 6;
        bugFee = new BigDecimal("200");
        betterFee = new BigDecimal("300");
        //0 无 1测试 2开发 3兼有
        bugFactor.put(0, new BigDecimal("0.2"));
        bugFactor.put(1, new BigDecimal("0.48"));
        bugFactor.put(2, new BigDecimal("0.32"));
        bugFactor.put(3, new BigDecimal("0.8"));

        //0 无 1提出人 2解决人 3兼有
        betterFactor.put(0, new BigDecimal("0.2"));
        betterFactor.put(1, new BigDecimal("0.4"));
        betterFactor.put(2, new BigDecimal("0.4"));
        betterFactor.put(3, new BigDecimal("0.8"));
    }
}
