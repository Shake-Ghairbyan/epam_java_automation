import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class GoRestTest {

    private final String token = "a27efba58da81f96b92922bc4ed805103264a09f6819ac931005cd609f7419fa";

    private final String body_template = """
            {
                "name": "Shake:%s",
                "email": "ShakeGh_11%s@epam.com",
                "gender": "Female",
                "status": "Inactive"
            }
            """;

    Header header = new Header("Authorization", "Bearer " + token);

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://gorest.co.in/";
        RestAssured.basePath = "public-api/";
    }

    @Test
    public void testPost() {

        final long unique_id = System.currentTimeMillis();
        String body = String.format(body_template, unique_id, unique_id);

        ValidatableResponse responsePost = RestAssured
                .given()
                .header(header)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("users")
                .then();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(Integer.parseInt(getJsonPathParam(responsePost, "code")), 201, "User was not created.");
        softAssert.assertEquals(getJsonPathParam(responsePost, "data.name"), String.format("Shake%s", unique_id), "User was not created.");
    }

    @Test
    public void testDuplicate() {

        final long unique_id = System.currentTimeMillis();
        String body = String.format(body_template, unique_id, unique_id);

        ValidatableResponse initialCheck = RestAssured
                .given()
                .header(header)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("users")
                .then();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(Integer.parseInt(getJsonPathParam(initialCheck, "code")), 201, "User was not created.");


        ValidatableResponse responseCheck = RestAssured
                .given()
                .header(header)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("users")
                .then();

        softAssert.assertEquals(Integer.parseInt(getJsonPathParam(responseCheck, "code")), 422, "Something went wrong! Duplicated user.");
        softAssert.assertAll();
    }

    @Test
    public void testDelete() {

        final long unique_id = System.currentTimeMillis();
        String body = String.format(body_template, unique_id, unique_id);

        ValidatableResponse initialCheck = RestAssured
                .given()
                .header(header)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("users")
                .then();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(Integer.parseInt(getJsonPathParam(initialCheck, "code")), 201, "User was not created.");

        String postedUserId = getJsonPathParam(initialCheck, "data.id");

        Response responseDelete = RestAssured
                .given()
                .header(header)
                .when()
                .delete("users/" + postedUserId)
                .then()
                .extract()
                .response();

        softAssert.assertEquals(responseDelete.statusCode(), 200, "User deletion failed.");
        softAssert.assertAll();
    }

    private String getJsonPathParam(ValidatableResponse response, String param) {
        return response.extract().jsonPath().get(param).toString();
    }
}
