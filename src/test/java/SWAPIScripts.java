import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class SWAPIScripts {

    static Response response;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://swapi.dev";
        response = given().get("/api/people");
    }
    @Test
    public void verifySuccessfulResponse() {
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200);
    }

    @Test
    public void verifyPeopleHeightMoreThan200() {
        String responseStr = response.asString();
        JsonPath js = new JsonPath(responseStr);
        int peopleCount = js.getInt("count");

        int count = 0;
        List<String> names = new ArrayList<>();

        for(int i = 1;i <= peopleCount + 1;i++){
            if(i == 17){
                i++;
            }
            responseStr = given().get("/api/people/" + i)
                    .then().assertThat().statusCode(200).extract().response().asString();
            js = new JsonPath(responseStr);
            String name = js.getString("name");
            int height = 0;

            try {
                height = js.getInt("height");
            }catch(NumberFormatException e){
                continue;
            }

            if(height > 200){
                count++;
                names.add(name);
            }
        }

        Assert.assertEquals(count, 10);
        Assert.assertEquals(names.get(0), "Darth Vader");
        Assert.assertEquals(names.get(1), "Chewbacca");
        Assert.assertEquals(names.get(2), "Roos Tarpals");
        Assert.assertEquals(names.get(3), "Rugor Nass");
        Assert.assertEquals(names.get(4), "Yarael Poof");
        Assert.assertEquals(names.get(5), "Lama Su");
        Assert.assertEquals(names.get(6), "Taun We");
        Assert.assertEquals(names.get(7), "Grievous");
        Assert.assertEquals(names.get(8), "Tarfful");
        Assert.assertEquals(names.get(9), "Tion Medon");

    }

    @Test
    public void verifyTotalPeople() {
        String responseStr = response.asString();
        JsonPath js = new JsonPath(responseStr);
        int peopleCount = js.getInt("count");

        Assert.assertEquals(peopleCount, 82);
    }

}
