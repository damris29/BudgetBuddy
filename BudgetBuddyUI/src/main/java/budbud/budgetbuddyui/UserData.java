package budbud.budgetbuddyui;

public class UserData {
    private static UserData instance;
    private String username;
    private String email;
    private String phone;
    private String birthdate;
    private String password;

    private UserData() { }

    public static UserData getInstance() {
        if (instance == null) {
            instance = new UserData();
        }
        return instance;
    }

    public void setUserData(String username, String email, String phone, String birthdate, String password) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.birthdate = birthdate;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }
    public String getBirthdate() {
        return birthdate;
    }
    public String getPassword(){
        return password;
    }
}
