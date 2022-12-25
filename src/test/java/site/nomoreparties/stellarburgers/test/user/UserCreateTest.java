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

public class UserCreateTest {

    static User user;
    static User userDouble;

    //Create user
    private static final String userEmail  = "maxim7898752367@stellarburger.site";
    private static final String userName = "maxim";

    //Create user double test
    private static final String userEmailDouble = "double734345363@stellarburger.site";

    //Create user without name test
    private static final String userWithoutNameEmail = "without5388799@stellarburger.site";

    @BeforeClass
    public static void suiteSetup() {
        RestAssured.baseURI = BASE_URI;
        user = new User(userEmail, USER_PASSWORD, userName);
        userDouble = new User(userEmailDouble, USER_PASSWORD, userName);
        User resetUser = new User(userEmail, USER_PASSWORD, null);
        UserMethod.resetUser(resetUser);
        User resetUserDouble = new User(userEmailDouble, USER_PASSWORD, null);
        UserMethod.resetUser(resetUserDouble);
    }

    @Test
    @DisplayName("Check successful create user")
    public void checkCreateUser() {
        ValidatableResponse response = UserMethod.sendPostRequestCreateUser(user);
        CommonMethod.checkResponseCode(response, CODE_200);
        CommonMethod.checkResponseBody(response, FIELD_SUCCESS, true);
        CommonMethod.checkResponseBody(response, FIELD_USER_EMAIL, userEmail);
        CommonMethod.checkResponseBody(response, FIELD_USER_NAME, userName);
        CommonMethod.checkResponseBodyNotNullField(response, FIELD_ACCESS_TOKEN);
        CommonMethod.checkResponseBodyNotNullField(response, FIELD_REFRESH_TOKEN);
    }

    @Test
    @DisplayName("Check create exist user")
    public void checkCreateUserDouble() {
        ValidatableResponse response = UserMethod.sendPostRequestCreateUser(userDouble);
        CommonMethod.checkResponseCode(response, CODE_200);
        ValidatableResponse responseUserDouble = UserMethod.sendPostRequestCreateUser(userDouble);
        CommonMethod.checkResponseCode(responseUserDouble, CODE_403);
        CommonMethod.checkResponseBody(responseUserDouble, FIELD_SUCCESS, false);
        CommonMethod.checkResponseBody(responseUserDouble, FIELD_MESSAGE, VALUE_USER_ALREADY_EXISTS);
    }

    @Test
    @DisplayName("Check create user without field 'name'")
    public void checkCreateUserWithoutNameField() {
        User userWithoutName = new User(userWithoutNameEmail, USER_PASSWORD, null);
        ValidatableResponse response = UserMethod.sendPostRequestCreateUser(userWithoutName);
        CommonMethod.checkResponseCode(response, CODE_403);
        CommonMethod.checkResponseBody(response, FIELD_MESSAGE, VALUE_EMAIL_PASSWORD_NAME_ARE_REQUIRED_FIELDS);
    }

}
