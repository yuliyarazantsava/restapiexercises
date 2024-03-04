import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class CheckRestApiForAuthors {
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = TestData.BASE_URI;
    }

    @Test
    void authorsGetTest() {
        List<Authors> authors = given()
                .when()
                .contentType(ContentType.JSON)
                .get("/api/v1/Authors")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().jsonPath().getList(".", Authors.class);

        assertThat(authors.size()).isGreaterThan(500);

        for (Authors author : authors) {
            System.out.println("Author: " + author.getFirstName() + " " + author.getLastName());
        }
    }

    @Test
    void authorsPostTest() {

        Authors expectedAuthor = Authors.builder()
                .firstName("Lady")
                .lastName("Gaga")
                .idBook(15)
                .id(15)
                .build();

        Authors responseAsObject = given()
                .when()
                .contentType(ContentType.JSON)
                .body(expectedAuthor)
                .post("/api/v1/Authors")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(Authors.class);

        assertThat(responseAsObject).isEqualTo(expectedAuthor);

    }

    @Test
    void authorsGetTestForIdBook() {
        List<Authors> authors = given()
                .when()
                .contentType(ContentType.JSON)
                .get("/api/v1/Authors/authors/books/15")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().jsonPath().getList(".", Authors.class);

        assertThat(authors)
                .allMatch(author -> author.getIdBook() == 15);
    }

    @Test
    void authorsGetTestForId() {
        Authors expectedAuthor = Authors.builder()
                .id(15)
                .idBook(5)
                .firstName("First Name 15")
                .lastName("Last Name 15")
                .build();

        Authors responseAsObject = given()
                .when()
                .contentType(ContentType.JSON)
                .get("/api/v1/Authors/15")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(Authors.class);

        assertThat(responseAsObject.getId()).isEqualTo(expectedAuthor.getId());
        assertThat(responseAsObject.getIdBook()).isEqualTo(expectedAuthor.getIdBook());
        assertThat(responseAsObject.getFirstName()).isEqualTo(expectedAuthor.getFirstName());
        assertThat(responseAsObject.getLastName()).isEqualTo(expectedAuthor.getLastName());
    }

    @Test
    void authorsPutTest() {
        Authors expectedAuthor = Authors.builder()
                .id(5)
                .idBook(10)
                .firstName("Lady")
                .lastName("Gaga")
                .build();

        Authors responseAsObject = given()
                .when()
                .contentType(ContentType.JSON)
                .body(expectedAuthor)
                .put("/api/v1/Authors/22")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(Authors.class);

        assertThat(responseAsObject.getId()).isEqualTo(expectedAuthor.getId());
        assertThat(responseAsObject.getIdBook()).isEqualTo(expectedAuthor.getIdBook());
        assertThat(responseAsObject.getFirstName()).isEqualTo(expectedAuthor.getFirstName());
        assertThat(responseAsObject.getLastName()).isEqualTo(expectedAuthor.getLastName());
    }

    @Test
    void authorsDeleteTest() {

                given()
                .when()
                .contentType(ContentType.JSON)
                .delete("/api/v1/Authors/30")
                .then()
                .statusCode(HttpStatus.SC_OK);


    }



}
