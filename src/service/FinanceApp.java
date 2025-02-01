package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import model.User;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class FinanceApp {
    private static final String DATA_FILE = "data/summary.json";
    private Map<String, User> users;
    private final Gson gson;

    public FinanceApp() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.users = new HashMap<>();
        loadUsersFromJson();
    }

    public void registerUser(String username, String password) {
        if (users.containsKey(username)) {
            System.out.println("Пользователь уже существует!");
            return;
        }
        users.put(username, new User(username, password));
        saveUsersToJson();
        System.out.println("Пользователь " + username + " зарегистрирован!");
    }

    public User loginUser(String username, String password) {
        User user = users.get(username);
        if (user == null || !user.checkPassword(password)) {
            System.out.println("Неверный логин или пароль!");
            return null;
        }
        return user;
    }

    public void saveUsersToJson() {
        try (Writer writer = new FileWriter(DATA_FILE)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            System.out.println("Ошибка сохранения данных: " + e.getMessage());
        }
    }

    private void loadUsersFromJson() {
    File file = new File(DATA_FILE);
    if (!file.exists()) {
        return; // Файл не найден, выходим
    }

    try (Reader reader = new FileReader(DATA_FILE)) {
        Type type = new TypeToken<Map<String, User>>() {}.getType();
        Map<String, User> loadedUsers = gson.fromJson(reader, type);

        if (loadedUsers == null || loadedUsers.isEmpty()) {
            System.out.println("Файл JSON пуст или содержит некорректные данные.");
            return;
        }

        users = loadedUsers;
    } catch (IOException e) {
        System.out.println("Ошибка загрузки файла: " + e.getMessage());
    } catch (JsonSyntaxException e) {
        System.out.println("Ошибка формата JSON. Возможно, файл повреждён. Пересоздайте summary.json.");
    }
}

    public Map<String, User> getUsers() {
        return users;
    }
}
