package framework.core;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import framework.config.TestConfig;

public class DriverFactory {
    private static final ThreadLocal<Playwright> playwrightThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browserThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();

    public static void init() {
        playwrightThreadLocal.set(Playwright.create());
        Browser browser = playwrightThreadLocal.get().chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(TestConfig.isHeadless())
        );
        browserThreadLocal.set(browser);
        pageThreadLocal.set(browser.newPage());
    }

    public static Page page() {
        return pageThreadLocal.get();
    }

    public static void close() {
        if (pageThreadLocal.get() != null) pageThreadLocal.get().close();
        if (browserThreadLocal.get() != null) browserThreadLocal.get().close();
        if (playwrightThreadLocal.get() != null) playwrightThreadLocal.get().close();
    }
}