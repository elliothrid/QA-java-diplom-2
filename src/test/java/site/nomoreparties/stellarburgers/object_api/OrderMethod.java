package site.nomoreparties.stellarburgers.object_api;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.model.OrderRequest;

import static site.nomoreparties.stellarburgers.constant.Header.*;

public class OrderMethod {

    public static final String ORDER_API_CREATE = "/api/orders";
    public static final String ORDER_API_GET = "/api/orders";
    private static final Filter requestFilter = new RequestLoggingFilter();
    private static final Filter responseFiler = new ResponseLoggingFilter();

    @Step("Send POST create order without authorization")
    public static ValidatableResponse sendPostRequestCreateOrderWithoutAuthorization(OrderRequest ingredientsIds) {
        return RestAssured.with().filters(requestFilter, responseFiler).header(CONTENT_TYPE, APPLICATION_JSON).and()
                .body(ingredientsIds).when().post(ORDER_API_CREATE).then();
    }

    @Step("Send POST create order with authorization")
    public static ValidatableResponse sendPostRequestCreateOrderWithAuthorization(OrderRequest ingredientsIds, String accessToken) {
        return RestAssured.with().filters(requestFilter, responseFiler).header(CONTENT_TYPE, APPLICATION_JSON).and()
                .header(AUTORIZATION, accessToken).body(ingredientsIds).when().post(ORDER_API_CREATE).then();
    }

    @Step("Send GET order with authorization")
    public static ValidatableResponse sendGetRequestCreateOrderWithAuthorization(OrderRequest ingredientsIds, String accessToken) {
        return RestAssured.with().filters(requestFilter, responseFiler).header(CONTENT_TYPE, APPLICATION_JSON).and()
                .header(AUTORIZATION, accessToken).body(ingredientsIds).when().get(ORDER_API_GET).then();
    }

    @Step("Send GET order without authorization")
    public static ValidatableResponse sendGetRequestCreateOrderWithoutAuthorization(OrderRequest ingredientsIds) {
        return RestAssured.with().filters(requestFilter, responseFiler).header(CONTENT_TYPE, APPLICATION_JSON).and()
                .body(ingredientsIds).when().get(ORDER_API_GET).then();
    }

}
