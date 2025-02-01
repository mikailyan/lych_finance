package model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.*;

public class Wallet implements Serializable {
    private static final long serialVersionUID = 1L;
    private double balance;
    private List<Transaction> transactions;
    private Map<String, Double> budgets;
    private String username;
    private String passwordHash;

    public Wallet(String username, String passwordHash) {
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
        this.budgets = new HashMap<>();
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public void addIncome(String category, double amount) {
        balance += amount;
        transactions.add(new Transaction("income", category, amount));
    }

    public void addExpense(String category, double amount) {
        balance -= amount;
        transactions.add(new Transaction("expense", category, amount));

        if (budgets.containsKey(category)) {
            double remainingBudget = budgets.get(category) - amount;
            budgets.put(category, remainingBudget);
            if (remainingBudget < 0) {
                System.out.println("Превышен бюджет по категории: " + category);
            }
        }
    }

    public void setBudget(String category, double amount) {
        budgets.put(category, amount);
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Map<String, Double> getBudgets() {
        return budgets;
    }

    public void printSummary() {
        double totalIncome = calculateTotalIncome();
        double totalExpense = calculateTotalExpense();

        System.out.println("Пользователь: " + username);
        System.out.println("Общий доход: " + totalIncome);
        System.out.println("Доходы по категориям:");
        Map<String, Double> incomeByCategory = calculateIncomeByCategory();
        for (Map.Entry<String, Double> entry : incomeByCategory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("Общие расходы: " + totalExpense);
        System.out.println("Бюджет по категориям:");
        Map<String, Double> budgetSummary = calculateBudgetSummary();
        for (Map.Entry<String, Double> entry : budgetSummary.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + ", Оставшийся бюджет: " + (budgets.get(entry.getKey()) - entry.getValue()));
        }

        System.out.println("Текущий баланс: " + balance);

        saveSummaryToJson(totalIncome, incomeByCategory, totalExpense, budgetSummary);
    }

    private double calculateTotalIncome() {
        return transactions.stream()
                .filter(t -> t.getType().equals("income"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    private double calculateTotalExpense() {
        return transactions.stream()
                .filter(t -> t.getType().equals("expense"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    private Map<String, Double> calculateIncomeByCategory() {
        Map<String, Double> incomeByCategory = new HashMap<>();
        for (Transaction t : transactions) {
            if (t.getType().equals("income")) {
                incomeByCategory.put(
                        t.getCategory(),
                        incomeByCategory.getOrDefault(t.getCategory(), 0.0) + t.getAmount()
                );
            }
        }
        return incomeByCategory;
    }

    private Map<String, Double> calculateBudgetSummary() {
        Map<String, Double> budgetSummary = new HashMap<>();
        for (Map.Entry<String, Double> entry : budgets.entrySet()) {
            String category = entry.getKey();
            double expense = transactions.stream()
                    .filter(t -> t.getType().equals("expense") && t.getCategory().equals(category))
                    .mapToDouble(Transaction::getAmount)
                    .sum();
            budgetSummary.put(category, expense);
        }
        return budgetSummary;
    }

    private void saveSummaryToJson(double totalIncome, Map<String, Double> incomeByCategory, double totalExpense, Map<String, Double> budgetSummary) {
        try {
            File file = new File("data/summary.json");
            file.getParentFile().mkdirs();

            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println("{");
                writer.println("  \"user\": {");
                writer.println("    \"username\": \"" + username + "\",");
                writer.println("    \"passwordHash\": \"" + passwordHash + "\",");
                writer.println("    \"balance\": " + balance);
                writer.println("  },");
                writer.println("  \"totalIncome\": " + totalIncome + ",");
                writer.println("  \"incomeByCategory\": {");
                int i = 0;
                for (Map.Entry<String, Double> entry : incomeByCategory.entrySet()) {
                    writer.println("    \"" + entry.getKey() + "\": " + entry.getValue() + (i++ < incomeByCategory.size() - 1 ? "," : ""));
                }
                writer.println("  },");
                writer.println("  \"totalExpense\": " + totalExpense + ",");
                writer.println("  \"budgetbyCategory\": {");
                i = 0;
                for (Map.Entry<String, Double> entry : budgetSummary.entrySet()) {
                    double remaining = budgets.get(entry.getKey()) - entry.getValue();
                    writer.println("    \"" + entry.getKey() + "\": {");
                    writer.println("      \"spent\": " + entry.getValue() + ",");
                    writer.println("      \"remainingBudget\": " + remaining);
                    writer.println("    }" + (i++ < budgetSummary.size() - 1 ? "," : ""));
                }
                writer.println("  }");
                writer.println("}");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении отчета: " + e.getMessage());
        }
    }
}
