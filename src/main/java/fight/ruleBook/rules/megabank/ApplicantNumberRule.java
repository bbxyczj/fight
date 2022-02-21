package fight.ruleBook.rules.megabank;

import com.deliveredtechnologies.rulebook.RuleState;
import com.deliveredtechnologies.rulebook.annotation.Given;
import com.deliveredtechnologies.rulebook.annotation.Rule;
import com.deliveredtechnologies.rulebook.annotation.Then;
import com.deliveredtechnologies.rulebook.annotation.When;
import fight.ruleBook.pojos.ApplicantBean;

import java.util.List;

/**
 * @author 钱多多-陈正健
 * @version 1.0
 * @date 2022/1/14 11:39
 */
@Rule(order = 1) //order specifies the order the rule should execute in; if not specified, any order may be used
public class ApplicantNumberRule {
    @Given
    private List<ApplicantBean> applicants; //Annotated Lists get injected with all Facts of the declared generic type

    @When
    public boolean when() {
        return applicants.size() > 3;
    }

    @Then
    public RuleState then() {
        return RuleState.BREAK;
    }
}
