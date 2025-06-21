package com.github.tapotas1234;

import com.github.tapotas1234.dao.UserDAOImpl;
import com.github.tapotas1234.model.User;

import java.util.Scanner;

public class App {
    public static void main( String[] args ) {
        while (true) {
            System.out.println("Выберите команду:\n" +
                    "1) Вывести всех пользователей из БД\n" +
                    "2) Вывести информацию о пользователе по его id (чтобы узнать конкретный id воспользуйтесь командой 1\n" +
                    "3) Обновить у пользователя email по id\n" +
                    "4) Обновить возраст пользователя по id\n" +
                    "5) Удалить пользователя по id\n" +
                    "6) Добавить пользователя");
            Scanner sc = new Scanner(System.in);
            int command = sc.nextInt();
            int userId;
            UserService service = new UserService(new UserDAOImpl());
            switch (command) {
                case (1) -> System.out.println(service.getAllUsers());
                case (2) -> {
                    System.out.println("Введите id пользователя: ");
                    userId = sc.nextInt();
                    System.out.println(service.getUserById(userId));
                }
                case (3) -> {
                    System.out.println("Введите id пользователя: ");
                    userId = sc.nextInt();
                    System.out.println("Введите новый email пользователя: ");
                    String email = sc.next();
                    service.updateUserEmail(userId, email);
                    System.out.println("Email успешно обновлен");
                }
                case (4) -> {
                    System.out.println("Введите id пользователя: ");
                    userId = sc.nextInt();
                    System.out.println("Введите возраст пользователя: ");
                    Integer age = sc.nextInt();
                    service.updateUserAge(userId, age);
                    System.out.println("Возраст успешно обновлен");
                }
                case (5) -> {
                    System.out.println("Введите id пользователя: ");
                    userId = sc.nextInt();
                    service.deleteUser(userId);
                    System.out.println("Пользователь успешно удален");
                }
                case (6) -> {
                    System.out.println("Введите имя пользователя");
                    String name = sc.next();
                    System.out.println("Введите возраст пользователя");
                    Integer age = sc.nextInt();
                    System.out.println("Введите email пользователя");
                    String email = sc.next();
                    service.addUser(new User(name, age, email));
                    System.out.println("Пользователь успешно добавлен");
                }
            }
        }
    }
}

