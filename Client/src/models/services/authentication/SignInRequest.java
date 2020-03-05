package models.services.authentication;

import models.services.WebApiRequest;

public class SignInRequest implements WebApiRequest {
    public String username;
    public String password;
    public enum SignInType {
        PLAIN,
        SHA256
    }
    public SignInType type;

    public SignInRequest(String username, String password, SignInType type) {
        this.username = username;
        this.password = password;
        this.type = type;
    }

}
