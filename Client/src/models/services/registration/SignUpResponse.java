package models.services.registration;

import models.services.WebApiResponse;

public class SignUpResponse implements WebApiResponse {
    
    public String error;

    public SignUpResponse() {
        this.error = null;
    }
    
    public SignUpResponse(String error) {
        this.error = error;
    }

}
