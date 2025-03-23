package budbud.budgetbuddyui;

import java.util.ArrayList;
import java.util.List;

public class UserData {
    private String username;
    private String email;
    private String phone;
    private String birthdate;
    private String password;

    public UserData(String username, String email, String phone, String birthdate, String password) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.birthdate = birthdate;
        this.password = password;
    }

    //Getter
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getBirthdate() { return birthdate; }
    public String getPassword() { return password; }

    //Setter
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }
    public void setPassword(String password) { this.password = password; }

    public static class UserManager {
        private static UserData loggedInUser;  // Store the logged-in user
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
        public static void setLoggedInUser(UserData user) {
            loggedInUser = user;
        }
        public static UserData getLoggedInUser() {
            return loggedInUser;
        }
    }
}
