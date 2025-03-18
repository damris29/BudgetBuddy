package budbud.budgetbuddyui;

import java.util.ArrayList;
import java.util.List;

public class UserData {
    private final String username;
    private final String email;
    private final String phone;
    private final String birthdate;
    private final String password;

    public UserData(String username, String email, String phone, String birthdate, String password) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.birthdate = birthdate;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getBirthdate() { return birthdate; }
    public String getPassword() { return password; }

    public static class UserManager {
        private static final List<UserData> users = new ArrayList<>();

        //adds new user
        public static void addUser(UserData user) {
            users.add(user);
        }

        //checks if the user already has an account
        public static boolean isUserExists(String username, String email) {
            for (UserData user : users) {
                if (user.getUsername().equals(username) || user.getEmail().equals(email)) {
                    return true;
                }
            }
            return false;
        }

        public static List<UserData> getUsers() {
            return users;
        }
    }
}
