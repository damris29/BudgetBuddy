package budbud.budgetbuddyui;

public class Transaction {
    private final String month;
    private final String category;
    private final double amount;

    public Transaction(String month, String category, double amount) {
        this.month = month;
        this.category = category;
        this.amount = amount;
    }

    public String getMonth() {
        return month;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }
}
