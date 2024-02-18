package praktikum.auth;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.*;
import praktikum.auth.user.AuthUsers;
import praktikum.helper.UserData;
import praktikum.registrations.user.UsersRegistration;

import static praktikum.helper.UserData.*;

@DisplayName("Проверка аутентификации")
public class AuthenticationUserTest {
    private final AuthUsers authUsers = new AuthUsers();
    private final AssertsAuth assertsAuth = new AssertsAuth();
    private final static UserData userData = new UserData();
    private final static UsersRegistration usersRegistration = new UsersRegistration();
    private static ValidatableResponse authBaseUser;
    private Authentication auth;
    private static String userToken;

    @BeforeClass
    public static void creatBaseUser() {
        usersRegistration.userRegistration(userData.baseUser());
    }

    @Test
    @DisplayName("Аутентификация под существующим пользователем")
    @Description("Аутентификация под существующим пользователем")
    public void successfulUserAuthentication() {
        auth = Authentication.fromRegistrationUser(userData.baseUser());
        authBaseUser = authUsers.authenticationUser(auth);
        assertsAuth.successfulAuthentication(authBaseUser);
    }

    @Test
    @DisplayName("Аутентификация с некорректным логином или паролем.")
    @Description("Аутентификация с некорректным логином или паролем.")
    public void userAuthWithIncorrectLogin() {
        auth = Authentication.fromRegistrationUser(userData.randomUser());
        authBaseUser = authUsers.authenticationUser(auth);
        assertsAuth.failedAuthentication(authBaseUser);
    }

    @Test
    @DisplayName("Аутентификация с пустым логином.")
    @Description("Аутентификация с пустым логином.")
    public void userAuthWithEmptyLogin() {
        auth = Authentication.fromRegistrationUser(userWithEmptyLogin());
        authBaseUser = authUsers.authenticationUser(auth);
        assertsAuth.failedAuthentication(authBaseUser);
    }

    @Test
    @DisplayName("Аутентификация с пустым паролем.")
    @Description("Аутентификация с пустым паролем.")
    public void userAuthWithEmptyPassword() {
        auth = Authentication.fromRegistrationUser(userWithEmptyPassword());
        authBaseUser = authUsers.authenticationUser(auth);
        assertsAuth.failedAuthentication(authBaseUser);
    }

    @AfterClass
    public static void deleteUser() {
        userToken = authBaseUser.extract().path("accessToken");
        usersRegistration.deleteUser(userToken);
    }

}
