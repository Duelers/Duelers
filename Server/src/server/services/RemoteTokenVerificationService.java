package server.services;

import Config.Config;
import shared.models.services.token.AuthenticationTokenResponse;
import shared.models.services.token.VerifyTokenRequest;

import javax.naming.ConfigurationException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public final class RemoteTokenVerificationService implements TokenVerificationService {
    private static RemoteTokenVerificationService instance;
    private final ApiClientService apiClient;
    private URI verifyTokenUri;
    private static final String VERIFY_TOKEN_ENDPOINT = "/api/token/v1/verify/";
    private static final String HTTPS = "https";

    private RemoteTokenVerificationService() {
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

    public static RemoteTokenVerificationService getInstance() {
        if (instance == null) {
            instance = new RemoteTokenVerificationService();
        }
        return instance;
    }

    private CompletableFuture<HttpResponse<String>> sendVerifyTokenRequest(URI uri, VerifyTokenRequest request) {
        return this.apiClient.sendPostRequestAsync(uri, request);
    }

    private CompletableFuture<HttpResponse<String>> sendVerifyTokenRequest(VerifyTokenRequest request) {
        return sendVerifyTokenRequest(this.verifyTokenUri, request);
    }

    public CompletableFuture<AuthenticationTokenResponse> verifyAuthenticationToken(String token) {
        VerifyTokenRequest verifyTokenRequest = new VerifyTokenRequest(token);
        return this.sendVerifyTokenRequest(verifyTokenRequest)
                .thenApply(r -> this.apiClient.processResponse(r, AuthenticationTokenResponse.class));
    }

}
