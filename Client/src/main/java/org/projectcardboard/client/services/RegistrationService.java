package org.projectcardboard.client.services;

import Config.Config;
import org.projectcardboard.client.models.request.SignUpRequest;
import org.projectcardboard.client.models.response.SignUpResponse;

import javax.naming.ConfigurationException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;

public final class RegistrationService {
    private static RegistrationService instance;
    private ApiClientService apiClient;
    private URI signUpUri;
    private static final String SIGN_UP_ENDPOINT = "/api/registration/v1/signup/";
    private static final String HTTPS = "https";

    private RegistrationService() {
        this.apiClient = ApiClientService.getInstance();
        String apiUri = Config.getInstance().getProperty("REGISTRATION_API_URI");
        String apiHost = Config.getInstance().getProperty("API_HOST");
        try {
            if (apiUri != null) {
                signUpUri = new URI(apiUri);
            } else if (apiHost != null) {
                signUpUri = new URI(HTTPS, apiHost, SIGN_UP_ENDPOINT, "");
            } else {
                throw new ConfigurationException();
            }
        } catch (URISyntaxException | ConfigurationException e) {
            throw new RuntimeException("No valid API host in config");
        }
    }

    public static RegistrationService getInstance() {
        if (instance == null) {
            instance = new RegistrationService();
        }
        return instance;
    }

    private CompletableFuture<HttpResponse<String>> sendSignUpRequestAsync(URI uri, SignUpRequest request) {
        return this.apiClient.sendPostRequestAsync(uri, request);
    }

    private CompletableFuture<HttpResponse<String>> sendSignUpRequestAsync(SignUpRequest request) {
        return sendSignUpRequestAsync(this.signUpUri, request);
    }

    public CompletableFuture<SignUpResponse> signUp(String username, String password) {
        SignUpRequest signUpRequest;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String hash = new PasswordConverterService(md).convertToHexString(username, password);
            signUpRequest = new SignUpRequest(username, hash, SignUpRequest.SignUpType.SHA256);
        } catch (NoSuchAlgorithmException e) {
            signUpRequest = new SignUpRequest(username, password, SignUpRequest.SignUpType.PLAIN);
        }
        return this.sendSignUpRequestAsync(signUpRequest)
                .thenApply(r -> this.apiClient.processResponse(r, SignUpResponse.class));
    }

}
