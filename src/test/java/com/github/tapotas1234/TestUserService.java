package com.github.tapotas1234;

import com.github.tapotas1234.dao.UserDAO;
import com.github.tapotas1234.model.User;
import jakarta.persistence.PersistenceException;
import org.hibernate.HibernateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestUserService {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserService userService;

    // Тесы для addUser(User user)
    @Test
    void addUser_Success() {
        User mockUser = new User("John", 22);
        mockUser.setId(1);

        userService.addUser(mockUser);

        verify(userDAO, times(1)).save(mockUser);
    }

    @Test
    void addUser_DoesNotThrowsHibernateException() {
        User mockUser = new User("John", 22);
        mockUser.setId(1);
        doThrow(new HibernateException("DB error"))
                .when(userDAO)
                .save(mockUser);

        assertDoesNotThrow(() ->
                userService.addUser(mockUser)
        );
        verify(userDAO).save(mockUser);
    }

    // Тесты для getUserById(Integer id)
    @Test
    void getUserById_Success() {
        User mockUser = new User("John", 32);
        mockUser.setId(1);
        when(userDAO.findUser(1)).thenReturn(mockUser);

        User result = userService.getUserById(1);

        verify(userDAO, times(1)).findUser(1);
    }

    @Test
    void getUserById_NullWhenIdIsNull() {
        assertNull(userService.getUserById(null));
    }

    @Test
    void getUserById_NullIfNoUserById() {
        Integer id = 10;
        when(userDAO.findUser(id)).thenReturn(null);

        User res = userService.getUserById(id);

        assertNull(res);
    }

    @Test
    void getUserById_NullIfThrowsPersistenceException() {
        when(userDAO.findUser(10)).thenThrow(PersistenceException.class);

        User res = userService.getUserById(10);

        assertNull(res);
    }

    // Тесты для getAllUsers()
    @Test
    void getAllUsers_Success() {
        userService.getAllUsers();

        verify(userDAO, times(1)).findAllUsers();
    }

    @Test
    void getAllUsers_NullWhenThrowsException() {
        when(userDAO.findAllUsers()).thenThrow(PersistenceException.class);

        assertNull(userService.getAllUsers());
    }

    // Тесты для updateUserAge(Integer id, Integer newAge)
    @Test
    void updateUserAge_Success() {
        User mockUser = new User("John", 22);
        mockUser.setId(1);
        when(userService.getUserById(1)).thenReturn(mockUser);

        userService.updateUserAge(1, 33);

        verify(userDAO, times(1)).update(mockUser);
    }

    @Test
    void updateUserAge_NullIfThrowsError() {
        User mockUser = new User("John", 22);
        mockUser.setId(1);
        when(userService.getUserById(1)).thenReturn(mockUser);
        doThrow(HibernateException.class).when(userDAO).update(mockUser);

        assertDoesNotThrow(() ->
                userService.updateUserAge(1, 23)
        );
        verify(userDAO).update(mockUser);
    }

    // Тесты для updateUserEmail(Integer id, String email)
    @Test
    void updateUserEmail_Success() {
        User mockUser = new User("John", 22);
        mockUser.setId(1);
        when(userService.getUserById(1)).thenReturn(mockUser);

        userService.updateUserEmail(1, "asdasd@mail.ru");

        verify(userDAO, times(1)).update(mockUser);
    }

    @Test
    void updateUserEmail_NullIfThrowsException() {
        User mockUser = new User("John", 22);
        mockUser.setId(1);
        when(userService.getUserById(1)).thenReturn(mockUser);
        doThrow(HibernateException.class).when(userDAO).update(mockUser);

        assertDoesNotThrow(() ->
                userService.updateUserEmail(1, "ddd@mail.ru")
        );
        verify(userDAO).update(mockUser);
    }

    // Тесты для deleteUser(Integer id)
    @Test
    void deleteUser_Success() {
        User mockUser = new User("John", 22);
        mockUser.setId(1);
        when(userService.getUserById(1)).thenReturn(mockUser);

        userService.deleteUser(1);

        verify(userDAO, times(1)).delete(mockUser);
    }

    @Test
    void deleteUser_NullWhenThrowsException() {
        User mockUser = new User("John", 22);
        mockUser.setId(1);
        when(userService.getUserById(1)).thenReturn(mockUser);
        doThrow(HibernateException.class).when(userDAO).delete(mockUser);

        assertDoesNotThrow(() ->
                userService.deleteUser(1)
        );
        verify(userDAO).delete(mockUser);
    }
}
