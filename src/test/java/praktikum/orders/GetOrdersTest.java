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
import praktikum.orders.getorders.GetOrders;
import praktikum.registrations.user.UsersRegistration;

@DisplayName("Проверка получения заказов пользователя")
public class GetOrdersTest {
    private final GetOrders getOrders = new GetOrders();
    private final AuthUsers authUsers = new AuthUsers();
    private final CreateOrder createOrder = new CreateOrder();
    private final AssertsOrders assertsOrders = new AssertsOrders();
    private final UsersRegistration usersRegistration = new UsersRegistration();
    private ValidatableResponse authRandomUser;
    private ValidatableResponse getOrdersForUser;
    private Authentication authUserData;
    private String userToken;
    private String randomUserEmail;

    @Before
    public void creatingTestUser() {
        ValidatableResponse creatingUser = usersRegistration.userRegistration(UserData.randomUser());
        randomUserEmail = creatingUser.extract().path("user.email");
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    @Description("Получение заказов авторизованного пользователя")
    public void creatingOrderWithAuthorization() {
        authUserData = new Authentication(randomUserEmail, "12345678");
        authRandomUser = authUsers.authenticationUser(authUserData);
        userToken = authRandomUser.extract().path("accessToken");
        createOrder.creatingOrder(OrdersData.orderBunWithIngredientsImmortalBun());
        getOrdersForUser = getOrders.GetOrdersForUserWithAuthorization(userToken);
        assertsOrders.getOrderWithAuthorized(getOrdersForUser);

    }
    @Test
    @DisplayName("Получение заказов не авторизованного пользователя")
    @Description("Получение заказов не авторизованного пользователя")
    public void creatingOrderWithoutAuthorization() {
        authUserData = new Authentication(randomUserEmail, "12345678");
        authRandomUser = authUsers.authenticationUser(authUserData);
        userToken = authRandomUser.extract().path("accessToken");
        createOrder.creatingOrder(OrdersData.orderBunWithIngredientsImmortalBun());
        getOrdersForUser = getOrders.GetOrdersForUserWithAuthorization("");
        assertsOrders.getOrderWithoutAuthorized(getOrdersForUser);
    }

    @After
    public void deleteUser() {
        authUserData = new Authentication(randomUserEmail, "12345678");
        authRandomUser = authUsers.authenticationUser(authUserData);
        userToken = authRandomUser.extract().path("accessToken");
        usersRegistration.deleteUser(userToken);
    }
}
