package shared.models.services.token;

import shared.models.services.WebApiRequest;

public class VerifyTokenRequest implements WebApiRequest {
    public String token;

    public VerifyTokenRequest(String token) {
        this.token = token;
    }

}
