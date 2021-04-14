import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class GoRestTest {

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://gorest.co.in/";
        RestAssured.basePath = "public-api/";
    }

    @Test
    public void testGoRest() {
        final String token = "a27efba58da81f96b92922bc4ed805103264a09f6819ac931005cd609f7419fa";

        final String body = """
                    {
                    "name": "Shake",
                    "email": "ShakeGh_11@epam.com",
                    "gender": "Female",
                    "status": "Inactive"
                }
                """;

        Header header = new Header("Authorization", "Bearer " + token);

        ValidatableResponse responsePost = RestAssured
                .given()
                .header(header)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("users")
                .then();

        responsePost.extract().response().prettyPrint();

        String postedUserId = responsePost.extract().jsonPath().get("data.id").toString();
        String postedUserName = responsePost.extract().jsonPath().get("data.name").toString();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(postedUserName, "Shake", "User creation failed.");


        ValidatableResponse responseCheck = RestAssured
                .given()
                .header(header)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("users")
                .then();

        int duplicatedResUser = responseCheck.extract().response().jsonPath().get("code");
        softAssert.assertEquals(duplicatedResUser, 422, "Something went wrong! Duplicated user.");


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
}
