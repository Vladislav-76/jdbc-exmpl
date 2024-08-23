package org.example;

import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean finish = false;
            while (!finish) {
                printMainMenu();
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> getUserByLogin(scanner);
                    case "2" -> createUser(scanner);
                    default -> finish = true;
                }
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("Введи символ:");
        System.out.println("1: Для получения пользователя по логину");
        System.out.println("2: Для сохранения пользователя в БД");
        System.out.println("Любой иной символ для выхода");
    }

    private static void getUserByLogin(Scanner scanner) throws SQLException {
        System.out.println("Введи логин");
        String login = scanner.nextLine();
        User user = new ServiceDB().userSelect(login);
        if (user != null) {
            System.out.println("User: " + user);
            System.out.println("Login: " + user.getLogin());
            System.out.println("Password: " + user.getPassword());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Date: " + user.getDate());
        }
        else System.out.println("Пользователь не найден.");
    }

    private static void createUser(Scanner scanner) {
        System.out.println("Введи логин");
        String login = scanner.nextLine();
        System.out.println("Введи пароль");
        String password = scanner.nextLine();
        System.out.println("Введи email");
        String email = scanner.nextLine();
        User user = new User(login, password, email, new Date());
        System.out.printf("Строк добавлено: %d\n", new ServiceDB().userInsert(user));
    }
}