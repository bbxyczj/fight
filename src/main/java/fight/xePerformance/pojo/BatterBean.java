package fight.xePerformance.pojo;

import lombok.Data;

/**
 * @author 钱多多-陈正健
 * @version 1.0
 * @date 2022/1/14 15:01
 */
@Data
public class BatterBean {
    /**
     * 改进点
     */
    private Double betterNum;
    /**
     * 改进类型
     */
    private Integer betterType;

    public BatterBean(Double betterNum, Integer betterType) {
        this.betterNum = betterNum;
        this.betterType = betterType;
    }
}
