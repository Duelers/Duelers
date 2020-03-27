package org.projectcardboard.client.models.request;

import shared.models.services.WebApiRequest;

public class SignInRequest implements WebApiRequest {
    public final String username;
    public final String password;
    public enum SignInType {
        PLAIN,
        SHA256
    }
    public final SignInType type;

    public SignInRequest(String username, String password, SignInType type) {
        this.username = username;
        this.password = password;
        this.type = type;
    }

}
