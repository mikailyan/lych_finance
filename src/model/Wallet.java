package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wallet {
    private double balance;
    private List<Transaction> transactions;
    private Map<String, Double> budgets;

    public Wallet() {
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
        this.budgets = new HashMap<>();
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

    public void printSummary() {
        System.out.println("Общий доход: " + calculateTotalIncome());
        System.out.println("Доходы по категориям:");
        printIncomeByCategory();
        System.out.println("Общие расходы: " + calculateTotalExpense());
        System.out.println("Бюджет по категориям:");
        printBudgetSummary();
        System.out.println("Текущий баланс: " + balance);
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

    private void printIncomeByCategory() {
        Map<String, Double> incomeByCategory = new HashMap<>();
        for (Transaction t : transactions) {
            if (t.getType().equals("income")) {
                incomeByCategory.put(
                        t.getCategory(),
                        incomeByCategory.getOrDefault(t.getCategory(), 0.0) + t.getAmount()
                );
            }
        }
        for (Map.Entry<String, Double> entry : incomeByCategory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private void printBudgetSummary() {
        for (Map.Entry<String, Double> entry : budgets.entrySet()) {
            String category = entry.getKey();
            double budget = entry.getValue();
            double expense = transactions.stream()
                    .filter(t -> t.getType().equals("expense") && t.getCategory().equals(category))
                    .mapToDouble(Transaction::getAmount)
                    .sum();
            double remaining = budget - expense;
            System.out.println(category + ": " + budget + ", Оставшийся бюджет: " + remaining);
        }
    }
}
