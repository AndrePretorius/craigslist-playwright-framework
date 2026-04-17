package framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestConfig {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = TestConfig.class.getClassLoader().getResourceAsStream("application-test.properties")) {
            if (input == null) {
                throw new IOException("Unable to find application-test.properties in resources");
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load application-test.properties", ex);
        }
    }

    // Playwright configuration
    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("playwright.browser.headless", "false"));
    }

    public static String getBrowserType() {
        return getProperty("playwright.browser.type", "chromium");
    }

    public static int getTimeout() {
        return Integer.parseInt(getProperty("playwright.timeout", "30000"));
    }

    // Application URLs
    public static String getBaseDomain() {
        return getProperty("app.base.domain", "https://craigslist.org");
    }

    public static String getLocation() {
        return getProperty("app.location", "madrid");
    }

    public static String getBaseUrl() {
        // Construct URL from domain and location: https://madrid.craigslist.org
        return getBaseDomain().replace("craigslist.org", getLocation() + ".craigslist.org");
    }

    public static String getSearchHousingPage() {
        return getProperty("app.search.housing.page");
    }

    public static String getLanguageParams() {
        return getProperty("app.lang", "");
    }

    // Utility method to get property with default
    // Checks system properties first (via -D flag), then properties file, then default
    public static String getProperty(String key) {
        String value = getPropertyWithDefault(key, null);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in application-test.properties");
        }
        return value;
    }

    public static String getProperty(String key, String defaultValue) {
        return getPropertyWithDefault(key, defaultValue);
    }

    /**
     * Gets a property checking in this order:
     * 1. System property (set via -D flag on command line)
     * 2. Property file (application-test.properties)
     * 3. Default value
     */
    private static String getPropertyWithDefault(String key, String defaultValue) {
        // Check system property first (allows -D overrides)
        String systemValue = System.getProperty(key);
        if (systemValue != null) {
            return systemValue;
        }
        // Fall back to properties file
        String fileValue = properties.getProperty(key);
        if (fileValue != null) {
            return fileValue;
        }
        // Use default
        return defaultValue;
    }
}

