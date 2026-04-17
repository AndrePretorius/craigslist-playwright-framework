package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import framework.config.TestConfig;
import framework.core.DriverFactory;
import framework.models.HousingResult;

import java.util.ArrayList;
import java.util.List;

public class HousingPage {
    Page page = DriverFactory.page();

    public void open() {
        String url = TestConfig.getBaseUrl() + TestConfig.getSearchHousingPage() + TestConfig.getLanguageParams();
        page.navigate(url);
    }

    public List<String> getSortLabels() {
        // 1. Open dropdown first (adjust selector to your trigger)
        page.locator(".cl-search-sort-mode > .icon.icom-.arrow").click();

        // 2. Wait for dropdown to appear
        Locator sortItems = page.locator("div.items button");
        sortItems.first().waitFor();

        // 3. Extract labels
        return sortItems.locator("span.label")
                .allTextContents()
                .stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    public void fillSearchField(String searchText) {
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search housing")).fill(searchText);
    }

    public void clickSearchButton() {
        page.locator("form").locator("button[type='submit']").click();
    }

    public List<HousingResult> getSearchResults() {
        Locator items = page.locator(".cl-search-result");

        // 1. Wait for at least one result
        items.first().waitFor();

        List<HousingResult> results = new ArrayList<>();

        for (Locator item : items.all()) {
            String title = safeTextOptional(item.locator("a.posting-title span.label"));

            // Skip items with empty titles
            if (title.isEmpty()) {
                continue;
            }

            results.add(new HousingResult(
                    title,
                    safeTextOptional(item.locator("span.priceinfo")),
                    safeTextOptional(item.locator("span.result-location"))
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

    public void selectSortListBy(String sortBy) {
        page.locator(".cl-search-sort-mode > .icon.icom-.arrow").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(sortBy)).click();
    }


}
