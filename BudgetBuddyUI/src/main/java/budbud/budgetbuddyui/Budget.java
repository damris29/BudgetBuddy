package budbud.budgetbuddyui;

import java.util.ArrayList;
import java.util.List;

public class Budget {
    private final String month;
    private final List<String> categories;
    private final double amount;

    public Budget(String month, List<String> categories, double amount) {
        this.month = month;
        this.categories = categories;
        this.amount = amount;
    }

    public String getMonth() {
        return month;
    }

    public List<String> getCategories() {
        return categories;
    }

    public double getAmount() {
        return amount;
    }
}
