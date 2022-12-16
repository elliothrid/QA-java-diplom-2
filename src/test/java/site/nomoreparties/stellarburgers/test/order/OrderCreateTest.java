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

import static site.nomoreparties.stellarburgers.constant.ServiceName.*;
import static site.nomoreparties.stellarburgers.constant.StatusCode.*;

public class OrderCreateTest {

    //Create order
    private static final List<String> ingredientNames = Arrays.asList("Краторная булка N-200i", "Хрустящие минеральные кольца", "Соус традиционный галактический");
    private static OrderRequest orderRequest;
    private static final String orderBodyFieldOrderName = "name";
    private static final String orderBodyFieldOrderNameValue = "Минеральный краторный традиционный-галактический бургер";
    private static final String orderBodyFieldOrderNumber = "order.number";

    //Create order with authorization and with ingredients
    static User user;
    private static final String userEmail  = "pavel7898752367@stellarburger.site";
    private static final String userPassword = "password";
    private static final String userName = "pavel";
    private static final String userJson = "{\"email\": \"pavel7898752367@stellarburger.site\", \"password\": \"password\"}";
    private static final String authorizedOrderBodyFieldOwnerName = "order.owner.email";
    private static final String authorizedOrderBodyFieldOwnerNameValue = userEmail;
    private static final String authorizedOrderBodyFieldId = "order._id";

    //Create order without ingredients
    private static final List<String> emptyIngredientNames = List.of();
    private static OrderRequest orderRequestWithoutIngredients;
    private static final String emptyOrderBodyFieldSuccess = "success";
    private static final Boolean emptyOrderBodyFieldSuccessValueTrue = false;
    private static final String emptyOrderBodyFieldMessage = "message";
    private static final String emptyOrderBodyFieldMessageValue = "Ingredient ids must be provided";

    //Create order without incorrect ingredients ids
    private static final List<String> incorrectIngredientsIds = Arrays.asList("aaaaaaaaaa", "bbbbbbbbbbb");
    private static final String incorrectOrderBodyFieldSuccess = "success";
    private static final Boolean incorrectOrderBodyFieldSuccessValueTrue = false;
    private static final String incorrectOrderBodyFieldMessage = "message";
    private static final String incorrectOrderBodyFieldMessageValue = "Ingredient ids must be provided";

    @BeforeClass
    public static void suiteSetup() {
        RestAssured.baseURI = BASE_URI;
        List<String> ingredientsIds = IngredientMethod.createIngredientsIdsList(ingredientNames);
        orderRequest = new OrderRequest(ingredientsIds);
        UserMethod.resetUser(userJson);
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
        CommonMethod.checkResponseBodyNotNullField(response, orderBodyFieldOrderNumber);
        CommonMethod.checkResponseBody(response, orderBodyFieldOrderName, orderBodyFieldOrderNameValue);
        //TODO: проверка что поля с почтой заказчика нет
    }

    @Test
    @DisplayName("Check create order without authorization and without ingredients")
    public void checkCreateOrderWithoutAuthorizationWithoutIngredients() {
        ValidatableResponse response = OrderMethod.sendPostRequestCreateOrderWithoutAuthorization(orderRequestWithoutIngredients);
        CommonMethod.checkResponseCode(response, CODE_400);
        CommonMethod.checkResponseBody(response, emptyOrderBodyFieldSuccess, emptyOrderBodyFieldSuccessValueTrue);
        CommonMethod.checkResponseBody(response, emptyOrderBodyFieldMessage, emptyOrderBodyFieldMessageValue);
    }

    @Test
    @DisplayName("Check create order with authorization and with ingredients")
    public void checkCreateOrderWithAuthorizationWithIngredients() {
        ValidatableResponse response = OrderMethod.sendPostRequestCreateOrderWithAuthorization(orderRequest, user.getAccessToken());
        CommonMethod.checkResponseCode(response, CODE_200);
        CommonMethod.checkResponseBodyNotNullField(response, authorizedOrderBodyFieldId);
        CommonMethod.checkResponseBody(response, authorizedOrderBodyFieldOwnerName, authorizedOrderBodyFieldOwnerNameValue);
    }

    @Test
    @DisplayName("Check create order with authorization and without ingredients")
    public void checkCreateOrderWithAuthorizationWithoutIngredients() {
        ValidatableResponse response = OrderMethod.sendPostRequestCreateOrderWithAuthorization(orderRequestWithoutIngredients, user.getAccessToken());
        CommonMethod.checkResponseCode(response, CODE_400);
        CommonMethod.checkResponseBody(response, emptyOrderBodyFieldSuccess, emptyOrderBodyFieldSuccessValueTrue);
        CommonMethod.checkResponseBody(response, emptyOrderBodyFieldMessage, emptyOrderBodyFieldMessageValue);
    }

    @Test
    @DisplayName("Check create order with incorrect ingredients ids")
    public void checkCreateOrderWithIncorrectIngredientsIds() {
        OrderRequest incorrectOrder = new OrderRequest(incorrectIngredientsIds);
        ValidatableResponse response = OrderMethod.sendPostRequestCreateOrderWithAuthorization(orderRequestWithoutIngredients, user.getAccessToken());
        CommonMethod.checkResponseCode(response, CODE_400);
        CommonMethod.checkResponseBody(response, incorrectOrderBodyFieldSuccess, incorrectOrderBodyFieldSuccessValueTrue);
        CommonMethod.checkResponseBody(response, incorrectOrderBodyFieldMessage, incorrectOrderBodyFieldMessageValue);
    }

}
