package tests;

import framework.config.TestConfig;
import framework.core.DriverFactory;
import framework.utils.ScreenshotUtils;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    private static boolean environmentLogged = false;

    @BeforeEach
    void setup() {
        // Log environment details to Allure once (not for every test)
        if (!environmentLogged) {
            logEnvironmentToAllure();
            environmentLogged = true;
        }
        DriverFactory.init();
        logger.info("Browser started");
    }

    @AfterEach
    void tearDown(TestInfo info) {
        try {
            // Take screenshot using the utility
            byte[] screenshot = ScreenshotUtils.captureScreenshot(info.getDisplayName());
            if (screenshot != null) {
                // Attach to Allure
                Allure.addAttachment("Screenshot", "image/png", new ByteArrayInputStream(screenshot), "png");
            }
        } catch (Exception e) {
            logger.error("Failed to take or attach screenshot", e);
        }
        DriverFactory.close();
        logger.info("Browser closed");
    }

    /**
     * Logs configuration and command-line arguments to Allure environment section
     * by writing to environment.properties file in target/allure-results
     */
    private void logEnvironmentToAllure() {
        try {
            Properties props = new Properties();

            // Application URLs
            props.setProperty("Base URL", TestConfig.getBaseUrl());
            props.setProperty("Search Housing Page", TestConfig.getSearchHousingPage());
            props.setProperty("Language Params", TestConfig.getLanguageParams());
            props.setProperty("Location", TestConfig.getLocation());

            // Playwright Configuration
            props.setProperty("Browser Headless", String.valueOf(TestConfig.isHeadless()));
            props.setProperty("Browser Type", TestConfig.getBrowserType());
            props.setProperty("Browser Timeout (ms)", String.valueOf(TestConfig.getTimeout()));

            // Command-line overrides (system properties)
            logSystemPropertyIfSet(props, "playwright.browser.headless");
            logSystemPropertyIfSet(props, "playwright.browser.type");
            logSystemPropertyIfSet(props, "playwright.timeout");
            logSystemPropertyIfSet(props, "app.base.url");
            logSystemPropertyIfSet(props, "app.search.housing.page");
            logSystemPropertyIfSet(props, "app.lang");
            logSystemPropertyIfSet(props, "app.location");

            // Java Environment
            props.setProperty("Java Version", System.getProperty("java.version"));
            props.setProperty("OS Name", System.getProperty("os.name"));
            props.setProperty("OS Architecture", System.getProperty("os.arch"));

            // Write to environment.properties file
            Path allureResultsDir = Paths.get("target/allure-results");
            Files.createDirectories(allureResultsDir);

            Path envPropsFile = allureResultsDir.resolve("environment.properties");
            try (FileWriter writer = new FileWriter(envPropsFile.toFile())) {
                props.store(writer, "Test Environment Configuration");
            }

            //logger.info("Environment configuration logged to Allure at " + envPropsFile);
        } catch (Exception e) {
            logger.error("Failed to log environment to Allure", e);
        }
    }

    /**
     * Logs a system property to Properties if it was explicitly set via -D flag
     */
    private void logSystemPropertyIfSet(Properties props, String propertyName) {
        String value = System.getProperty(propertyName);
        if (value != null) {
            props.setProperty("CmdLine: " + propertyName, value);
        }
    }
}
