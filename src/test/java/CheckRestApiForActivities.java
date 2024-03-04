import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class CheckRestApiForActivities {


    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = TestData.BASE_URI;
    }

    @Test
    void activitiesGetTest() {
        List<Activities> activities = given()
                .when()
                .contentType(ContentType.JSON)
                .get("/api/v1/Activities")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().jsonPath().getList(".", Activities.class);

        assertThat(activities.size()).isEqualTo(30);

        for (Activities activity : activities) {
            System.out.println("Title: " + activity.getTitle());
        }
    }

    @Test
    void activitiesPostTest() {

        Activities expectedActivity = Activities.builder()
                .id(100)
                .title("mama")
                .dueDate("2024-03-01T13:37:41.78Z")
                .completed(true)
                .build();

        Activities responseAsObject = given()
                .when()
                .contentType(ContentType.JSON)
                .body(expectedActivity)
                .post("/api/v1/Activities")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(Activities.class);

        assertThat(responseAsObject).isEqualTo(expectedActivity);

    }

    @Test
    void activitiesGetTestWithQueryParam() {
        Activities expectedActivity = Activities.builder()
                .id(3)
                .title("Activity 3")
                .completed(false)
                .build();

        Activities responseAsObject = given()
                .when()
                .contentType(ContentType.JSON)
                .get("/api/v1/Activities/3")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(Activities.class);

        assertThat(responseAsObject.getId()).isEqualTo(expectedActivity.getId());
        assertThat(responseAsObject.getTitle()).isEqualTo(expectedActivity.getTitle());
        assertThat(responseAsObject.isCompleted()).isEqualTo(expectedActivity.isCompleted());
        }
    }

