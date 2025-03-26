package budbud.budgetbuddyui;

import java.util.ArrayList;
import java.util.List;

public class BudgetDataModel {
    private static final List<Budget> budgetList = new ArrayList<>();

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
    }

    public static void removeBudget(Budget budget) {
        budgetList.remove(budget);
    }
}
