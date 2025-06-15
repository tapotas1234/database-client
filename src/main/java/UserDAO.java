import model.User;
import java.util.List;

public interface UserDAO {
    void addUser(User user);
    List<User> getAllUsers();
    User getUser(Integer id);
    User updateUserAge(Integer id, Integer newAge);
    User updateUserEmail(Integer id, String newEmail);
    User deleteUser(Integer id);

}
