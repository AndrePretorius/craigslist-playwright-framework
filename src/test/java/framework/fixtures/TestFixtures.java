package framework.fixtures;

/**
 * Test fixtures providing reusable test data.
 * Similar to Playwright TypeScript fixtures pattern.
 * <p>
 * Usage in tests:
 * String searchKeyword = TestFixtures.getSearchKeyword();
 */
public class TestFixtures {

    // Location-specific fixtures
    public static class Madrid {
        public static String getSearchKeyword() {
            return "traditional";
        }
        // Add more Madrid-specific fixtures here
    }

    public static class London {
        public static String getSearchKeyword() {
            return "luxury";
        }
        // Add more London-specific fixtures here
    }

    // Add more locations as needed
}
