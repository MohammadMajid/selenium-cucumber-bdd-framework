package com.ableto.qa.testng;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.api.testng.TestNGCucumberRunner;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created by Mohammad Majid on 4/11/2020
 */

@CucumberOptions(
        tags = { "@smoke" },
        //monochrome = true,
        features = "src/test/resources/features/EditAndDeleteEployee.feature",
        glue = {"com.ableto.steps"},
        //dryRun = true,
        plugin={
                "pretty:target/cucumber-report/cucumber-pretty.txt",
                "html:target/cucumber-report",
                "json:target/cucumber-report/json/cucumber-report.json",
        }
)
public class BDDRunnerTestNG {

    private TestNGCucumberRunner cucumberRunner;

    @BeforeClass(alwaysRun = true)
    public void setUpClass(){
        cucumberRunner = new TestNGCucumberRunner(this.getClass());
    }

    @Test(groups = "cucumber" ,dataProvider = "features", description = "Runs cucumber features")
    public void fature(CucumberFeatureWrapper cucumberFeature){
        cucumberRunner.runCucumber(cucumberFeature.getCucumberFeature());
    }

    @DataProvider
    public Object[][] features(){
        return cucumberRunner.provideFeatures();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass(){
        cucumberRunner.finish();
    }
}
