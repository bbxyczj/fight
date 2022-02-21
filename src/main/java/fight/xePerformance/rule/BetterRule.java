package fight.xePerformance.rule;

import com.deliveredtechnologies.rulebook.annotation.*;
import fight.xePerformance.config.XePerformanceConfig;
import fight.xePerformance.pojo.BatterBean;
import fight.xePerformance.pojo.TeamMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * @author 钱多多-陈正健
 * @version 1.0
 * @date 2022/1/14 11:39
 */
@Rule(order = 2)
public class BetterRule {
    private static final Logger log= LoggerFactory.getLogger(BetterRule.class);
    @Given("teamMember")
    private TeamMember teamMember;

    @Result
    private TeamMember result;

    @When
    public boolean when() {
      return null != teamMember.getBatterBeans() && !teamMember.getBatterBeans().isEmpty();
    }

    @Then
    public void then() {
        //算改进点的绩效
        BigDecimal batterPerformance=BigDecimal.ZERO;
        for (BatterBean bean:teamMember.getBatterBeans()){
            //改进点*改进价格*（团队人数*改进类型对应系数-1）
            batterPerformance=batterPerformance.add(new BigDecimal(bean.getBetterNum()).multiply(XePerformanceConfig.betterFee)
                    .multiply(new BigDecimal(XePerformanceConfig.peopleNum)
                            .multiply(XePerformanceConfig.betterFactor.get(bean.getBetterType())).subtract(BigDecimal.ONE)));
        }
        log.info("改进绩效为:{}",batterPerformance);
        teamMember.setBetterPerformance(batterPerformance);
        teamMember.setTeamPerformance(teamMember.getTeamPerformance().add(batterPerformance));
        result=teamMember;
    }
}
