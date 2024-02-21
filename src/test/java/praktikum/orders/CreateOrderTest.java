package praktikum.orders;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.auth.Authentication;
import praktikum.auth.user.AuthUsers;
import praktikum.helper.OrdersData;
import praktikum.helper.UserData;
import praktikum.orders.creatingorders.CreateOrder;
import praktikum.registrations.user.UsersRegistration;

@DisplayName("Проверка создания заказов")
public class CreateOrderTest {
    private final CreateOrder createOrder = new CreateOrder();
    private final AssertsOrders assertsOrders = new AssertsOrders();
    private final AuthUsers authUsers = new AuthUsers();
    private final UsersRegistration usersRegistration = new UsersRegistration();
    private ValidatableResponse creatingOrderUser;
    private ValidatableResponse authRandomUser;
    private Authentication authUserData;
    private String userToken;
    private String randomUserEmail;

    @Before
    public void creatingTestUser() {
        ValidatableResponse creatingUser = usersRegistration.userRegistration(UserData.randomUser());
        randomUserEmail = creatingUser.extract().path("user.email");
    }

    @Test
    @DisplayName("Создание заказа c авторизацией")
    @Description("Создание заказа c авторизацией")
    public void creatingOrderWithAuthorization() {
        authUserData = new Authentication(randomUserEmail, "12345678");
        authRandomUser = authUsers.authenticationUser(authUserData);
        userToken = authRandomUser.extract().path("accessToken");
        creatingOrderUser = createOrder.creatingOrder(OrdersData.orderBunFluorescent());
        assertsOrders.creatingOrderWithAuthorized(creatingOrderUser);
    }

    @Test
    @DisplayName("Создание заказа без авторизацией")
    @Description("Создание заказа без авторизацией")
    public void creatingOrderWithoutAuthorization() {
        creatingOrderUser = createOrder.creatingOrder(OrdersData.orderBunCrater());
        assertsOrders.creatingOrderWithoutAuthorized(creatingOrderUser);
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Создание заказа с ингредиентами")
    public void creatingOrderWithIngredients() {
        creatingOrderUser = createOrder.creatingOrder(OrdersData.orderBunWithIngredientsImmortalBun());
        assertsOrders.creatingOrderWithIngredientsImmortalBun(creatingOrderUser);
    }
    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Создание заказа с неверным хешем ингредиентов")
    public void creatingOrderWithIncorrectHash() {
        creatingOrderUser = createOrder.creatingOrder(OrdersData.incorrectOrderBun());
        assertsOrders.creatingOrderWithIncorrectHash(creatingOrderUser);
    }
    @After
    public void deleteUser() {
        authUserData = new Authentication(randomUserEmail, "12345678");
        authRandomUser = authUsers.authenticationUser(authUserData);
        userToken = authRandomUser.extract().path("accessToken");
        usersRegistration.deleteUser(userToken);
    }
}
