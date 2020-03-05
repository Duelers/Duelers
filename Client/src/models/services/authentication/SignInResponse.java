package models.services.authentication;

import models.services.WebApiResponse;

public class SignInResponse implements WebApiResponse {
    
    public String error;
    public String token;

    public SignInResponse() {
        this.error = null;
    }
    
    public SignInResponse(String error) {
        this.error = error;
    }

}
