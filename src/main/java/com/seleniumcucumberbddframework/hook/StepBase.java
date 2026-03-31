package com.seleniumcucumberbddframework.hook;

import com.seleniumcucumberbddframework.pages.DashBoardPage;
import com.seleniumcucumberbddframework.utlis.RandomTestData;
import com.seleniumcucumberbddframework.utlis.WebElementUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by Mohammad Majid on 4/11/2020
 */

public class StepBase extends WebElementUtils{

    private static final String APP_BASE_URL_PROPERTY = "app.base.url";

    protected DashBoardPage dashBoard = new DashBoardPage();
    protected RandomTestData randomData = new RandomTestData();

    protected String loadDataFromPropertiesFile(String string,String fileName) {
        Properties prop = new Properties();
        try (InputStream input = loadPropertiesStream(fileName)) {
            if (input == null) {
                return "";
            }
            prop.load(input);
            return prop.getProperty(string);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    protected String resolveApplicationUrl() {
        String pagePath = loadDataFromPropertiesFile("url", "page.properties").trim();
        if (pagePath.isEmpty()) {
            return "";
        }

        String appBaseUrl = System.getProperty(APP_BASE_URL_PROPERTY, "").trim();
        String relativePagePath = pagePath.startsWith("/") ? pagePath.substring(1) : pagePath;

        if (!appBaseUrl.isEmpty()) {
            String normalizedBaseUrl = appBaseUrl.endsWith("/") ? appBaseUrl : appBaseUrl + "/";
            return URI.create(normalizedBaseUrl).resolve(relativePagePath).toString();
        }

        String localAppBaseUrl = Paths.get(System.getProperty("user.dir"), "src", "app").toUri().toString();
        return URI.create(localAppBaseUrl).resolve(relativePagePath).toString();
    }

    private InputStream loadPropertiesStream(String fileName) throws IOException {
        InputStream classpathStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (classpathStream != null) {
            return classpathStream;
        }

        String file = System.getProperty("user.dir") + "/src/main/resources/" + fileName;
        return new FileInputStream(file);
    }
}
