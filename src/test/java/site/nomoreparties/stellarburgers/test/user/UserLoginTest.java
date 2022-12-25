package site.nomoreparties.stellarburgers.test.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.BeforeClass;
import org.junit.Test;
import site.nomoreparties.stellarburgers.User;
import site.nomoreparties.stellarburgers.object_api.CommonMethod;
import site.nomoreparties.stellarburgers.object_api.UserMethod;

import static site.nomoreparties.stellarburgers.constant.BodyFieldValue.*;
import static site.nomoreparties.stellarburgers.constant.ServiceName.*;
import static site.nomoreparties.stellarburgers.constant.StatusCode.*;
import static site.nomoreparties.stellarburgers.constant.User.*;

public class UserLoginTest {

    static User userLogin;
    private static final String userLoginEmail  = "oleg7898752367@stellarburger.site";
    private static final String userLoginName = "oleg";

    @BeforeClass
    public static void suiteSetup() {
        RestAssured.baseURI = BASE_URI;
        userLogin = new User(userLoginEmail, USER_PASSWORD, userLoginName);
        UserMethod.sendPostRequestCreateUser(userLogin);
    }

    @Test
    @DisplayName("Check successful user login")
    public void checkLoginUser() {
        User userToLogin = new User(userLoginEmail, USER_PASSWORD, null);
        ValidatableResponse response = UserMethod.sendPostRequestLoginUser(userToLogin);
        CommonMethod.checkResponseCode(response, CODE_200);
        CommonMethod.checkResponseBody(response, FIELD_SUCCESS, true);
        CommonMethod.checkResponseBody(response, FIELD_USER_EMAIL, userLoginEmail);
        CommonMethod.checkResponseBody(response, FIELD_USER_NAME, userLoginName);
        CommonMethod.checkResponseBodyNotNullField(response, FIELD_ACCESS_TOKEN);
        CommonMethod.checkResponseBodyNotNullField(response, FIELD_REFRESH_TOKEN);
    }

    @Test
    @DisplayName("Check user login with wrong password")
    public void checkLoginUserWithWrongPassword() {
        User userWithWrongPassword = new User(userLoginEmail, USER_WRONG_PASSWORD, null);
        ValidatableResponse response = UserMethod.sendPostRequestLoginUser(userWithWrongPassword);
        CommonMethod.checkResponseCode(response, CODE_401);
        CommonMethod.checkResponseBody(response, FIELD_SUCCESS, false);
        CommonMethod.checkResponseBody(response, FIELD_MESSAGE, VALUE_EMAIL_OR_PASSWORD_ARE_INCORRECT);
    }

}
