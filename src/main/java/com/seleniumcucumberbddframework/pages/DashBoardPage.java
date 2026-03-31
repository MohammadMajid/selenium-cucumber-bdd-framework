package com.seleniumcucumberbddframework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import java.util.List;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Mohammad Majid on 4/11/2020
 */

public class DashBoardPage extends PageBase {

    public DashBoardPage() {
        super();
        PageFactory.initElements(driver(), this);
    }

    @FindBy(how = How.XPATH, using = "//*[@for='firstname']//following::input[1]")
    private WebElement firstName;
    @FindBy(how = How.XPATH, using = "//*[@for='firstname']//following::input[2]")
    private WebElement lastName;
    @FindBy(how = How.XPATH, using = "//*[@for='firstname']//following::input[3]")
    private WebElement program;
    @FindBy(how = How.CLASS_NAME, using = "//a[@class='login']")
    private WebElement loginLink;
    @FindBy(how = How.XPATH, using = "//*[text()='Submit']")
    private WebElement submitBtn;
    @FindBy(how = How.ID, using = "btnAddEmployee")
    private WebElement addNewEmployee;

    public void addEmployee() {
        ;
        click(addNewEmployee);
    }

    public void AddEmployeeName(String name, String name1,int num) {
        typeText(firstName, name);
        typeText(lastName, name1);
        typeText(program, String.valueOf(num));
        click(submitBtn);
    }

    public void selectActionEdit() {
        List<WebElement> lists = driver().findElements(By.xpath("//*[@id='btnEdit']"));
        lists.get(0).click();
    }

    public void deleteEmp() {
        List<WebElement> lists = driver().findElements(By.xpath("//*[@id='btnDelete']"));
        for (int i=0;i<lists.size();i++){
            lists.get(i).click();
            delayFor(3000);
        }
    }

    public void verifyAllEmpRemoved(){
        List<WebElement> lists = driver().findElements(By.xpath("//*[@class='table table-striped table-hover']"));
        assertThat(1,equalTo(lists.size()));
    }

    public void verifyEmpAdded(){
        List<WebElement> lists = driver().findElements(By.xpath("//*[@class='table table-striped table-hover']//td"));
        String actualName = lists.get(2).getText();
        assertThat(actualName.isEmpty(), equalTo(false));
    }
}
