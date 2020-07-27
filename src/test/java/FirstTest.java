import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static java.lang.String.format;
import static java.lang.System.out;
import static org.testng.Assert.assertEquals;

public class FirstTest {

    private String petEndpoint = "https://petstore.swagger.io/v2/pet/";
    private RequestSpecification httpRequest;
    private JsonPath getResponseBody(final Response response, final Boolean logResponseBody) {
        if (logResponseBody) {
            out.println(format("%s: Response Body is =>  %s", Thread.currentThread().getStackTrace()[2].getMethodName(), response.getBody().asString()));
        }
        return response.jsonPath();
    }

    @BeforeClass
    public void setUpTests() {
        RestAssured.baseURI = petEndpoint;
        httpRequest = RestAssured.given();
    }

    @Test(testName = "Should return 405 on malformed id")
    public void shouldReturn405OnMalformedId() {
        Response response = httpRequest.request(Method.GET, "");
        assertEquals(response.getStatusCode(), 405);
    }

    @Test(testName = "Should return 404 on not found animal.")
    public void shouldReturn404OnAnimalNotFound() {
        Response response = httpRequest.request(Method.GET, "10010");
        assertEquals(response.getStatusCode(), 404);
        assertEquals(getResponseBody(response, true).get("message"), "Pet not found");
    }

    @Test(testName = "Should return with 200 on animal found")
    public void shouldReturn200OnAnimalFound() {
        Response response = httpRequest.request(Method.GET, "3");
        assertEquals(response.getStatusCode(), 200);
        assertEquals(getResponseBody(response, true).get("name"), "doggie");
        assertEquals(getResponseBody(response, false).get("status"), "available");
    }
}
