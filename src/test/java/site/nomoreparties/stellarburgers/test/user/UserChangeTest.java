package site.nomoreparties.stellarburgers.test.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.BeforeClass;
import org.junit.Test;
import site.nomoreparties.stellarburgers.User;
import site.nomoreparties.stellarburgers.object_api.CommonMethod;
import site.nomoreparties.stellarburgers.object_api.UserMethod;

import static site.nomoreparties.stellarburgers.constant.AccessToken.*;
import static site.nomoreparties.stellarburgers.constant.BodyFieldValue.*;
import static site.nomoreparties.stellarburgers.constant.ServiceName.*;
import static site.nomoreparties.stellarburgers.constant.StatusCode.*;
import static site.nomoreparties.stellarburgers.constant.User.*;

public class UserChangeTest {

    static User user;
    private static final String userEmail  = "masha7898752367@stellarburger.site";
    private static final String userName = "masha";
    private static final String userNewEmail = "maria7898752367@stellarburger.site";
    private static final String userNewName  = "maria";

    @BeforeClass
    public static void suiteSetup() {
        RestAssured.baseURI = BASE_URI;
        User resetUser = new User(userEmail, USER_PASSWORD, null);
        UserMethod.resetUser(resetUser);
        User resetUserWithNewPassword = new User(userNewEmail, USER_PASSWORD, null);
        UserMethod.resetUser(resetUserWithNewPassword);
        User resetUserWithNewName = new User(userEmail, USER_NEW_PASSWORD, null);
        UserMethod.resetUser(resetUserWithNewName);
        user = new User(userEmail, USER_PASSWORD, userName);
        ValidatableResponse createdUserResponse = UserMethod.sendPostRequestCreateUser(user);
        String token = UserMethod.getAccessToken(createdUserResponse);
        user.setAccessToken(token);
    }

    @Test
    @DisplayName("Check changing login on authorized user")
    public void checkAuthorizedUserChangeFieldLogin() {
        User userToChangeWithNewEmail = new User(userNewEmail, USER_PASSWORD, userName);
        ValidatableResponse response = UserMethod.sendPatchRequestChangeUser(userToChangeWithNewEmail, user.getAccessToken());
        CommonMethod.checkResponseCode(response, CODE_200);
        CommonMethod.checkResponseBody(response, FIELD_SUCCESS, true);
        CommonMethod.checkResponseBody(response, "user.email", userNewEmail);
    }

    @Test
    @DisplayName("Check changing password on authorized user")
    public void checkAuthorizedUserChangeFieldPassword() {
        User userToChangeWithNewEmail = new User(userEmail, USER_NEW_PASSWORD, userName);
        ValidatableResponse response = UserMethod.sendPatchRequestChangeUser(userToChangeWithNewEmail, user.getAccessToken());
        CommonMethod.checkResponseCode(response, CODE_200);
        CommonMethod.checkResponseBody(response, FIELD_SUCCESS, true);
    }

    @Test
    @DisplayName("Check changing name on authorized user")
    public void checkAuthorizedUserChangeFieldName() {
        User userToChangeWithNewEmail = new User(userEmail, USER_PASSWORD, userNewName);
        ValidatableResponse response = UserMethod.sendPatchRequestChangeUser(userToChangeWithNewEmail, user.getAccessToken());
        CommonMethod.checkResponseCode(response, CODE_200);
        CommonMethod.checkResponseBody(response, FIELD_SUCCESS, true);
        CommonMethod.checkResponseBody(response, "user.name", userNewName);
    }

    @Test
    @DisplayName("Check changing login on unauthorized user")
    public void checkUnauthorizedUserChangeFieldLogin() {
        User userWithNewEmail = new User(userNewEmail, USER_PASSWORD, userName);
        ValidatableResponse response = UserMethod.sendPatchRequestChangeUser(userWithNewEmail, EMPTY_ACCESS_TOKEN);
        CommonMethod.checkResponseCode(response, CODE_401);
        CommonMethod.checkResponseBody(response, FIELD_SUCCESS, false);
        CommonMethod.checkResponseBody(response, FIELD_MESSAGE, VALUE_YOU_SHOULD_BE_AUTHORISED);
    }

    @Test
    @DisplayName("Check changing password on unauthorized user")
    public void checkUnauthorizedUserChangeFieldPassword() {
        User userWithNewEmail = new User(userNewEmail, USER_PASSWORD, userName);
        ValidatableResponse response = UserMethod.sendPatchRequestChangeUser(userWithNewEmail, EMPTY_ACCESS_TOKEN);
        CommonMethod.checkResponseCode(response, CODE_401);
        CommonMethod.checkResponseBody(response, FIELD_SUCCESS, false);
        CommonMethod.checkResponseBody(response, FIELD_MESSAGE, VALUE_YOU_SHOULD_BE_AUTHORISED);
    }

    @Test
    @DisplayName("Check changing name on unauthorized user")
    public void checkUnauthorizedUserChangeFieldName() {
        User userWithNewEmail = new User(userNewEmail, USER_PASSWORD, userName);
        ValidatableResponse response = UserMethod.sendPatchRequestChangeUser(userWithNewEmail, EMPTY_ACCESS_TOKEN);
        CommonMethod.checkResponseCode(response, CODE_401);
        CommonMethod.checkResponseBody(response, FIELD_SUCCESS, false);
        CommonMethod.checkResponseBody(response, FIELD_MESSAGE, VALUE_YOU_SHOULD_BE_AUTHORISED);
    }

}
