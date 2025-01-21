package service;

import model.User;

import java.util.HashMap;
import java.util.Map;

public class FinanceApp {
    private Map<String, User> users;

    public FinanceApp() {
        users = new HashMap<>();
    }

    public void registerUser(String username, String password) {
        if (users.containsKey(username)) {
            System.out.println("Пользователь уже существует!");
            return;
        }
        users.put(username, new User(username, password));
        System.out.println("Пользователь успешно зарегистрирован!");
    }

    public User loginUser(String username, String password) {
        User user = users.get(username);
        if (user == null || !user.getPassword().equals(password)) {
            System.out.println("Неверный логин или пароль!");
            return null;
        }
        return user;
    }
}
