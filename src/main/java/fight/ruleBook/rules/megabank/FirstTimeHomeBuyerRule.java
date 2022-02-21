package fight.ruleBook.rules.megabank;

import com.deliveredtechnologies.rulebook.annotation.*;
import fight.ruleBook.pojos.ApplicantBean;

import java.util.List;

/**
 * @author 钱多多-陈正健
 * @version 1.0
 * @date 2022/1/14 11:39
 */
@Rule(order = 4)
public class FirstTimeHomeBuyerRule {
    @Given
    List<ApplicantBean> applicants;

    @Result
    private double rate;

    @When
    public boolean when() {
        return
                applicants.stream().anyMatch(applicant -> applicant.isFirstTimeHomeBuyer());
    }

    @Then
    public void then() {
        rate = rate - (rate * 0.20);
    }
}
