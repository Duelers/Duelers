package shared.models.services.token;

import shared.models.services.WebApiResponse;

public class AuthenticationTokenResponse implements WebApiResponse {
    
    public String error;
    public String username;

    public AuthenticationTokenResponse() {
        this.error = null;
    }
    
    public AuthenticationTokenResponse(String error) {
        this.error = error;
    }

}
