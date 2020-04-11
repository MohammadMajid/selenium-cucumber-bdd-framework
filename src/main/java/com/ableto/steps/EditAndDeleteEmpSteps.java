package com.ableto.steps;

import com.ableto.hook.StepBase;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Created by Mohammad Majid on 4/11/2020
 */
public class EditAndDeleteEmpSteps extends StepBase {

    @Given("^An Admin$")
    public void an_Admin() throws Throwable {
        driver.manage().deleteAllCookies();
    }

    @When("^I can browse to the site$")
    public void i_can_browse_to_the_site() throws Throwable {
        driver.navigate().to("file:///" + System.getProperty("user.dir") + "/src/app/home.html?username=admin123");
    }

    @Then("^Ableto Admin Dashboard page should display$")
    public void ableto_Admin_Dashboard_page_should_display() throws Throwable {
        dashBoard.verifyPageTitle(loadDataFromPropertiesFile("dashTitle", "page.properties"));
    }

    @When("^I can select the Action Edit$")
    public void i_can_select_the_Action_Edit() throws Throwable {
        dashBoard.selectActionEdit();
    }

    @Then("^I can edit employee details$")
    public void i_can_edit_employee_details() throws Throwable {
        dashBoard.AddEmployeeName(randomData.FirstNameWithA(),randomData.randomName(),1);
    }

    @And("^the data should change in the table$")
    public void the_data_should_change_in_the_table() throws Throwable {
        dashBoard.verifyEmpAdded();
    }

    @When("^I click the Action X$")
    public void i_click_the_Action_X() throws Throwable {
        dashBoard.deleteEmp();
    }

    @Then("^the employee should be deleted$")
    public void the_employee_should_be_deleted() throws Throwable {
         dashBoard.verifyAllEmpRemoved();
    }

}
