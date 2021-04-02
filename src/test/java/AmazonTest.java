import amazon.AuthorPage;
import amazon.AuthorSearchPage;
import amazon.HomePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class AmazonTest {

    private WebDriver driver;
    private String authorName = "stephen king";

    @BeforeSuite
    private void setSystemProperties() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
    }

    @BeforeClass
    private void createDriverInstance() {
        driver = new ChromeDriver();
        driver.get("https://www.amazon.com/");
    }

    @Test
    private void testAuthorSearchAndBooksPriceSorting() throws Exception {
        HomePage homePage = new HomePage(driver);
        homePage.waitUntilPageLoads();

        String actualDeliveryCountry = homePage.getDeliveryTargetCountry();
        String expectedDeliveryCountry = "Deliver to Armenia";

        SoftAssert softAssert = new SoftAssert();
        String assertMessageForDeliveryAdd = "Delivery address differs from expected one.";
        softAssert.assertEquals(actualDeliveryCountry, expectedDeliveryCountry, assertMessageForDeliveryAdd);

        homePage.clickBooksFromNavBar();
        homePage.enterAuthorNameAndSearch(authorName);

        AuthorSearchPage authorSearchPage = new AuthorSearchPage(driver);
        authorSearchPage.waitUntilPageLoads();

        int actualCount = authorSearchPage.countOfSearchResultsContainingAuthor(authorName);
        int expectedCount = authorSearchPage.countOfResultsOnFirstPage();
        String assertMessageForAuthorNameCount = String.format("%s is not author in all search results on the firs page.",
                authorName.toUpperCase());

        softAssert.assertEquals(actualCount, expectedCount, assertMessageForAuthorNameCount);

        authorSearchPage.getLinkWithTargetedAuthorName(authorName).click();

        AuthorPage authorPage = new AuthorPage(driver);
        authorPage.waitUntilPageLoads();

        String expectedBooksByAuthorText = String.format("books by %s", authorName);
        String actualBooksByAuthorText = authorPage.booksByAuthorText();
        String assertMessageForAuthorName = "Author name in books by section field differs from expected one.";
        softAssert.assertEquals(actualBooksByAuthorText, expectedBooksByAuthorText, assertMessageForAuthorName);

        authorPage.sortByPriceLowToHigh();
        authorPage.waitUntilPageLoads();

        String assertMessageForBooksSort = "Books are not sorted from low to high by price.";
        List<Double> pricesOfBooks = authorPage.getPricesOfBooks();
        for (int i = 1; i < pricesOfBooks.size(); i++) {
            double current = pricesOfBooks.get(i);
            double previous = pricesOfBooks.get(i - 1);
            softAssert.assertTrue(previous <= current, assertMessageForBooksSort);
        }

        softAssert.assertAll();
    }

    @AfterClass
    private void tearDownDriverInstance() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
