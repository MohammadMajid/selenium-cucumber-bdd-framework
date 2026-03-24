package com.seleniumcucumberbddframework.hook;

import com.seleniumcucumberbddframework.pages.DashBoardPage;
import com.seleniumcucumberbddframework.utlis.RandomTestData;
import com.seleniumcucumberbddframework.utlis.WebElementUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Mohammad Majid on 4/11/2020
 */

public class StepBase extends WebElementUtils{

    protected DashBoardPage dashBoard = new DashBoardPage();
    protected RandomTestData randomData = new RandomTestData();

    protected String loadDataFromPropertiesFile(String string,String fileName) {
        final String file = System.getProperty("user.dir") + "/src/main/resources/" + fileName;
        Properties prop = new Properties();
        InputStream input;
        try {
            input = new FileInputStream(file);
            // load a properties file
            prop.load(input);
            return prop.getProperty(string);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "";
    }
}
