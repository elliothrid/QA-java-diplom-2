package site.nomoreparties.stellarburgers.object_api;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.User;

import static site.nomoreparties.stellarburgers.constant.Header.*;
import static site.nomoreparties.stellarburgers.constant.StatusCode.*;

public class UserMethod {

    public static final String USER_API_CREATE = "/api/auth/register";
    public static final String USER_API_DELETE = "/api/auth/user";
    public static final String USER_API_CHANGE =  "/api/auth/user";
    public static final String USER_API_LOGIN =  "/api/auth/login";

    private static final Filter requestFilter = new RequestLoggingFilter();
    private static final Filter responseFiler = new ResponseLoggingFilter();

    @Step("Send POST create user")
    public static ValidatableResponse sendPostRequestCreateUser(User user) {
        return RestAssured.with().filters(requestFilter, responseFiler).header(CONTENT_TYPE, APPLICATION_JSON).and().body(user)
                .when().post(USER_API_CREATE).then();
    }

    @Step("Send POST login user")
    public static ValidatableResponse sendPostRequestLoginUser(User user) {
        return RestAssured.with().filters(requestFilter, responseFiler).header(CONTENT_TYPE, APPLICATION_JSON).and()
                .body(user).when().post(USER_API_LOGIN).then();
    }

    @Step("Send PATCH change user")
    public static ValidatableResponse sendPatchRequestChangeUser(User user, String accessToken) {
        return RestAssured.with().filters(requestFilter, responseFiler).header(CONTENT_TYPE, APPLICATION_JSON).and()
                .header(AUTORIZATION, accessToken).body(user).when().patch(USER_API_CHANGE).then();
    }

    @Step("Send DELETE exist user")
    public static void sendDeleteExistUser(String accessToken) {
        RestAssured.with().filters(requestFilter, responseFiler).header(AUTORIZATION, accessToken)
                .when().delete(USER_API_DELETE).then();
    }

    @Step("Get access token")
    public static String getAccessToken(ValidatableResponse validatableResponse) {
        return validatableResponse.extract().path("accessToken").toString();
    }

    @Step("Reset (delete) exist user")
    public static void resetUser(User user) {
        ValidatableResponse response = sendPostRequestLoginUser(user);
        if (response.extract().statusCode() == CODE_200) {
            String accessToken = getAccessToken(response);
            sendDeleteExistUser(accessToken);
        }
    }

}
