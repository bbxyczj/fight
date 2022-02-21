package fight.xePerformance.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 钱多多-陈正健
 * @version 1.0
 * @date 2022/1/14 13:55
 */
@Data
public class TeamMember {

    /**
     * 姓名
     */
    private String name;

    /**
     * 团队绩效
     */
    private BigDecimal teamPerformance;

    /**
     * bug绩效
     */
    private BigDecimal BugPerformance;

    /**
     * 改进绩效
     */
    private BigDecimal betterPerformance;

    private List<BugBean> bugBeans;

    private List<BatterBean> batterBeans;


}
