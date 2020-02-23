package server.clientPortal.models.message;

public class AccountFields {
    private String username;
    private String password;

    AccountFields(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
