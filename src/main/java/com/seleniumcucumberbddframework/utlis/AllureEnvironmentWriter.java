package com.seleniumcucumberbddframework.utlis;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class AllureEnvironmentWriter {

    private static final String RESULTS_DIR_PROPERTY = "allure.results.directory";
    private static final String RUNNER_NAME_PROPERTY = "test.runner.name";
    private static final String ENVIRONMENT_FILE = "environment.properties";

    private AllureEnvironmentWriter() {
    }

    public static synchronized void write(WebDriver driver) {
        String resultsDirectory = System.getProperty(RESULTS_DIR_PROPERTY);
        if (resultsDirectory == null || resultsDirectory.trim().isEmpty()) {
            return;
        }

        Path resultsPath = Paths.get(resultsDirectory);
        Path environmentFile = resultsPath.resolve(ENVIRONMENT_FILE);

        Properties properties = new Properties();
        properties.setProperty("Runner", System.getProperty(RUNNER_NAME_PROPERTY, "unknown"));
        properties.setProperty("OS", System.getProperty("os.name", "unknown"));
        properties.setProperty("OS.Version", System.getProperty("os.version", "unknown"));
        properties.setProperty("OS.Arch", System.getProperty("os.arch", "unknown"));
        properties.setProperty("Java.Version", System.getProperty("java.version", "unknown"));
        properties.setProperty("Java.Vendor", System.getProperty("java.vendor", "unknown"));
        properties.setProperty("Browser.Driver", driver.getClass().getSimpleName());
        properties.setProperty("Selenium.Version", resolveVersion(WebDriver.class));

        if (driver instanceof HasCapabilities) {
            Capabilities capabilities = ((HasCapabilities) driver).getCapabilities();
            String browserName = valueOrUnknown(capabilities.getBrowserName());
            String browserVersion = valueOrUnknown(capabilities.getBrowserVersion());
            properties.setProperty("Browser", browserName);
            properties.setProperty("Browser.Version", browserVersion);

            Object platformName = capabilities.getCapability("platformName");
            if (platformName != null) {
                properties.setProperty("Browser.Platform", String.valueOf(platformName));
            }
        }

        try {
            Files.createDirectories(resultsPath);
            try (OutputStream outputStream = Files.newOutputStream(environmentFile)) {
                properties.store(outputStream, "Allure environment");
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to write Allure environment metadata", exception);
        }
    }

    private static String resolveVersion(Class<?> type) {
        Package classPackage = type.getPackage();
        if (classPackage == null || classPackage.getImplementationVersion() == null) {
            return "unknown";
        }
        return classPackage.getImplementationVersion();
    }

    private static String valueOrUnknown(String value) {
        return value == null || value.trim().isEmpty() ? "unknown" : value;
    }
}