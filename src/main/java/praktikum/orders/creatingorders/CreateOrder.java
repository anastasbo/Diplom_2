package praktikum.orders.creatingorders;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.orders.Orders;

import static io.restassured.RestAssured.given;
import static praktikum.Endpoints.BASE_URL;
import static praktikum.Endpoints.ORDERS;

public class CreateOrder {
    @Step("Создать заказ")
    public ValidatableResponse creatingOrder(Orders orders) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(orders)
                .when()
                .post(ORDERS)
                .then();
    }
}
