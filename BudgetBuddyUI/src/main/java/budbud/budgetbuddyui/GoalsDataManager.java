package budbud.budgetbuddyui;

import java.util.ArrayList;
import java.util.List;

public class GoalsDataManager {
    // Singleton instance
    private static GoalsDataManager instance;

    // Data storage
    private List<Goal> goals = new ArrayList<>();

    // Private constructor to prevent instantiation
    private GoalsDataManager() {}

    // Get the singleton instance
    public static GoalsDataManager getInstance() {
        if (instance == null) {
            instance = new GoalsDataManager();
        }
        return instance;
    }

    // Add a new goal
    public void addGoal(Goal goal) {
        goals.add(goal);
    }

    // Get all goals
    public List<Goal> getGoals() {
        return goals;
    }

    // Update an existing goal
    public void updateGoal(int index, Goal updatedGoal) {
        if (index >= 0 && index < goals.size()) {
            goals.set(index, updatedGoal);
        }
    }

    // Delete a goal
    public void deleteGoal(int index) {
        if (index >= 0 && index < goals.size()) {
            goals.remove(index);
        }
    }

    // Clear all goals
    public void clearGoals() {
        goals.clear();
    }
}
