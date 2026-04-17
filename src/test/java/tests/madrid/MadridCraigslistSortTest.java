package tests.madrid;

import framework.fixtures.TestFixtures;
import framework.models.HousingResult;
import framework.pages.HousingPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Feature("Housing Page")
@Story("Sorting Functionality")
@DisplayName("Housing Page Sorting Tests")
public class MadridCraigslistSortTest extends tests.BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(MadridCraigslistSortTest.class);

    static {
        System.setProperty("app.location", "madrid");
    }

    @Test
    @Description("TC-1 Test Price Low to High sorting")
    @DisplayName("Should allow sorting by Price: Low to High")
    void verifyAscendingPriceSortTest() {
        List<HousingResult> expectedSearchList = List.of(
                new HousingResult(
                        "Fully Furnished Private House with Garden & Pool in Arturo Soria",
                        "€5200",
                        "Arturo Soria"
                ),
                new HousingResult(
                        "Traditional House",
                        "€120.000",
                        "Toledo"
                )
        );

        HousingPage housingPage = new HousingPage();
        housingPage.open();
        housingPage.selectSortListBy("£ → £££");
        List<HousingResult> actuallSearhResultsList = housingPage.getSearchResults();

        assertEquals(expectedSearchList, actuallSearhResultsList);
        logger.info("TEST: verifyAscendingPriceSortTest - PASSED");
    }

    @Test
    @Description("TC-2 Test Price High to Low sorting")
    @DisplayName("Should allow sorting by Price: High to Low")
    void verifyDescendingPriceSortTest() {
        List<HousingResult> expectedSearchList = List.of(
                new HousingResult(
                        "Traditional House",
                        "€120.000",
                        "Toledo"
                ),
                new HousingResult(
                        "Fully Furnished Private House with Garden & Pool in Arturo Soria",
                        "€5200",
                        "Arturo Soria"
                )
        );

        HousingPage housingPage = new HousingPage();
        housingPage.open();
        housingPage.selectSortListBy("£££ → £");
        List<HousingResult> actuallSearhResultsList = housingPage.getSearchResults();

        assertEquals(expectedSearchList, actuallSearhResultsList);
        logger.info("TEST: verifyDescendingPriceSortTest - PASSED");
    }

    @Test
    @Description("TC-3 Verify all default sort options are available")
    @DisplayName("Should display all default sorting options")
    void verifySortListDefaultValueTest() {
        List<String> expectedList = List.of(
                "£ → £££",
                "£££ → £",
                "newest"
        );
        HousingPage housingPage = new HousingPage();
        housingPage.open();
        List<String> actualList = housingPage.getSortLabels();

        assertEquals(expectedList, actualList);
        logger.info("TEST: verifySortListDefaultValueTest - PASSED");
    }

    @Test
    @Description("TC-4 Verify Upcoming and Relevant sort option appears after search")
    @DisplayName("Should have 'Upcoming' and 'Relevant' sort option available after search")
    void verifySortListAfterSearchingTest() {
        List<String> expectedList = List.of(
                "£ → £££",
                "£££ → £",
                "newest",
                "upcoming",
                "relevant"
        );
        HousingPage housingPage = new HousingPage();
        housingPage.open();
        housingPage.fillSearchField(TestFixtures.Madrid.getSearchKeyword());
        housingPage.clickSearchButton();
        List<String> actualList = housingPage.getSortLabels();

        assertEquals(expectedList, actualList);
        logger.info("TEST: verifySortListAfterSearchingTest - PASSED");
    }

}
