package runner;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "json:target/cucumber.json","html:target/cucumber-pretty" },
				 features = "Features/TestFeature.feature",
				 glue={"stepDef"})
public class TestRunner {

}