package service;

import model.User;
import java.util.Scanner;

public class UserSession {
    private User user;
    private Scanner scanner;

    public UserSession(User user, Scanner scanner) {
        this.user = user;
        this.scanner = scanner;
    }

    public void start() {
        while (true) {
            System.out.println("\nВведите нужную команду: income - указать доход, expense - указать расход");
            System.out.println("budget - установить бюджет, summary - показать отчет, logout - выйти");

            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "income":
                    handleIncome();
                    break;
                case "expense":
                    handleExpense();
                    break;
                case "budget":
                    handleBudget();
                    break;
                case "summary":
                    user.getWallet().printSummary();
                    break;
                case "logout":
                    System.out.println("Выход из аккаунта");
                    return;
                default:
                    System.out.println("Неизвестная команда! Попробуйте снова.");
            }
        }
    }

    private void handleIncome() {
        System.out.print("Категория дохода: ");
        String category = scanner.nextLine().trim();

        double amount = getValidAmount("Введите сумму дохода: ");
        if (amount >= 0) {
            user.getWallet().addIncome(category, amount);
            System.out.println("Доход добавлен!");
        }
    }

    private void handleExpense() {
        System.out.print("Категория расхода: ");
        String category = scanner.nextLine().trim();

        double amount = getValidAmount("Введите сумму расхода: ");
        if (amount >= 0) {
            user.getWallet().addExpense(category, amount);
            System.out.println("Расход добавлен!");
        }
    }

    private void handleBudget() {
        System.out.print("Категория бюджета: ");
        String category = scanner.nextLine().trim();

        double amount = getValidAmount("Введите сумму бюджета: ");
        if (amount >= 0) {
            user.getWallet().setBudget(category, amount);
            System.out.println("Бюджет установлен!");
        }
    }

    private double getValidAmount(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double amount = Double.parseDouble(input);
                if (amount < 0) {
                    System.out.println("Ошибка: сумма не может быть отрицательной. Повторите ввод.");
                } else {
                    return amount;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное число!");
            }
        }
    }
}
