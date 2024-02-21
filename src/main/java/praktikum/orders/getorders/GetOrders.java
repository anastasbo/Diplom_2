package praktikum.orders.getorders;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static praktikum.Endpoints.*;

public class GetOrders {

    @Step("Получение заказов конкретного пользователя")
    public ValidatableResponse GetOrdersForUserWithAuthorization(String token) {
        return given()
                .header("Authorization", token)
                .baseUri(BASE_URL)
                .when()
                .get(ORDERS)
                .then();
    }
}
