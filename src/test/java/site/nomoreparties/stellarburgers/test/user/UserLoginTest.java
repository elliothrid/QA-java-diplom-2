package site.nomoreparties.stellarburgers.test.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.BeforeClass;
import org.junit.Test;
import site.nomoreparties.stellarburgers.User;
import site.nomoreparties.stellarburgers.object_api.CommonMethod;
import site.nomoreparties.stellarburgers.object_api.UserMethod;

import static site.nomoreparties.stellarburgers.constant.ServiceName.*;
import static site.nomoreparties.stellarburgers.constant.StatusCode.*;

public class UserLoginTest {

    static User userLogin;

    //Create user
    private static final String userLoginEmail  = "oleg7898752367@stellarburger.site";
    private static final String userLoginPassword = "password";
    private static final String userLoginName = "oleg";
    private static final String userLoginJsonLogin = "{\"email\": \"oleg7898752367@stellarburger.site\", \"password\": \"password\"}";
    private static final String userLoginBodyFieldSuccess = "success";
    private static final String userLoginBodyFieldUserEmail = "user.email";
    private static final String userLoginBodyFieldUserName = "user.name";
    private static final String userLoginBodyFieldAccessToken = "accessToken";
    private static final String userLoginBodyFieldRefreshToken = "refreshToken";
    private static final Boolean userLoginBodyValueTrue = true;

    //Login user with wrong password
    private static final String userWithWrongPasswordJson = "{\"email\": \"oleg7898752367@stellarburger.site\", \"password\": \"wrong_password\"}";
    private static final String userWithWrongPasswordBodyFieldSuccessDouble = "success";
    private static final Boolean userWithWrongPasswordBodyValueSuccessDouble = false;
    private static final String userWithWrongPasswordBodyFieldMessageDouble = "message";
    private static final String userWithWrongPasswordBodyValueMessageDouble = "email or password are incorrect";

    @BeforeClass
    public static void suiteSetup() {
        RestAssured.baseURI = BASE_URI;
        userLogin = new User(userLoginEmail, userLoginPassword, userLoginName);
        UserMethod.sendPostRequestCreateUser(userLogin);
    }

    @Test
    @DisplayName("Check successful user login")
    public void checkLoginUser() {
        ValidatableResponse response = UserMethod.sendPostRequestLoginUser(userLoginJsonLogin);
        CommonMethod.checkResponseCode(response, CODE_200);
        CommonMethod.checkResponseBody(response, userLoginBodyFieldSuccess, userLoginBodyValueTrue);
        CommonMethod.checkResponseBody(response, userLoginBodyFieldUserEmail, userLoginEmail);
        CommonMethod.checkResponseBody(response, userLoginBodyFieldUserName, userLoginName);
        CommonMethod.checkResponseBodyNotNullField(response, userLoginBodyFieldAccessToken);
        CommonMethod.checkResponseBodyNotNullField(response, userLoginBodyFieldRefreshToken);
    }

    @Test
    @DisplayName("Check user login with wrong password")
    public void checkLoginUserWithWrongPassword() {
        ValidatableResponse response = UserMethod.sendPostRequestLoginUser(userWithWrongPasswordJson);
        CommonMethod.checkResponseCode(response, CODE_401);
        CommonMethod.checkResponseBody(response, userWithWrongPasswordBodyFieldSuccessDouble, userWithWrongPasswordBodyValueSuccessDouble);
        CommonMethod.checkResponseBody(response, userWithWrongPasswordBodyFieldMessageDouble, userWithWrongPasswordBodyValueMessageDouble);
    }

}
