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

        //Verify that the people endpoint is returning a successful response
        Assert.assertEquals(statusCode,200);
    }

    @Test
    public void verifyPeopleHeightMoreThan200() {
        //Convert the response to string
        String responseStr = response.asString();

        //Get the count value from the response
        JsonPath js = new JsonPath(responseStr);
        int peopleCount = js.getInt("count");

        //This variable is used for the count of results where height > 200
        int count = 0;

        //This is where the list of names from the result where height > 200 will be saved
        List<String> names = new ArrayList<>();

        for(int i = 1;i <= peopleCount + 1;i++){

            //Result 17 is null, i++ to go straight to Result 18
            if(i == 17){
                i++;
            }

            responseStr = given().get("/api/people/" + i)
                    .then().assertThat().statusCode(200).extract().response().asString();
            js = new JsonPath(responseStr);
            //Get name from the result
            String name = js.getString("name");
            int height = 0;

            //Get height from the result
            try {
                height = js.getInt("height");
            }catch(NumberFormatException e){
                //If height is not a valid number just continue to the next loop
                continue;
            }

            //Check if height > 200, if yes add 1 to count and add the name to the list.
            if(height > 200){
                count++;
                names.add(name);
            }
        }

        //Verify that the total number of people where height is greater than 200 matches the expected count(10)
        Assert.assertEquals(count, 10);

        //Verify that the 10 individuals returned are:
        //Darth Vader * Chewbacca * Roos Tarpals * Rugor Nass * Yarael Poof
        //Lama Su * Tuan We * Grievous * Tarfful * Tion Medon
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

        //Verify that the total number of people checked equals the expected count (82)
        Assert.assertEquals(peopleCount, 82);
    }

}
