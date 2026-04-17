package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitUntilState;
import framework.config.TestConfig;
import framework.core.DriverFactory;
import framework.models.HousingResult;

import java.util.ArrayList;
import java.util.List;

public class HousingPage {
    public static final String SEARCH_SORT_MODE_ICON_ICOM_ARROW = ".cl-search-sort-mode > .icon.icom-.arrow";
    public static final String DIV_ITEMS_BUTTON = "div.items button";
    public static final String SPAN_LABEL = "span.label";
    public static final String SEARCH_HOUSING_STRING = "Search housing";
    public static final String FORM = "form";
    public static final String BUTTON_TYPE_SUBMIT = "button[type='submit']";
    public static final String SEARCH_RESULT = ".cl-search-result";
    public static final String A_POSTING_TITLE_SPAN_LABEL = "a.posting-title span.label";
    public static final String SPAN_PRICEINFO = "span.priceinfo";
    public static final String SPAN_RESULT_LOCATION = "span.result-location";

    Page page = DriverFactory.page();

    public void open() {
        String url = TestConfig.getBaseUrl() + TestConfig.getSearchHousingPage() + TestConfig.getLanguageParams();
        page.navigate(url, new Page.NavigateOptions()
                .setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.locator(SEARCH_RESULT).first().waitFor();
    }

    private void clickSearchIconArrow() {
        page.locator(SEARCH_SORT_MODE_ICON_ICOM_ARROW).click();
    }

    public List<String> getSortLabels() {
        // 1. Open dropdown first (adjust selector to your trigger)
        clickSearchIconArrow();

        // 2. Wait for dropdown to appear
        Locator sortItems = page.locator(DIV_ITEMS_BUTTON);
        sortItems.first().waitFor();

        // 3. Extract labels
        return sortItems.locator(SPAN_LABEL)
                .allTextContents()
                .stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    public void fillSearchField(String searchText) {
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName(SEARCH_HOUSING_STRING)).fill(searchText);
    }

    public List<HousingResult> getSearchResults() {
        Locator items = page.locator(SEARCH_RESULT);

        // 1. Wait for at least one result
        page.locator(SEARCH_RESULT).first().waitFor();

        List<HousingResult> results = new ArrayList<>();

        for (Locator item : items.all()) {
            String title = safeTextOptional(item.locator(A_POSTING_TITLE_SPAN_LABEL));

            // Skip items with empty titles
            // TODO Discuss with Developer why there's an empty title result in last entry in List and if it should be fixed
            //  on the application side instead of filtering it out here
            if (title.isEmpty()) {
                continue;
            }

            results.add(new HousingResult(
                    title,
                    safeTextOptional(item.locator(SPAN_PRICEINFO)),
                    safeTextOptional(item.locator(SPAN_RESULT_LOCATION))
            ));
        }

        return results;
    }

    private String safeTextOptional(Locator locator) {
        try {
            if (locator.count() == 0) return "";
            String value = locator.textContent();
            return value == null ? "" : value.trim();
        } catch (Exception e) {
            return "";
        }
    }

    public void clickSearchButton() {
        page.locator(FORM).locator(BUTTON_TYPE_SUBMIT).click();
        page.locator(SEARCH_RESULT).first().waitFor();
    }

    public void selectSortListBy(String sortBy) {
        clickSearchIconArrow();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(sortBy)).click();
    }


}
