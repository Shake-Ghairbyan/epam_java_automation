import amazon.AuthorPage;
import amazon.AuthorSearchPage;
import amazon.HomePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

public class AmazonTest {

    private WebDriver driver;
    private String authorName = "jane austen";

    @BeforeSuite
    private void setSystemProperties() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
    }

    @BeforeClass
    private void createDriverInstance() {
        driver = new ChromeDriver();
        driver.get("https://www.amazon.com/");
    }

    @Test(priority = 1)
    private void testHomePage() {
        HomePage homePage = new HomePage(driver);
        homePage.waitUntilPageLoads();

        String actualDeliveryCountry = homePage.getDeliveryTargetCountry();
        String expectedDeliveryCountry = "Deliver to Armenia";

        SoftAssert softAssert = new SoftAssert();
        String assertMessage = "Delivery address differs from expected one.";
        softAssert.assertEquals(actualDeliveryCountry, expectedDeliveryCountry, assertMessage);

        homePage.clickBooksFromNavBar();
        homePage.enterAuthorNameAndSearch(authorName);
    }

    @Test(priority = 2)
    private void testAuthorSearchPage() throws Exception {

        AuthorSearchPage authorSearchPage = new AuthorSearchPage(driver);
        authorSearchPage.waitUntilPageLoads();

        int actualCount = authorSearchPage.countOfSearchResultsContainingAuthor(authorName);
        int expectedCount = authorSearchPage.countOfResultsOnFirsPage();
        String assertMessage = String.format("%s is not author in all search results on the firs page.", authorName.toUpperCase());

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(actualCount, expectedCount, assertMessage);

        authorSearchPage.authorNameIsClickable(authorName).click();
    }

    @Test(priority = 3)
    private void testAuthorPage() {
        AuthorPage authorPage = new AuthorPage(driver);
        authorPage.waitUntilPageLoads();

        SoftAssert softAssert = new SoftAssert();
        String expectedBooksByAuthorText = String.format("books by %s", authorName);
        String actualBooksByAuthorText = authorPage.booksByAuthorText();
        String assertMessageAuthor = "Author name in books by section field differs from expected one.";
        softAssert.assertEquals(actualBooksByAuthorText, expectedBooksByAuthorText, assertMessageAuthor);

        authorPage.sortByPriceLowToHigh();
        authorPage.waitUntilPageLoads();

        boolean assertCond = authorPage.areSortedByPriceLowToHigh();
        softAssert.assertTrue(assertCond, "Books are not sorted from low to high by price.");
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
