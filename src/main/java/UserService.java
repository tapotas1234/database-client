import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import model.User;

import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

public class UserService implements UserDAO{
    private static final SessionFactory sessionFactory;
    private static final Logger logger = LogManager.getLogger(UserService.class);

    static {
        try {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure()
                    .build();
            sessionFactory = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (sessionFactory != null && !sessionFactory.isClosed()) {
                    System.out.println("[Shutdown hook] Закрываем sessionFactory");
                    sessionFactory.close();
                }
            }));
        } catch (Exception e) {
            System.err.println("Ошибка инициализации SessionFactory");
            throw new ExceptionInInitializerError(e);
        }
    }

    public void addUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            try {
                session.persist(user);
                session.getTransaction().commit();
            } catch (Exception e) {
                logger.error("Пользователь не добавлен из-за ошибки: {}", e.getMessage());
                rollbackIfActive(session);
            }
        } catch (Exception sessionE) {
            logger.error("Ошибка при открытии сессии: {}", sessionE.getMessage());
        }
    }

    private static void rollbackIfActive(Session session) {
        Transaction tx = session.getTransaction();
        if (tx.isActive()) {
            try {
                tx.rollback();
            } catch (Exception e) {
                logger.error("Ошибка при откате транзакции", e);
            }
        }
    }

    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User", User.class).list();
        } catch (PersistenceException e) {
            logger.error("Произошла ошибка при получении пользователей: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public User getUser(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            String sql = "FROM User WHERE id = :id";
            Query<User> query = session.createQuery(sql, User.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (PersistenceException e) {
            logger.error("Failed to find product with id: {}", id, e);
            return null;
        }
    }

    public User updateUserAge(Integer userId, Integer newAge) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User currentUser = session.get(User.class, userId);
            if (currentUser == null) {
                logger.warn("Пользователь с таким Id не найден в БД");
                session.getTransaction().rollback();
                return null;
            }
            currentUser.setAge(newAge);
            session.getTransaction().commit();
            logger.info("Возраст пользователя успешно обновлен");
            return currentUser;
        } catch (OptimisticLockException e) {
            throw new StaleObjectStateException("Сущность была модифицирована другой транзакцией", e);
        }
    }

    public User updateUserEmail(Integer userId, String newEmail) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User currentUser = session.get(User.class, userId);
            if (currentUser == null) {
                logger.warn("Пользователь с таким Id не найден в БД");
                session.getTransaction().rollback();
                return null;
            }
            currentUser.setEmail(newEmail);
            session.getTransaction().commit();
            logger.info("Эл. почта пользователя успешно обновлена");
            return currentUser;
        } catch (OptimisticLockException e) {
            throw new StaleObjectStateException("Сущность была модифицирована другой транзакцией", e);
        }
    }

    public User deleteUser(Integer userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User currentUser = session.get(User.class, userId);
            if (currentUser == null) {
                logger.warn("Пользователь с таким Id не найден в БД");
                session.getTransaction().rollback();
                return null;
            }
            session.remove(currentUser);
            session.getTransaction().commit();
            logger.info("Пользователь успешно удален");
            return currentUser;
        }
    }
}
