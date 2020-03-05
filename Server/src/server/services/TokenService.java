package server.services;

import Config.Config;
import com.google.gson.Gson;
import shared.models.services.token.AuthenticationTokenResponse;
import shared.models.services.token.VerifyTokenRequest;

import javax.naming.ConfigurationException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public final class TokenService {
    private static TokenService instance;
    private ApiClientService apiClient;
    private URI verifyTokenUri;
    private static final String SERVER_NAME = Config.getInstance().getProperty("SERVER_NAME");
    private static final String VERIFY_TOKEN_ENDPOINT = "/api/token/v1/verify/";
    private static final String HTTPS = "https";

    private TokenService() {
        this.apiClient = ApiClientService.getInstance();
        String apiUri = Config.getInstance().getProperty("TOKEN_API_URI");
        String apiHost = Config.getInstance().getProperty("API_HOST");
        try {
            if (apiUri != null) {
                this.verifyTokenUri = new URI(apiUri);
            } else if (apiHost != null) {
                this.verifyTokenUri = new URI(HTTPS, apiHost, VERIFY_TOKEN_ENDPOINT, "");
            } else {
                throw new ConfigurationException();
            }
        } catch (URISyntaxException | ConfigurationException e) {
            throw new RuntimeException("No valid API host in config");
        }
    }

    public static TokenService getInstance() {
        if (instance == null) {
            instance = new TokenService();
        }
        return instance;
    }

    private CompletableFuture<HttpResponse<String>> sendVerifyTokenRequest(URI uri, VerifyTokenRequest request) {
        return this.apiClient.sendPostRequestAsync(uri, request);
    }

    private CompletableFuture<HttpResponse<String>> sendVerifyTokenRequest(VerifyTokenRequest request) {
        return sendVerifyTokenRequest(this.verifyTokenUri, request);
    }

    private AuthenticationTokenResponse processAuthenticationTokenResponse(HttpResponse<String> response) {
        int status = response.statusCode();
        //TODO: rework this to deserialise the actual error message if available
        if (status != 200) {
            return new AuthenticationTokenResponse("Token API error: " + status);
        }
        AuthenticationTokenResponse authenticationTokenResponse = new Gson().fromJson(response.body(), AuthenticationTokenResponse.class);
        authenticationTokenResponse.error = "";
        return authenticationTokenResponse;

    }

    public CompletableFuture<AuthenticationTokenResponse> verifyAuthenticationToken(String token) {
        VerifyTokenRequest verifyTokenRequest = new VerifyTokenRequest(token);
        return this.sendVerifyTokenRequest(verifyTokenRequest)
                .thenApply(this::processAuthenticationTokenResponse); //temporary to keep reverse compatibility
    }

}
