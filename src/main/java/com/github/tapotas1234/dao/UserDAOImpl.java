package com.github.tapotas1234.dao;

import com.github.tapotas1234.model.User;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 Класс представляет собой DAO-слой и реализует интерфейс UserDAO и описанные в нем методы по взаимодействию с БД
 без дополнительной бизнес-логики
 */
public class UserDAOImpl implements UserDAO {
    private final SessionFactory sessionFactory;

    /**
     * Конструктор с параметрами предназначен для тестов, где URL для БД, имя пользователя и пароль нужно получать из
     * тест-контейнера, а не конфигурационного файла
     */
    public UserDAOImpl(String dbUrl, String username, String password) {
        sessionFactory = buildSessionFactory(dbUrl, username, password);
    }

    private SessionFactory buildSessionFactory(String dbUrl, String username, String password) {
        if (sessionFactory == null) {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")
                    .applySetting("hibernate.connection.url", dbUrl)
                    .applySetting("hibernate.connection.username", username)
                    .applySetting("hibernate.connection.password", password)
                    .build();
            return new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();
        } else {
            return sessionFactory;
        }
    }

    /**
     * Создание SessionFactory с данными из конфига
     */
    public UserDAOImpl() {
        String dbUrl = System.getenv("UserServiceDBUrl");
        String dbUser = System.getenv("UserServiceLogin");
        String dbPass = System.getenv("UserServicePassword");
        sessionFactory = buildSessionFactory(dbUrl, dbUser, dbPass);
    }

    private static String getPropertyFromConfig(String key) {
        return new Configuration().configure().getProperty(key);
    }

    public void save(User user) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.persist(user);
                tx.commit();
            } catch (HibernateException e) {
                tx.rollback();
                throw e;
            }
        }
    }

    public List<User> findAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String sql = "FROM User";
            return session.createQuery(sql, User.class).list();
        }
    }

    public User findUser(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            return session.get(User.class, id);
        }
    }

    public void update(User user) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.merge(user);
                session.getTransaction().commit();
            } catch (HibernateException e) {
                tx.rollback();
                throw e;
            }
        }
    }

    public void delete(User user) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.remove(user);
                tx.commit();
            } catch (HibernateException e) {
                tx.rollback();
                throw e;
            }
        }
    }

    public void deleteAll() throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.createNativeQuery("DELETE FROM users;")
                        .executeUpdate();

                tx.commit();
            } catch (HibernateException e) {
                if (tx != null) tx.rollback();
                throw e;
            }
        }
    }
}
