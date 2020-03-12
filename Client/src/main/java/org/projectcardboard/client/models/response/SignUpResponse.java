package org.projectcardboard.client.models.response;

import shared.models.services.WebApiResponse;

public class SignUpResponse implements WebApiResponse {
    
    public final String error;

    public SignUpResponse() {
        this.error = null;
    }
    
    public SignUpResponse(String error) {
        this.error = error;
    }

}
