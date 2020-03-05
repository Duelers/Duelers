package services;

import Config.Config;
import controller.Client;
import models.message.Message;
import models.services.registration.SignUpRequest;
import models.services.registration.SignUpResponse;

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
    private static final String SERVER_NAME = Config.getInstance().getProperty("SERVER_NAME");
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

    private SignUpResponse processSignUpResponse(HttpResponse<String> response) {
        int status = response.statusCode();
        //TODO: rework this to deserialise the actual error message if available
        if (status == 400) {
            return new SignUpResponse("Username already taken");
        }
        if (status != 201) {
            return new SignUpResponse("Registration API error: " + status);
        }
        return new SignUpResponse();

    }

    private boolean handleSignUpResult(SignUpResponse signUpResponse) {
        if (signUpResponse.error == null) {
            return true;
        } else {
            Client.getInstance().showError(signUpResponse.error);
            return false;
        }
    }

    public void signUp(String username, String password) {
        SignUpRequest signUpRequest;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String hash = new PasswordConverterService(md).convert(username, password);
            signUpRequest = new SignUpRequest(username, hash, SignUpRequest.SignUpType.SHA256);
        } catch (NoSuchAlgorithmException e) {
            signUpRequest = new SignUpRequest(username, password, SignUpRequest.SignUpType.PLAIN);
        }
        this.sendSignUpRequestAsync(signUpRequest)
                .thenApply(this::processSignUpResponse)
                .thenApply(this::handleSignUpResult)
                .thenAccept(b -> {if (b) {AuthenticationService.getInstance().signIn( username, password);}});
    }

}
