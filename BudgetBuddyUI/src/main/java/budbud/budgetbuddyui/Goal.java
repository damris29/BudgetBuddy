package budbud.budgetbuddyui;

public class Goal {
    private String title;
    private double targetAmount;
    private double currentAmount;

    public Goal(String title, double targetAmount) {
        // Input validation
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (targetAmount <= 0) {
            throw new IllegalArgumentException("Target amount must be greater than zero");
        }
        this.title = title;
        this.targetAmount = targetAmount;
        this.currentAmount = 0;
    }

    //Gets the title of the goal
    public String getTitle() {
        return title;
    }

    //Updates the title of the goal
    public void setTitle(String title) {
        this.title = title;
    }

    //Gets the target amount needed for the goal
    public double getTargetAmount() {
        return targetAmount;
    }

    //Updates the target amount for the goal
    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    //Gets the current amount saved towards the goal
    public double getCurrentAmount() {
        return currentAmount;
    }

    //Updates the current amount saved for the goal
    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }

    //add amount to the desired goal
    public void addAmount(double amount) {
        this.currentAmount += amount;
    }

    public double getProgress() {
        return Math.min(currentAmount / targetAmount, 1.0);
    }
}
