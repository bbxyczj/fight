package fight.xePerformance.rule;

import com.deliveredtechnologies.rulebook.RuleState;
import com.deliveredtechnologies.rulebook.annotation.*;
import fight.xePerformance.config.XePerformanceConfig;
import fight.xePerformance.pojo.BugBean;
import fight.xePerformance.pojo.TeamMember;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * @author 钱多多-陈正健
 * @version 1.0
 * @date 2022/1/14 11:39
 */
@Rule(order = 1)
public class BugRule {
    private static final Logger log= LoggerFactory.getLogger(BugRule.class);
    @Given("teamMember")
    private TeamMember teamMember;

    @Result
    private TeamMember result;

    @When
    public boolean when() {
      return null != teamMember.getBugBeans() && !teamMember.getBugBeans().isEmpty();
    }

    @Then
    public void then() {
        //算BUG的绩效
        BigDecimal bugPerformance=BigDecimal.ZERO;
        for (BugBean bean:teamMember.getBugBeans()){
            //bug数*bug价格*（团队人数*bug类型对应系数-1）
            bugPerformance=bugPerformance.add(new BigDecimal(bean.getBugNum()).multiply(XePerformanceConfig.bugFee)
                    .multiply(new BigDecimal(XePerformanceConfig.peopleNum)
                            .multiply(XePerformanceConfig.bugFactor.get(bean.getBugType())).subtract(BigDecimal.ONE)));
        }
        //bug为负绩效
        bugPerformance=bugPerformance.negate();
        log.info("bug绩效为:{}",bugPerformance);
        teamMember.setBugPerformance(bugPerformance);
        teamMember.setTeamPerformance(teamMember.getTeamPerformance().add(bugPerformance));
        result=teamMember;
    }
}
