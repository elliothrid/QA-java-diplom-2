package site.nomoreparties.stellarburgers.test.order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.BeforeClass;
import org.junit.Test;
import site.nomoreparties.stellarburgers.User;
import site.nomoreparties.stellarburgers.model.OrderRequest;
import site.nomoreparties.stellarburgers.object_api.CommonMethod;
import site.nomoreparties.stellarburgers.object_api.OrderMethod;
import site.nomoreparties.stellarburgers.object_api.UserMethod;
import site.nomoreparties.stellarburgers.object_api.IngredientMethod;

import java.util.Arrays;
import java.util.List;

import static site.nomoreparties.stellarburgers.constant.BodyFieldValue.*;
import static site.nomoreparties.stellarburgers.constant.ServiceName.*;
import static site.nomoreparties.stellarburgers.constant.StatusCode.*;

public class OrderCreateTest {

    //Create order
    private static final List<String> ingredientNames = Arrays.asList("Краторная булка N-200i", "Хрустящие минеральные кольца", "Соус традиционный галактический");
    private static OrderRequest orderRequest;
    private static final String orderBodyFieldOrderNameValue = "Минеральный краторный традиционный-галактический бургер";

    //Create order with authorization and with ingredients
    static User user;
    private static final String userEmail  = "pavel7898752367@stellarburger.site";
    private static final String userPassword = "password";
    private static final String userName = "pavel";

    //Create order without ingredients
    private static final List<String> emptyIngredientNames = List.of();
    private static OrderRequest orderRequestWithoutIngredients;

    @BeforeClass
    public static void suiteSetup() {
        RestAssured.baseURI = BASE_URI;
        List<String> ingredientsIds = IngredientMethod.createIngredientsIdsList(ingredientNames);
        orderRequest = new OrderRequest(ingredientsIds);
        User resetUser = new User(userEmail, userPassword, null);
        UserMethod.resetUser(resetUser);
        user = new User(userEmail, userPassword, userName);
        ValidatableResponse createdUserResponse = UserMethod.sendPostRequestCreateUser(user);
        String token = UserMethod.getAccessToken(createdUserResponse);
        user.setAccessToken(token);
        orderRequestWithoutIngredients = new OrderRequest(emptyIngredientNames);
    }

    @Test
    @DisplayName("Check create order without authorization and with ingredients")
    public void checkCreateOrderWithoutAuthorizationWithIngredients() {
        ValidatableResponse response = OrderMethod.sendPostRequestCreateOrderWithoutAuthorization(orderRequest);
        CommonMethod.checkResponseCode(response, CODE_200);
        CommonMethod.checkResponseBodyNotNullField(response, FIELD_ORDER_NUMBER);
        CommonMethod.checkResponseBody(response, FIELD_NAME, orderBodyFieldOrderNameValue);
    }

    @Test
    @DisplayName("Check create order without authorization and without ingredients")
    public void checkCreateOrderWithoutAuthorizationWithoutIngredients() {
        ValidatableResponse response = OrderMethod.sendPostRequestCreateOrderWithoutAuthorization(orderRequestWithoutIngredients);
        CommonMethod.checkResponseCode(response, CODE_400);
        CommonMethod.checkResponseBody(response, FIELD_SUCCESS, false);
        CommonMethod.checkResponseBody(response, FIELD_MESSAGE, VALUE_INGREDIENT_IDS_MUST_BE_PROVIDED);
    }

    @Test
    @DisplayName("Check create order with authorization and with ingredients")
    public void checkCreateOrderWithAuthorizationWithIngredients() {
        ValidatableResponse response = OrderMethod.sendPostRequestCreateOrderWithAuthorization(orderRequest, user.getAccessToken());
        CommonMethod.checkResponseCode(response, CODE_200);
        CommonMethod.checkResponseBodyNotNullField(response, FIELD_ORDER__ID);
        CommonMethod.checkResponseBody(response, FIELD_ORDER_OWNER_EMAIL, userEmail);
    }

    @Test
    @DisplayName("Check create order with authorization and without ingredients")
    public void checkCreateOrderWithAuthorizationWithoutIngredients() {
        ValidatableResponse response = OrderMethod.sendPostRequestCreateOrderWithAuthorization(orderRequestWithoutIngredients, user.getAccessToken());
        CommonMethod.checkResponseCode(response, CODE_400);
        CommonMethod.checkResponseBody(response, FIELD_SUCCESS, false);
        CommonMethod.checkResponseBody(response, FIELD_MESSAGE, VALUE_INGREDIENT_IDS_MUST_BE_PROVIDED);
    }

    @Test
    @DisplayName("Check create order with incorrect ingredients ids")
    public void checkCreateOrderWithIncorrectIngredientsIds() {
        ValidatableResponse response = OrderMethod.sendPostRequestCreateOrderWithAuthorization(orderRequestWithoutIngredients, user.getAccessToken());
        CommonMethod.checkResponseCode(response, CODE_400);
        CommonMethod.checkResponseBody(response, FIELD_SUCCESS, false);
        CommonMethod.checkResponseBody(response, FIELD_MESSAGE, VALUE_INGREDIENT_IDS_MUST_BE_PROVIDED);
    }

}
