package budbud.budgetbuddyui;

import java.util.ArrayList;
import java.util.List;

// Interface for polymorphism
interface GoalManager {
    void addGoal(Goal goal);
    List<Goal> getGoals();
    void updateGoal(int index, Goal updatedGoal);
    void deleteGoal(Goal index);
    void clearGoals();
}

public class GoalsDataManager implements GoalManager {
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

    public void addGoal(Goal goal, boolean logAction) {
        goals.add(goal);
        if (logAction) {
            System.out.println("Goal added: " + goal);
        }
    }

    @Override
    public void addGoal(Goal goal) {
        addGoal(goal, false);
    }

    // Get all goals
    @Override
    public List<Goal> getGoals() {
        return goals;
    }

    // Update an existing goal with exception handling
    @Override
    public void updateGoal(int index, Goal updatedGoal) {
        try {
            goals.set(index, updatedGoal);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error: Invalid goal index " + index);
        }
    }

    // Delete a goal with exception handling
    @Override
    public void deleteGoal(Goal index) {
        try {
            goals.remove(index);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error: Invalid goal index " + index);
        }
    }

    // Clear all goals
    @Override
    public void clearGoals() {
        goals.clear();
    }

    // Override toString() to display stored goals
    @Override
    public String toString() {
        return "GoalsDataManager{" +
                "goals=" + goals +
                '}';
    }
}
