package fight.ruleBook.test;

import com.deliveredtechnologies.rulebook.Fact;
import com.deliveredtechnologies.rulebook.FactMap;
import com.deliveredtechnologies.rulebook.NameValueReferableMap;
import com.deliveredtechnologies.rulebook.model.runner.RuleBookRunner;
import fight.ruleBook.pojos.ApplicantBean;

/**
 * @author 钱多多-陈正健
 * @version 1.0
 * @date 2022/1/14 11:24
 */
public class RuleTest {

    public static void main(String[] args) {
//        RuleBook ruleBook = RuleBookBuilder.create()
//                .addRule(rule -> rule.withFactType(String.class)
//                        .when(f -> f.containsKey("hello") && f.containsKey("world"))
//                        .using("hello").then(System.out::print)
//                        .using("world").then(System.out::println))
//                .build();
//
//        NameValueReferableMap factMap = new FactMap();
//        factMap.setValue("hello", "Hello ");
//        factMap.setValue("world", " World");
//        ruleBook.run(factMap);


//
//        RuleBook homeLoanRateRuleBook = RuleBookBuilder.create(HomeLoanRateRuleBook.class).withResultType(Double.class)
//                .withDefaultResult(4.5)
//                .build();
//
//        NameValueReferableMap facts = new FactMap();
//        facts.setValue("Credit Score", 650);
//        facts.setValue("Cash on Hand", 20000);
//        facts.setValue("First Time Homebuyer", true);
//
//        homeLoanRateRuleBook.run(facts);
//
//        homeLoanRateRuleBook.getResult().ifPresent(result -> System.out.println("Applicant qualified for the following rate: " + result));


        RuleBookRunner ruleBook = new RuleBookRunner("fight.ruleBook.rules.megabank");
        NameValueReferableMap<ApplicantBean> facts = new FactMap<>();
        ApplicantBean applicant1 = new ApplicantBean(650, 20000, true);
        ApplicantBean applicant2 = new ApplicantBean(620, 30000, true);
        facts.put(new Fact<>(applicant1));
        facts.put(new Fact<>(applicant2));

        ruleBook.setDefaultResult(4.5);
        ruleBook.run(facts);
        ruleBook.getResult().ifPresent(result -> System.out.println("Applicant qualified for the following rate: " + result));


    }
}
