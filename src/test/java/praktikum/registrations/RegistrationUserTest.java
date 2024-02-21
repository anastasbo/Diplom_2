package praktikum.registrations;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import praktikum.auth.Authentication;
import praktikum.auth.user.AuthUsers;
import praktikum.helper.UserData;
import praktikum.registrations.user.UsersRegistration;

@DisplayName("Проверка создания пользователя")
public class RegistrationUserTest {
    private final UsersRegistration usersRegistration = new UsersRegistration();
    private final AssertsRegistrations assertsRegistrations = new AssertsRegistrations();
    private final AuthUsers authUsers = new AuthUsers();
    private ValidatableResponse creatingUser;

    @Test
    @DisplayName("Регистрация уникального пользователя")
    @Description("Создать уникального пользователя")
    public void creatingUniqueUser() {
        creatingUser = usersRegistration.userRegistration(UserData.randomUser());
        assertsRegistrations.successfulCreation(creatingUser);

    }

    @Test
    @DisplayName("Повторная регистрация пользователя")
    @Description("Создать пользователя, который уже зарегистрирован")
    public void createUserAlreadyExists() {
        creatingUser = usersRegistration.userRegistration(UserData.baseUser());
        ValidatableResponse creatingBaseUser = usersRegistration.userRegistration(UserData.baseUser());
        assertsRegistrations.creatingExistingAccount(creatingBaseUser);
    }

    @Test
    @DisplayName("Регистрация пользователя с пустым email")
    @Description("создать пользователя и не заполнить одно из обязательных полей.")
    public void createUserEmptyEmail() {
        creatingUser = usersRegistration.userRegistration(UserData.userWithEmptyLogin());
        assertsRegistrations.failedCreation(creatingUser);
    }

    @Test
    @DisplayName("Регистрация пользователя с пустым password")
    @Description("создать пользователя и не заполнить одно из обязательных полей.")
    public void createUserEmptyPassword() {
        creatingUser = usersRegistration.userRegistration(UserData.userWithEmptyPassword());
        assertsRegistrations.failedCreation(creatingUser);
    }

    @Test
    @DisplayName("Регистрация пользователя с пустым name")
    @Description("создать пользователя и не заполнить одно из обязательных полей.")
    public void createUserEmptyName() {
        creatingUser = usersRegistration.userRegistration(UserData.userWithEmptyName());
        assertsRegistrations.failedCreation(creatingUser);
    }

    @After
    public void deleteUser() {
        if (creatingUser.extract().path("user.email") != null) {
            String randomUserEmail = creatingUser.extract().path("user.email");
            Authentication authUserData = new Authentication(randomUserEmail, "12345678");
            ValidatableResponse authRandomUser = authUsers.authenticationUser(authUserData);
            String userToken = authRandomUser.extract().path("accessToken");
            usersRegistration.deleteUser(userToken);
        }
    }
}
