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
@Rule(order = 3)
public class QuarterPointReductionRule {
    @Given
    List<ApplicantBean> applicants;

    @Result
    private double rate;

    @When
    public boolean when() {
        return
                applicants.stream().anyMatch(applicant -> applicant.getCreditScore() >= 700) &&
                        applicants.stream().map(applicant -> applicant.getCashOnHand()).reduce(0.0, Double::sum) >= 50000;
    }

    @Then
    public void then() {
        rate = rate - (rate * 0.25);
    }
}
