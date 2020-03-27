package org.projectcardboard.client.models.response;

import shared.models.services.WebApiResponse;

public class SignInResponse implements WebApiResponse {
    
    public final String error;
    public String token;

    public SignInResponse() {
        this.error = null;
    }
    
    public SignInResponse(String error) {
        this.error = error;
    }

}
