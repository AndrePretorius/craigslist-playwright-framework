# Craigslist Playwright Framework

## Quick Links
- **[TEST_CASES.md](TEST_CASES.md)** - Detailed documentation of all 4 test cases
- **[application-test.properties](src/test/resources/application-test.properties)** - Test configuration

## Running Tests

### Basic Test Execution
```bash
mvn clean test                    # Run all tests
mvn allure:serve            # Generate and serve Allure report locally

# Run tests and immediately serve Allure report (blocks until you stop it)
mvn clean test allure:serve   # Run tests and immediately serve Allure report (blocks until you stop it)

# Generate report without serving
mvn allure:report           # Generate Allure HTML report
mvn clean test allure:report # Full test + reporting cycle
```

## Command-Line Property Overrides

You can override any property from `application-test.properties` using Maven's `-D` flag:

### Override Headless Mode
```bash
# Run tests in headless mode (CI/CD environments)
mvn test -Dplaywright.browser.headless=true

# Run tests with browser visible (default)
mvn test -Dplaywright.browser.headless=false
```

### Override Application Location
```bash
# Test against different Craigslist locations/regions
mvn test -Dapp.location=london

# Other supported locations: madrid, london, newyork, losangeles, etc.
mvn test -Dapp.location=newyork

# Change base domain if needed
mvn test -Dapp.base.domain=https://craigslist.org
```

### Override Application URL Components
```bash
# Test against different pages
mvn test -Dapp.search.housing.page=/search/aaa
```

### Override Timeout
```bash
# Increase timeout for slower networks
mvn test -Dplaywright.timeout=60000
```

### Override Language/Region Parameters
```bash
# Test with different language settings
mvn test -Dapp.lang="?lang=es&cc=es"
```

### Multiple Overrides
```bash
# Combine multiple overrides
mvn test \
  -Dplaywright.browser.headless=true \
  -Dapp.location=london \
  -Dplaywright.timeout=10000
```

## Configuration Priority

Properties are resolved in this order:
1. **System properties** (via `-D` command-line flags) - Highest priority
2. **application-test.properties file** - Default values
3. **Hardcoded defaults in TestConfig** - Fallback values

This means command-line arguments always override file-based configuration.

## Configuration Files

- **application-test.properties** - Environment configuration (URLs, timeouts, browser settings)
- **TestFixtures.java** - Test data (search keywords, expected values)

## Parallel Test Execution

Tests are configured to run **in parallel** by default for faster execution:

### Default Configuration
- **Thread Count**: 4 concurrent threads
- **Execution Mode**: Methods run concurrently within and across test classes
- **JUnit 5**: Parallel execution enabled via system properties

### Run Tests with Custom Thread Count
```bash
# Use 8 threads instead of 4
mvn test -DthreadCount=8

# Run tests sequentially (disable parallel)
mvn test -DthreadCount=1
```

### Thread-Safe Design
The framework is designed for parallel execution:
- Each test thread gets its own `Playwright` instance via `ThreadLocal` in `DriverFactory`
- Browser instances are isolated per thread - no cross-contamination
- Screenshots are captured separately for each test
- Allure environment info is logged once per test run

### Why Parallel Execution?
✓ **Faster CI/CD pipelines** - 4 tests run simultaneously instead of sequentially  
✓ **Better resource utilization** - Modern multi-core CPUs stay busy  
✓ **Thread-safe design** - `ThreadLocal` ensures isolation  
✓ **Production-ready** - No flaky behavior from shared state
