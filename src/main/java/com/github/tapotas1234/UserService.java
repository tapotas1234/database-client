package com.github.tapotas1234;

import com.github.tapotas1234.dao.UserDAO;
import com.github.tapotas1234.model.User;
import jakarta.persistence.PersistenceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;

import java.util.List;

/**
 * Класс представляет собой service-слой и используется для взаимодействия с БД, с бизнес-логикой. Входной точкой для
 * взаимодействия с БД стоит использовать его
 */
public class UserService {
        private final UserDAO userDAO;
        private static final Logger logger = LogManager.getLogger(UserService.class);

        UserService(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        public void addUser(User user) {
            try {
                userDAO.save(user);
            } catch (HibernateException e) {
                logger.error("Пользователь не добавлен из-за ошибки: {}", e.getMessage());
            }
        }

        public User getUserById(Integer id) {
            try {
                User user = userDAO.findUser(id);
                if (user == null) {
                    logger.error("Пользователь с таким id не найден");

                    return null;
                } else {
                    return user;
                }
            } catch (PersistenceException e) {
                logger.error("Ошибка при попытке найти пользователя с id {}", id, e);

                return null;
            }
        }

        public List<User> getAllUsers() {
            try {
                return userDAO.findAllUsers();
            } catch (PersistenceException e) {
                logger.error("Ошибка при попытке получения пользователей", e);

                return null;
            }
        }

        public void updateUserAge(Integer id, Integer newAge) {
            User user = getUserById(id);
            user.setAge(newAge);
            try {
                userDAO.update(user);
            } catch (HibernateException e) {
                logger.error("Возраст пользователя не обновлен из-за ошибки: {}", e.getMessage());
            }
        }

        public void updateUserEmail(Integer id, String newEmail) {
            User user = getUserById(id);
            user.setEmail(newEmail);
            try {
                userDAO.update(user);
            } catch (HibernateException e) {
                logger.error("Email пользователя не обновлен из-за ошибки: {}", e.getMessage());
            }
        }

        public void deleteUser(Integer id) {
            User user = getUserById(id);
            try {
                userDAO.delete(user);
            } catch (HibernateException e) {
                logger.error("Пользователь не удален из-за ошибки: {}", e.getMessage());
            }
        }
}
