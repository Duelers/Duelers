package models.services.registration;

import models.services.WebApiRequest;

public class SignUpRequest implements WebApiRequest {
    public String username;
    public String password;
    public enum SignUpType {
        PLAIN,
        SHA256
    }
    public SignUpType type;

    public SignUpRequest(String username, String password, SignUpType type) {
        this.username = username;
        this.password = password;
        this.type = type;
    }

}
