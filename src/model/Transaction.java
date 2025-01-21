package model;

import java.time.LocalDateTime;

public class Transaction {
    private String type;
    private String category;
    private double amount;
    private LocalDateTime timestamp;

    public Transaction(String type, String category, double amount) {
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("%s - %s: %s - %.2f", timestamp, type, category, amount);
    }
}
