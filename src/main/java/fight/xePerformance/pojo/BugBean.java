package fight.xePerformance.pojo;

import lombok.Data;

/**
 * @author 钱多多-陈正健
 * @version 1.0
 * @date 2022/1/14 15:01
 */
@Data
public class BugBean {
    /**
     * bug数
     */
    private Integer bugNum;

    /**
     * bug类型
     */
    private Integer bugType;


    public BugBean(Integer bugNum, Integer bugType) {
        this.bugNum = bugNum;
        this.bugType = bugType;
    }
}
