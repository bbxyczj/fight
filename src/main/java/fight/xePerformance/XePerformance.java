package fight.xePerformance;

import com.deliveredtechnologies.rulebook.Fact;
import com.deliveredtechnologies.rulebook.FactMap;
import com.deliveredtechnologies.rulebook.NameValueReferableMap;
import com.deliveredtechnologies.rulebook.model.runner.RuleBookRunner;
import fight.ruleBook.pojos.ApplicantBean;
import fight.xePerformance.config.XePerformanceConfig;
import fight.xePerformance.pojo.BatterBean;
import fight.xePerformance.pojo.BugBean;
import fight.xePerformance.pojo.TeamMember;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 钱多多-陈正健
 * @version 1.0
 * @date 2022/1/14 13:46
 */
public class XePerformance {


    public static void main(String[] args) {
        XePerformanceConfig.init();
        RuleBookRunner ruleBook = new RuleBookRunner("fight.xePerformance.rule");
        NameValueReferableMap<TeamMember> facts = new FactMap<>();
        TeamMember teamMember=new TeamMember();
        teamMember.setName("陈正健");
        teamMember.setTeamPerformance(new BigDecimal("5500"));
        List<BugBean> bugList=new ArrayList<>();
        bugList.add(new BugBean(1,2));
        teamMember.setBugBeans(bugList);
        List<BatterBean> batterList=new ArrayList<>();
        batterList.add(new BatterBean(0.5d,2));
        teamMember.setBatterBeans(batterList);

        facts.setValue("teamMember",teamMember);
        ruleBook.setDefaultResult(teamMember);
        ruleBook.run(facts);
        ruleBook.getResult().ifPresent(result ->{
            TeamMember teamMemberResult = (TeamMember) result.getValue();
            System.out.println(teamMemberResult.getName()+"最终绩效为"+teamMemberResult.getTeamPerformance());
        });

    }
}
