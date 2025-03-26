package budbud.budgetbuddyui;

import java.util.ArrayList;
import java.util.List;

interface Budgetable {
    String getMonth();
    List<String> getCategories();
    double getAmount();
}

public class Budget implements Budgetable {  // Implementing interface
    private final String month;
    private final List<String> categories;
    private final double amount;

    public Budget(String month, List<String> categories, double amount) {
        // Input validation with exception handling
        if (month == null || month.trim().isEmpty()) {
            throw new IllegalArgumentException("Month cannot be null or empty");
        }
        if (categories == null) {
            throw new IllegalArgumentException("Categories list cannot be null");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Budget amount cannot be negative");
        }

        this.month = month;
        this.categories = new ArrayList<>(categories);
        this.amount = amount;
    }

    @Override
    public String getMonth() {
        return month;
    }

    @Override
    public List<String> getCategories() {
        return categories;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Budget{" +
                "month='" + month + '\'' +
                ", categories=" + categories +
                ", amount=" + amount +
                '}';
    }
}
