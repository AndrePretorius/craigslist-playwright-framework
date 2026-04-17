package framework.utils;

import com.microsoft.playwright.Page;
import framework.core.DriverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for capturing and managing screenshots in Playwright tests.
 * Provides methods for automatic screenshot capture and manual debugging screenshots.
 *
 * Usage Examples:
 * <pre>
 * // Basic screenshot with custom name
 * ScreenshotUtils.captureScreenshot("my_custom_screenshot");
 *
 * // Debug screenshot with context
 * ScreenshotUtils.captureDebugScreenshot("before_click");
 * ScreenshotUtils.captureDebugScreenshot("after_error");
 *
 * // Quick debug screenshot
 * ScreenshotUtils.captureDebugScreenshot();
 *
 * // In test methods for debugging
 * @Test
 * void myTest() {
 *     // ... test code ...
 *     ScreenshotUtils.captureDebugScreenshot("step_1_complete");
 *     // ... more test code ...
 * }
 * </pre>
 */
public class ScreenshotUtils {
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = "target/screenshots";

    /**
     * Captures a screenshot and saves it to the target/screenshots directory.
     * Returns the screenshot bytes for further processing (e.g., Allure attachment).
     *
     * @param screenshotName The name for the screenshot file (without extension)
     * @return The screenshot bytes, or null if capture failed
     */
    public static byte[] captureScreenshot(String screenshotName) {
        try {
            Page page = DriverFactory.page();
            if (page == null) {
                logger.warn("Cannot capture screenshot: Page is null");
                return null;
            }

            byte[] screenshot = page.screenshot();

            // Save to target/screenshots
            Path screenshotPath = Paths.get(SCREENSHOT_DIR, screenshotName + ".png");
            Files.createDirectories(screenshotPath.getParent()); // Directory creation handled here
            Files.write(screenshotPath, screenshot);

            logger.info("Screenshot captured: {}", screenshotPath);
            return screenshot;
        } catch (IOException e) {
            logger.error("Failed to capture screenshot: {}", screenshotName, e);
            return null;
        } catch (Exception e) {
            logger.error("Unexpected error while capturing screenshot: {}", screenshotName, e);
            return null;
        }
    }

    /**
     * Captures a screenshot with a timestamp-based name for debugging purposes.
     * Useful for manual debugging calls.
     *
     * @param context Additional context for the screenshot name (e.g., "before_click", "after_error")
     * @return The screenshot bytes, or null if capture failed
     */
    public static byte[] captureDebugScreenshot(String context) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String screenshotName = "debug_" + context + "_" + timestamp;
        return captureScreenshot(screenshotName);
    }

    /**
     * Captures a screenshot with a simple debug name.
     * Useful for quick debugging without specifying context.
     *
     * @return The screenshot bytes, or null if capture failed
     */
    public static byte[] captureDebugScreenshot() {
        return captureDebugScreenshot("manual");
    }
}
