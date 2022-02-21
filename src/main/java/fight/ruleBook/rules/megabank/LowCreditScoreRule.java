package fight.ruleBook.rules.megabank;

import com.deliveredtechnologies.rulebook.RuleState;
import com.deliveredtechnologies.rulebook.annotation.*;
import fight.ruleBook.pojos.ApplicantBean;

import java.util.List;

/**
 * @author 钱多多-陈正健
 * @version 1.0
 * @date 2022/1/14 11:39
 */
@Rule(order = 2)
public class LowCreditScoreRule {
    @Given
    private List<ApplicantBean> applicants;

    @Result
    private double rate;

    @When
    public boolean when() {
        return applicants.stream()
                .allMatch(applicant -> applicant.getCreditScore() < 600);
    }

    @Then
    public RuleState then() {
        rate *= 4;
        return RuleState.BREAK;
    }
}
