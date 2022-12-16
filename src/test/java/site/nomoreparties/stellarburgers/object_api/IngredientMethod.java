package site.nomoreparties.stellarburgers.object_api;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.Ingredient;
import site.nomoreparties.stellarburgers.model.IngredientResponse;

import java.util.ArrayList;
import java.util.List;

public class IngredientMethod {

    public static final String INGREDIENT_API_GET = "/api/ingredients";

    private static final Filter requestFilter = new RequestLoggingFilter();
    private static final Filter responseFiler = new ResponseLoggingFilter();

    @Step("Send GET ingredient")
    public static ValidatableResponse sendGetRequestGetIngredient() {
        return RestAssured.with().filters(requestFilter, responseFiler).when().get(INGREDIENT_API_GET).then();
    }

    @Step("Create ingredient's id list")
    public static List<String> createIngredientsIdsList(List<String> ingredientsNames) {
        ValidatableResponse ingredientResponse = IngredientMethod.sendGetRequestGetIngredient();
        List<Ingredient> ingredientList = ingredientResponse.extract().as(IngredientResponse.class).getData();

        List<String> ingredientsIdsList = new ArrayList<>();
        for (String ingredientName : ingredientsNames) {
            for (Ingredient ingredient : ingredientList) {
                if (ingredient.getName().equals(ingredientName)) {
                    ingredientsIdsList.add(ingredient.get_id());
                    break;
                }
            }
        }
        return ingredientsIdsList;
    }

}
