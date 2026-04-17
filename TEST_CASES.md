# Test Cases Documentation

This document describes all test cases in the Craigslist Playwright Framework.

## Overview

The framework currently contains **4 test cases** focused on validating sort functionality in the Craigslist housing search feature.

---

## Test Case 1: Verify Ascending Price Sort

**Test Method**: `verifyAscendingPriceSortTest()`  
**File**: `src/test/java/tests/madrid/MadridCraigslistSortTest.java`

### Description
Validates that properties are sorted in ascending price order when the "£ → £££" (ascending) sort option is selected.

### Prerequisites
- Application is accessible at base URL
- Housing search page is loaded
- At least 2 properties are available for sorting

### Test Steps
1. Open the Craigslist Madrid housing search page
2. Click the sort dropdown menu
3. Select the "£ → £££" (ascending price) sort option
4. Retrieve the search results

### Expected Results
Results are returned in ascending price order:
1. "Fully Furnished Private House with Garden & Pool in Arturo Soria" - €5200
2. "Traditional House" - €120.000

### Test Data
- Sort label: `"£ → £££"`
- Expected results: 2 properties sorted by ascending price

### Assertions
- `assertEquals(expectedSearchList, actuallSearhResultsList)` - Verifies exact match of results in order

---

## Test Case 2: Verify Descending Price Sort

**Test Method**: `verifyDescendingPriceSortTest()`  
**File**: `src/test/java/tests/madrid/MadridCraigslistSortTest.java`

### Description
Validates that properties are sorted in descending price order when the "£££ → £" (descending) sort option is selected.

### Prerequisites
- Application is accessible at base URL
- Housing search page is loaded
- At least 2 properties are available for sorting

### Test Steps
1. Open the Craigslist Madrid housing search page
2. Click the sort dropdown menu
3. Select the "£££ → £" (descending price) sort option
4. Retrieve the search results

### Expected Results
Results are returned in descending price order (highest to lowest):
1. "Traditional House" - €120.000
2. "Fully Furnished Private House with Garden & Pool in Arturo Soria" - €5200

### Test Data
- Sort label: `"£££ → £"`
- Expected results: 2 properties sorted by descending price

### Assertions
- `assertEquals(expectedSearchList, actuallSearhResultsList)` - Verifies exact match of results in reverse order

---

## Test Case 3: Verify Sort List Default Value

**Test Method**: `verifySortListDefaultValueTest()`  
**File**: `src/test/java/tests/madrid/MadridCraigslistSortTest.java`

### Description
Validates that the sort dropdown displays the correct default sort options when no search query has been entered.

### Prerequisites
- Application is accessible at base URL
- Housing search page is loaded
- No search query has been submitted

### Test Steps
1. Open the Craigslist Madrid housing search page
2. Click the sort dropdown menu to expand it
3. Extract all available sort option labels
4. Verify the labels match the expected default options

### Expected Results
Sort dropdown displays exactly 3 options in this order:
1. "£ → £££" (ascending price)
2. "£££ → £" (descending price)
3. "newest" (newest first)

### Test Data
- Expected sort labels are hardcoded in the test

### Assertions
- `assertEquals(expectedList, actualList)` - Verifies exact match of sort option labels

---

## Test Case 4: Verify Sort List After Searching

**Test Method**: `verifySortListAfterSearchingTest()`  
**File**: `src/test/java/tests/madrid/MadridCraigslistSortTest.java`

### Description
Validates that the sort dropdown displays additional sort options after a search query is submitted. More options become available when filtering by search keyword.

### Prerequisites
- Application is accessible at base URL
- Housing search page is loaded
- Search keyword is available in test fixtures

### Test Steps
1. Open the Craigslist Madrid housing search page
2. Enter search keyword "traditional" in the search field
3. Click the Search button
4. Wait for results to load (pagination element visible)
5. Click the sort dropdown menu to expand it
6. Extract all available sort option labels
7. Verify the labels match the expected options (default + additional)

### Expected Results
Sort dropdown displays 5 options in this order:
1. "£ → £££" (ascending price)
2. "£££ → £" (descending price)
3. "newest" (newest first)
4. "upcoming" (upcoming listings)
5. "relevant" (relevance to search)

### Test Data
- Search keyword: `TestFixtures.getSearchKeyword()` → "traditional"
- Expected sort labels are hardcoded in the test

### Assertions
- `assertEquals(expectedList, actualList)` - Verifies exact match of all 5 sort option labels

---

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Case
```bash
# Run Test Case 1 (Ascending Price Sort)
mvn test -Dtest=MadridCraigslistSortTest#verifyAscendingPriceSortTest

# Run Test Case 2 (Descending Price Sort)
mvn test -Dtest=MadridCraigslistSortTest#verifyDescendingPriceSortTest

# Run Test Case 3 (Default Sort Options)
mvn test -Dtest=MadridCraigslistSortTest#verifySortListDefaultValueTest

# Run Test Case 4 (Sort Options After Search)
mvn test -Dtest=MadridCraigslistSortTest#verifySortListAfterSearchingTest
```

### Run Tests in Headless Mode
```bash
mvn test -Dplaywright.browser.headless=true
```

### Run Tests with Custom Thread Count
```bash
# Run with 8 parallel threads
mvn test -DthreadCount=8

# Run sequentially (single thread)
mvn test -DthreadCount=1
```

### Generate Allure Report
```bash
# Run tests and serve report (recommended - generates and serves report automatically)
mvn clean test allure:serve

# Or run tests first, then serve
mvn clean test
mvn allure:serve
```

**Note**: The build is configured with `testFailureIgnore=true`, so the Allure report will be generated even when tests fail. This allows you to review test results and debugging information in the report.

---

## Test Data

All test data is centralized in `TestFixtures.java`:

### Fixture Methods Used
- `TestFixtures.getSearchKeyword()` - Returns "traditional"

**Note**: Sort labels are hardcoded in test assertions for clarity and maintenance.

---

## Configuration

Test configuration is managed via `application-test.properties`:

```properties
# Playwright Configuration
playwright.browser.headless=false
playwright.browser.type=chromium
playwright.timeout=5000

# Application URLs
app.base.domain=https://craigslist.org
app.location=madrid
app.search.housing.page=/search/hhh
app.lang=?lang=en&cc=gb
```

### Override Configuration
```bash
# Run in headless mode
mvn test -Dplaywright.browser.headless=true

# Test different location
mvn test -Dapp.location=london
mvn test -Dapp.location=newyork

# Increase timeout for slow networks
mvn test -Dplaywright.timeout=60000
```

---

## Reporting

### Screenshots
- Automatically captured for all tests
- Saved to: `target/screenshots/{testName}.png`
- Attached to Allure report

### Allure Report
- Located at: `target/allure-report/index.html`
- Environment tab shows:
  - Application configuration
  - Command-line overrides used
  - Java and OS information
- Each test shows:
  - Pass/fail status
  - Duration
  - Screenshots
  - Execution details

---

## Test Isolation & Parallelization

- **Parallel Execution**: Tests run in 4 concurrent threads by default
- **Thread Safety**: Each thread has its own `Playwright` instance via `ThreadLocal`
- **No Cross-Contamination**: Browser instances are completely isolated
- **Scalability**: Easily adjust with `-DthreadCount=N`

---

## Dependencies

### Page Objects Used
- `HousingPage` - Encapsulates all housing search interactions

### Methods Used from HousingPage
- `open()` - Navigate to housing search page
- `getSortLabels()` - Extract sort dropdown options
- `fillSearchField(String)` - Enter search keyword
- `clickSearchButton()` - Submit search
- `selectSortListBy(String)` - Select sort option
- `getSearchResults()` - Retrieve paginated results

### Test Base Class
- `BaseTest` - Provides:
  - Browser setup/teardown
  - Screenshot capture
  - Allure environment logging
  - SLF4J logger

---

## Test Status & Notes

| Test Case | Status | Notes |
|-----------|--------|-------|
| Test 1 - Ascending Price Sort | ⚠️ | Framework implemented correctly, but requires real application data for validation |
| Test 2 - Descending Price Sort | ⚠️ | Framework implemented correctly, but requires real application data for validation |
| Test 3 - Default Sort Options | ❌ | **FAILS** - Application shows 7 options instead of required 3 (price ⬆, price ⬇, newest) |
| Test 4 - Sort After Search | ❌ | **FAILS** - Application shows 7 options instead of required 5 (price ⬆, price ⬇, newest, upcoming, relevant) |

### Current Application Behavior (April 17, 2026)
- **Actual default sort options**: `[newest, oldest, distance, £ → £££, £££ → £, relevance, upcoming]`
- **Actual after-search sort options**: `[newest, oldest, distance, £ → £££, £££ → £, relevance, upcoming]`
- **Expected default sort options**: `[£ → £££, £££ → £, newest]`
- **Expected after-search sort options**: `[£ → £££, £££ → £, newest, upcoming, relevant]`

### Findings
- **Tests are correct** and align with task requirements
- **Application under construction** shows additional sort options not specified in requirements
- **Application needs updates** to limit sort options to only those required by the task
- **Framework is robust** and will pass once application matches specifications
- **Location-based testing** successfully implemented and working

### Recommendations
1. Update application to show only required sort options
2. Remove extra options: `oldest`, `distance`, `relevance` from default view
3. Ensure search functionality adds only `upcoming` and `relevant` options
4. Re-run tests after application fixes to confirm compliance

---

## Future Enhancements

- Add tests for additional sort types (location, date range)
- Add tests for sorting with multiple filters
- Add performance benchmarks for sort operations
- Add tests for pagination with sorted results
- Add tests for remember sort preference across sessions

