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

public class UserChangeTest {

    static User user;

    //User authorized
    private static final String userEmail  = "masha7898752367@stellarburger.site";
    private static final String userPassword = "password";
    private static final String userName = "masha";
    private static final String userJson  = "{\"email\": \"masha7898752367@stellarburger.site\", \"password\": \"password\"}";
    private static final String userFieldSuccess = "success";
    private static final Boolean userValueTrue = true;

    //User unauthorized
    private static final String unauthorizedUserFieldSuccess = "success";
    private static final Boolean unauthorizedUserValueFalse = false;
    private static final String unauthorizedUserBodyFieldMessage = "message";
    private static final String unauthorizedUserBodyValueMessage = "You should be authorised";
    private static final String unauthorizedUserEmptyToken = "";

    //User to change - field "email"
    private static final String userToChangeEmailJson  = "{\"email\": \"maria7898752367@stellarburger.site\", \"password\": \"password\"}";
    private static final String userToChangeEmailNewEmailJson  = "{\"email\": \"maria7898752367@stellarburger.site\", \"password\": \"password\", \"name\": \"masha\"}";
    private static final String userToChangeEmailNewEmail  = "maria7898752367@stellarburger.site";

    //User to change - field "password"
    private static final String userToChangePasswordJson  = "{\"email\": \"masha7898752367@stellarburger.site\", \"password\": \"new_password\"}";
    private static final String userToChangePasswordNewPasswordJson = "{\"email\": \"masha7898752367@stellarburger.site\", \"password\": \"new_password\", \"name\": \"masha\"}";

    //User to change - field "name"
    private static final String userToChangeNewNameJson = "{\"email\": \"masha7898752367@stellarburger.site\", \"password\": \"password\", \"name\": \"olga\"}";
    private static final String userToChangeNewName  = "olga";

    @BeforeClass
    public static void suiteSetup() {
        RestAssured.baseURI = BASE_URI;
        UserMethod.resetUser(userJson);
        UserMethod.resetUser(userToChangeEmailJson);
        UserMethod.resetUser(userToChangePasswordJson);
        user = new User(userEmail, userPassword, userName);
        ValidatableResponse createdUserResponse = UserMethod.sendPostRequestCreateUser(user);
        String token = UserMethod.getAccessToken(createdUserResponse);
        user.setAccessToken(token);
    }

    @Test
    @DisplayName("Check changing login on authorized user")
    public void checkAuthorizedUserChangeFieldLogin() {
        ValidatableResponse response = UserMethod.sendPatchRequestChangeUser(userToChangeEmailNewEmailJson, user.getAccessToken());
        CommonMethod.checkResponseCode(response, CODE_200);
        CommonMethod.checkResponseBody(response, userFieldSuccess, userValueTrue);
        CommonMethod.checkResponseBody(response, "user.email", userToChangeEmailNewEmail);
    }

    @Test
    @DisplayName("Check changing password on authorized user")
    public void checkAuthorizedUserChangeFieldPassword() {
        ValidatableResponse response = UserMethod.sendPatchRequestChangeUser(userToChangePasswordNewPasswordJson, user.getAccessToken());
        CommonMethod.checkResponseCode(response, CODE_200);
        CommonMethod.checkResponseBody(response, userFieldSuccess, userValueTrue);
    }

    @Test
    @DisplayName("Check changing name on authorized user")
    public void checkAuthorizedUserChangeFieldName() {
        ValidatableResponse response = UserMethod.sendPatchRequestChangeUser(userToChangeNewNameJson, user.getAccessToken());
        CommonMethod.checkResponseCode(response, CODE_200);
        CommonMethod.checkResponseBody(response, userFieldSuccess, userValueTrue);
        CommonMethod.checkResponseBody(response, "user.name", userToChangeNewName);
    }

    @Test
    @DisplayName("Check changing login on unauthorized user")
    public void checkUnauthorizedUserChangeFieldLogin() {
        ValidatableResponse response = UserMethod.sendPatchRequestChangeUser(userToChangeEmailNewEmailJson, unauthorizedUserEmptyToken);
        CommonMethod.checkResponseCode(response, CODE_401);
        CommonMethod.checkResponseBody(response, unauthorizedUserFieldSuccess, unauthorizedUserValueFalse);
        CommonMethod.checkResponseBody(response, unauthorizedUserBodyFieldMessage, unauthorizedUserBodyValueMessage);
    }

    @Test
    @DisplayName("Check changing password on unauthorized user")
    public void checkUnauthorizedUserChangeFieldPassword() {
        ValidatableResponse response = UserMethod.sendPatchRequestChangeUser(userToChangeEmailNewEmailJson, unauthorizedUserEmptyToken);
        CommonMethod.checkResponseCode(response, CODE_401);
        CommonMethod.checkResponseBody(response, unauthorizedUserFieldSuccess, unauthorizedUserValueFalse);
        CommonMethod.checkResponseBody(response, unauthorizedUserBodyFieldMessage, unauthorizedUserBodyValueMessage);
    }

    @Test
    @DisplayName("Check changing name on unauthorized user")
    public void checkUnauthorizedUserChangeFieldName() {
        ValidatableResponse response = UserMethod.sendPatchRequestChangeUser(userToChangeEmailNewEmailJson, unauthorizedUserEmptyToken);
        CommonMethod.checkResponseCode(response, CODE_401);
        CommonMethod.checkResponseBody(response, unauthorizedUserFieldSuccess, unauthorizedUserValueFalse);
        CommonMethod.checkResponseBody(response, unauthorizedUserBodyFieldMessage, unauthorizedUserBodyValueMessage);
    }

}
