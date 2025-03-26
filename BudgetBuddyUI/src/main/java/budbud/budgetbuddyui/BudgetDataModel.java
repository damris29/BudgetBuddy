package budbud.budgetbuddyui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BudgetDataModel {
    private static final List<Budget> budgetList = new ArrayList<>();
    private static final Map<String, Map<String, Double>> expenses = new HashMap<>();

    // Budget management methods
    public static List<Budget> getBudgetList() {
        return budgetList;
    }

    public static void addBudget(Budget budget) {
        // Check if budget for this month already exists
        for (int i = 0; i < budgetList.size(); i++) {
            if (budgetList.get(i).getMonth().equals(budget.getMonth())) {
                // Replace existing budget
                budgetList.set(i, budget);
                return;
            }
        }
        // If no existing budget found, add new one
        budgetList.add(budget);

        // Initialize expenses map for this month
        if (!expenses.containsKey(budget.getMonth())) {
            expenses.put(budget.getMonth(), new HashMap<>());
        }
    }

    public static void removeBudget(Budget budget) {
        budgetList.remove(budget);
        // Optionally remove expenses for this month too
        expenses.remove(budget.getMonth());
    }

    public static Budget getBudgetByMonth(String month) {
        //Lambda expression
        return budgetList.stream()
                .filter(budget -> budget.getMonth().equals(month))
                .findFirst()
                .orElse(null);
    }

    // Expense tracking methods
    public static void addExpense(String month, String category, double amount) {
        // Initialize month map if it doesn't exist
        if (!expenses.containsKey(month)) {
            expenses.put(month, new HashMap<>());
        }

        // Get the expenses for the month
        Map<String, Double> monthExpenses = expenses.get(month);

        // Add to existing expense or create new one
        double currentAmount = monthExpenses.getOrDefault(category, 0.0);
        monthExpenses.put(category, currentAmount + amount);
    }

    public static double getExpenseForMonthAndCategory(String month, String category) {
        if (!expenses.containsKey(month)) {
            return 0.0;
        }

        Map<String, Double> monthExpenses = expenses.get(month);
        return monthExpenses.getOrDefault(category, 0.0);
    }

    public static double getTotalExpensesForMonth(String month) {
        if (!expenses.containsKey(month)) {
            return 0.0;
        }

        double total = 0.0;
        for (double amount : expenses.get(month).values()) {
            total += amount;
        }
        return total;
    }

    public static Map<String, Double> getExpensesForMonth(String month) {
        if (!expenses.containsKey(month)) {
            return new HashMap<>();
        }
        return expenses.get(month);
    }

    //toString() helps debugging, to see if the code works
    @Override
    public String toString() {
        return "BudgetDataModel{" +
                "budgetList=" + budgetList +
                ", expenses=" + expenses +
                '}';
    }
}
