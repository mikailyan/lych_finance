import model.User;
import service.FinanceApp;
import service.UserSession;

import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FinanceApp app = new FinanceApp();
        Scanner scanner = new Scanner(System.in);

        Map<String, User> users = app.getUsers();
        if (!users.isEmpty()) {
            System.out.println("Обнаружены сохранённые пользователи. Хотите автоматически войти? (yes/no)");
            String autoLogin = scanner.nextLine().trim().toLowerCase();

            if (autoLogin.equals("yes")) {
                for (String username : users.keySet()) {
                    User user = users.get(username);
                    System.out.println("Автовход для пользователя: " + username);
                    new UserSession(user, scanner).start();
                    return;
                }
            }
        }

        while (true) {
            System.out.println("Введите команду (register, login, exit):");
            String command = scanner.nextLine();

            if (command.equals("register")) {
                System.out.println("Введите логин:");
                String username = scanner.nextLine();
                System.out.println("Введите пароль:");
                String password = scanner.nextLine();
                app.registerUser(username, password);
            } else if (command.equals("login")) {
                System.out.println("Введите логин:");
                String username = scanner.nextLine();
                System.out.println("Введите пароль:");
                String password = scanner.nextLine();
                User user = app.loginUser(username, password);
                if (user != null) {
                    new UserSession(user, scanner).start();
                }
            } else if (command.equals("exit")) {
                app.saveUsersToJson();
                break;
            } else {
                System.out.println("Неизвестная команда!");
            }
        }
    }
}
