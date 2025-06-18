import model.User;
import java.util.List;

/**
 Интерфейс определяет набор методов, обеспечивающих все CRUD-операции над сущностью User
*/
public interface UserDAO {
    /**
     * @param user - пользователь, который будет добавлен в БД
     */
    void addUser(User user);

    /**
     * @return List<User> - список всех пользователей в БД
     */
    List<User> getAllUsers();

    /**
     *
     * @param id - id пользователя
     * @return User = пользователь с соответствующим id
     */
    User getUser(Integer id);

    /**
     *
     * @param id - id пользователя
     * @param newAge - новое значение возраста, которое запишется в БД
     * @return User = пользователь с обновленным возрастом
     */
    User updateUserAge(Integer id, Integer newAge);

    /**
     *
     * @param id - id пользователя
     * @param newEmail - новое значение email, которое запишется в БД
     * @return User = пользователь с обновленной почтой
     */
    User updateUserEmail(Integer id, String newEmail);

    /**
     *
     * @param id - id пользователя
     * @return User = удаленный пользователь
     */
    User deleteUser(Integer id);

}
