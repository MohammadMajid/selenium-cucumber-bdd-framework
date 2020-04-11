package com.ableto.pages;

import com.ableto.utlis.WebElementUtils;
import org.junit.Assert;

/**
 * Created by Mohammad Majid on 4/11/2020
 */

public class PageBase extends WebElementUtils{

    public PageBase(){
        super();
    }

    public void verifyPageTitle(String expectedTitle){
        String title = driver.getTitle();
        Assert.assertEquals(expectedTitle,title);
    }
}
