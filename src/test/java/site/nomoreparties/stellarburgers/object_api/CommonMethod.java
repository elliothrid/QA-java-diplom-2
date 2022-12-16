package site.nomoreparties.stellarburgers.object_api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CommonMethod {

    @Step("Check response code")
    public static void checkResponseCode(ValidatableResponse response, int code) {
        response.statusCode(code);
    }

    @Step("Check response body field value")
    public static void checkResponseBody(ValidatableResponse response, String checkedField, boolean expectedValue) {
        response.assertThat().body(checkedField, equalTo(expectedValue));
    }

    @Step("Check response body field value")
    public static void checkResponseBody(ValidatableResponse response, String checkedField, String expectedValue) {
        response.assertThat().body(checkedField, equalTo(expectedValue));
    }

    @Step("Check response body field is not null")
    public static void checkResponseBodyNotNullField(ValidatableResponse response, String checkedField) {
        response.assertThat().body(checkedField, notNullValue());
    }

}
