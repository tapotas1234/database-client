package com.github.tapotas1234.dao;

import com.github.tapotas1234.model.User;
import org.hibernate.HibernateException;

import java.util.List;

/**
 Интерфейс определяет набор методов, обеспечивающих все CRUD-операции над сущностью User
*/
public interface UserDAO {
    /**
     * @param user - пользователь, который будет добавлен в БД
     */
    void save(User user) throws HibernateException;

    /**
     * @return List<User> - список всех пользователей в БД
     */
    List<User> findAllUsers();

    /**
     *
     * @param id - id пользователя
     * @return User = пользователь с соответствующим id
     */
    User findUser(Integer id);

    /**
     * @param user - пользователь со всеми данными, которые будут перезаписаны в БД
     */
    void update(User user) throws HibernateException;

    /**
     * @param user - пользователь, которого хотим удалить
     */
    void delete(User user) throws HibernateException;

    /**
     * удаляет всех пользователей из БД
     */
    void deleteAll() throws HibernateException;
}
