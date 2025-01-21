import model.User;
import service.FinanceApp;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FinanceApp app = new FinanceApp();
        Scanner scanner = new Scanner(System.in);

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
                    while (true) {
                        System.out.println("Введите команду (income, expense, budget, summary, logout):");
                        String subCommand = scanner.nextLine();
                        if (subCommand.equals("income")) {
                            System.out.println("Категория:");
                            String category = scanner.nextLine();
                            System.out.println("Сумма:");
                            double amount = Double.parseDouble(scanner.nextLine());
                            user.getWallet().addIncome(category, amount);
                        } else if (subCommand.equals("expense")) {
                            System.out.println("Категория:");
                            String category = scanner.nextLine();
                            System.out.println("Сумма:");
                            double amount = Double.parseDouble(scanner.nextLine());
                            user.getWallet().addExpense(category, amount);
                        } else if (subCommand.equals("budget")) {
                            System.out.println("Категория:");
                            String category = scanner.nextLine();
                            System.out.println("Бюджет:");
                            double amount = Double.parseDouble(scanner.nextLine());
                            user.getWallet().setBudget(category, amount);
                        } else if (subCommand.equals("summary")) {
                            user.getWallet().printSummary();
                        } else if (subCommand.equals("logout")) {
                            break;
                        } else {
                            System.out.println("Неизвестная команда!");
                        }
                    }
                }
            } else if (command.equals("exit")) {
                System.out.println("Выход из программы.");
                break;
            } else {
                System.out.println("Неизвестная команда!");
            }
        }
        scanner.close();
    }
}
