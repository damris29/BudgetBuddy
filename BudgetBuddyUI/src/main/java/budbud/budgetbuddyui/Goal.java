package budbud.budgetbuddyui;

public class Goal {
    private String title;
    private double targetAmount;
    private double currentAmount;

    public Goal(String title, double targetAmount) {
        this.title = title;
        this.targetAmount = targetAmount;
        this.currentAmount = 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public void addAmount(double amount) {
        this.currentAmount += amount;
    }

    public double getProgress() {
        return Math.min(currentAmount / targetAmount, 1.0);
    }
}
