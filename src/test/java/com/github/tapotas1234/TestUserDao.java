package com.github.tapotas1234;

import com.github.tapotas1234.dao.UserDAO;
import com.github.tapotas1234.dao.UserDAOImpl;
import com.github.tapotas1234.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Testcontainers
public class TestUserDao {
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("userService")
            .withUsername(System.getenv("UserServiceLogin"))
            .withPassword(System.getenv("UserServicePassword"));

    private UserDAO userDAO;
    private static final Logger logger = LogManager.getLogger(TestUserDao.class);

    @BeforeEach
    void setup() {
        String jdbcUrl = postgres.getJdbcUrl();
        String username = postgres.getUsername();
        String password = postgres.getPassword();

        userDAO = new UserDAOImpl(jdbcUrl, username, password);
    }

    @Test
    @DisplayName("тестирует добавление пользователя только с именем и возрастом, без email")
    public void saveAndFindUserWithNameAndAge() {
        User user = new User("Peter", 22);

        userDAO.save(user);
        User foundUser = userDAO.findUser(user.getId());

        Assertions.assertEquals("Peter", foundUser.getName());
        Assertions.assertEquals(22, foundUser.getAge());
        Assertions.assertNull(foundUser.getEmail());
    }

    @Test
    @DisplayName("тестирует добавление пользователя только с именем, возрастом и email")
    public void saveAndFindUserWithNameAgeAndEmail() {
        User user = new User("Peter", 22, "custom@mail.ru");

        userDAO.save(user);
        User foundUser = userDAO.findUser(user.getId());

        Assertions.assertEquals("Peter", foundUser.getName());
        Assertions.assertEquals(22, foundUser.getAge());
        Assertions.assertEquals("custom@mail.ru", foundUser.getEmail());
    }

    @Test
    public void findAllUsers() {
        User user1 = new User("Peter", 22, "custom@mail.ru");
        User user2 = new User("John", 33, "john@mail.ru");
        userDAO.save(user1);
        userDAO.save(user2);

        List<User> users = userDAO.findAllUsers();

        Assertions.assertEquals(2, users.size());
        // проверки для user1
        User peter = users.get(0);
        Assertions.assertEquals("Peter", peter.getName());
        Assertions.assertEquals(22, peter.getAge());
        Assertions.assertEquals("custom@mail.ru", peter.getEmail());
        // проверки для user2
        User john = users.get(1);
        Assertions.assertEquals("John", john.getName());
        Assertions.assertEquals(33, john.getAge());
        Assertions.assertEquals("john@mail.ru", john.getEmail());
    }

    @Test
    public void updateUser() {
        User user1 = new User("Peter", 22, "custom@mail.ru");
        userDAO.save(user1);
        Integer id = user1.getId();

        user1.setEmail("test@mail.ru");
        user1.setAge(10);
        userDAO.update(user1);
        User foundUser = userDAO.findUser(id);

        Assertions.assertEquals("Peter", foundUser.getName());
        Assertions.assertEquals(10, foundUser.getAge());
        Assertions.assertEquals("test@mail.ru", foundUser.getEmail());
    }

    @Test
    public void deleteUser() {
        User user1 = new User("Peter", 22, "custom@mail.ru");
        userDAO.save(user1);
        Integer id = user1.getId();

        userDAO.delete(user1);
        User foundUser = userDAO.findUser(id);

        Assertions.assertNull(foundUser);
    }

    @Test
    public void deleteAllUsersForTest() {
        User user1 = new User("Peter", 22, "custom@mail.ru");
        User user2 = new User("John", 33, "john@mail.ru");
        userDAO.save(user1);
        userDAO.save(user2);

        userDAO.deleteAll();
        List<User> users = userDAO.findAllUsers();

        Assertions.assertTrue(users.isEmpty());
    }

    @AfterEach
    public void deleteAllUsers() {
        try {
            userDAO.deleteAll();
        } catch (Exception e) {
            logger.error("Не удалось удалить всех пользователей после теста. Текст ошибки: {}", e.getMessage());
        }
    }

}
