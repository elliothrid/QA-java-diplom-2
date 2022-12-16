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

public class UserCreateTest {

    static User user;
    static User userDouble;

    //Create user
    private static final String userEmail  = "maxim7898752367@stellarburger.site";
    private static final String userPassword = "password";
    private static final String userName = "maxim";
    private static final String userJsonLogin = "{\"email\": \"maxim7898752367@stellarburger.site\", \"password\": \"password\"}";
    private static final String userBodyFieldSuccess = "success";
    private static final String userBodyFieldUserEmail = "user.email";
    private static final String userBodyFieldUserName = "user.name";
    private static final String userBodyFieldAccessToken = "accessToken";
    private static final String userBodyFieldRefreshToken = "refreshToken";
    private static final Boolean userBodyValueTrue = true;

    //Create user double test
    private static final String userEmailDouble = "double734345363@stellarburger.site";
    private static final String userJsonLoginDouble = "{\"email\": \"double734345363@stellarburger.site\", \"password\": \"password\"}";
    private static final String userBodyFieldSuccessDouble = "success";
    private static final Boolean userBodyValueSuccessDouble = false;
    private static final String userBodyFieldMessageDouble = "message";
    private static final String userBodyValueMessageDouble = "User already exists";

    //Create user without name test
    private static final String userJsonWithoutLoginField = "{\"email\": \"without5388799@stellarburger.site\", \"password\": \"password\"}";
    private static final String userBodyFieldMessageWithoutLoginField  = "message";
    private static final String userBodyValueMessageWithoutLoginField  = "Email, password and name are required fields";

    @BeforeClass
    public static void suiteSetup() {
        RestAssured.baseURI = BASE_URI;
        user = new User(userEmail, userPassword, userName);
        userDouble = new User(userEmailDouble, userPassword, userName);
        UserMethod.resetUser(userJsonLogin);
        UserMethod.resetUser(userJsonLoginDouble);
    }

    @Test
    @DisplayName("Check successful create user")
    public void checkCreateUser() {
        ValidatableResponse response = UserMethod.sendPostRequestCreateUser(user);
        CommonMethod.checkResponseCode(response, CODE_200);
        CommonMethod.checkResponseBody(response, userBodyFieldSuccess, userBodyValueTrue);
        CommonMethod.checkResponseBody(response, userBodyFieldUserEmail, userEmail);
        CommonMethod.checkResponseBody(response, userBodyFieldUserName, userName);
        CommonMethod.checkResponseBodyNotNullField(response, userBodyFieldAccessToken);
        CommonMethod.checkResponseBodyNotNullField(response, userBodyFieldRefreshToken);
    }

    @Test
    @DisplayName("Check create exist user")
    public void checkCreateUserDouble() {
        ValidatableResponse response = UserMethod.sendPostRequestCreateUser(userDouble);
        CommonMethod.checkResponseCode(response, CODE_200);
        ValidatableResponse responseUserDouble = UserMethod.sendPostRequestCreateUser(userDouble);
        CommonMethod.checkResponseCode(responseUserDouble, CODE_403);
        CommonMethod.checkResponseBody(responseUserDouble, userBodyFieldSuccessDouble, userBodyValueSuccessDouble);
        CommonMethod.checkResponseBody(responseUserDouble, userBodyFieldMessageDouble, userBodyValueMessageDouble);
    }

    @Test
    @DisplayName("Check create user without field 'name'")
    public void checkCreateUserWithoutNameField() {
        ValidatableResponse response = UserMethod.sendPostRequestCreateUser(userJsonWithoutLoginField);
        CommonMethod.checkResponseCode(response, CODE_403);
        CommonMethod.checkResponseBody(response, userBodyFieldMessageWithoutLoginField, userBodyValueMessageWithoutLoginField);
    }

}
