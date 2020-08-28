package ru.netology.web;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private final static String activeStatus = "active";
    private final static String blockedStatus = "blocked";

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static Gson gson = new Gson();
    private static Faker faker = new Faker(new Locale("en"));


    private DataGenerator() {
    }

    private static void registrationUsers(TestUser userData) {
        given()
                .spec(requestSpec)
                .body(gson.toJson(userData))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static TestUser generateValidActive() {
        TestUser userData = new TestUser(faker.name().username(),
                faker.internet().password(true), activeStatus);
        registrationUsers(userData);
        return userData;
    }

    public static TestUser generateValidBlocked() {
        TestUser userData = new TestUser(faker.name().username(),
                faker.internet().password(true), blockedStatus);
        registrationUsers(userData);
        return userData;
    }

    public static TestUser generateInvalidLogin() {
        String password = faker.internet().password(true);
        TestUser userDataRegistration = new TestUser(faker.name().username(),
                password, activeStatus);
        registrationUsers(userDataRegistration);
        return new TestUser(faker.name().username(),
                password, activeStatus);
    }

    public static TestUser generateInvalidPassword() {
        String login = faker.name().username();
        TestUser userDataRegistration = new TestUser(login,
                faker.internet().password(true), activeStatus);
        registrationUsers(userDataRegistration);
        return new TestUser(login,
                faker.internet().password(true), activeStatus);
    }

}
