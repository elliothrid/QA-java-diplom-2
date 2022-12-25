package site.nomoreparties.stellarburgers.test.order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import site.nomoreparties.stellarburgers.User;
import site.nomoreparties.stellarburgers.model.OrderRequest;
import site.nomoreparties.stellarburgers.object_api.CommonMethod;
import site.nomoreparties.stellarburgers.object_api.IngredientMethod;
import site.nomoreparties.stellarburgers.object_api.OrderMethod;
import site.nomoreparties.stellarburgers.object_api.UserMethod;

import java.util.Arrays;
import java.util.List;

import static site.nomoreparties.stellarburgers.constant.BodyFieldValue.*;
import static site.nomoreparties.stellarburgers.constant.ServiceName.*;
import static site.nomoreparties.stellarburgers.constant.StatusCode.*;

public class OrderGetTest {

    //Create order
    private static final List<String> ingredientNames = Arrays.asList("Краторная булка N-200i", "Хрустящие минеральные кольца", "Соус традиционный галактический");
    private static OrderRequest orderRequest;

    //Create order with authorization
    static User user;
    private static final String userEmail  = "nikolay7898752367@stellarburger.site";
    private static final String userPassword = "password";
    private static final String userName = "nikolay";

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
    }

    @Test
    @DisplayName("Check get order with authorization")
    public void checkGetOrderWithAuthorization() {
        ValidatableResponse responseCreateOrder = OrderMethod.sendPostRequestCreateOrderWithAuthorization(orderRequest, user.getAccessToken());
        CommonMethod.checkResponseCode(responseCreateOrder, CODE_200);
        String createdOrderId = responseCreateOrder.extract().path("order._id").toString();
        ValidatableResponse responseGetOrder = OrderMethod.sendGetRequestCreateOrderWithAuthorization(orderRequest, user.getAccessToken());
        List<String> getOrderIds = responseGetOrder.extract().path("orders._id");
        Assert.assertEquals("Order ID is incorrect", createdOrderId, getOrderIds.get(0));
    }

    @Test
    @DisplayName("Check get order without authorization")
    public void checkGetOrderWithoutAuthorization() {
        ValidatableResponse response = OrderMethod.sendGetRequestCreateOrderWithoutAuthorization(orderRequest);
        CommonMethod.checkResponseCode(response, CODE_401);
        CommonMethod.checkResponseBody(response, FIELD_SUCCESS, false);
        CommonMethod.checkResponseBody(response, FIELD_MESSAGE, VALUE_YOU_SHOULD_BE_AUTHORISED);
    }

}
