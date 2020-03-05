package services;

import Config.Config;
import com.google.gson.Gson;
import controller.Client;
import models.JsonConverter;
import models.message.Message;
import models.services.authentication.SignInRequest;
import models.services.authentication.SignInResponse;
import models.services.registration.SignUpRequest;
import models.services.registration.SignUpResponse;

import javax.naming.ConfigurationException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;

public final class AuthenticationService {
    private static AuthenticationService instance;
    private ApiClientService apiClient;
    private URI signInUri;
    private static final String SERVER_NAME = Config.getInstance().getProperty("SERVER_NAME");
    private static final String SIGN_IN_ENDPOINT = "/api/authentication/v1/signin/";
    private static final String HTTPS = "https";

    private AuthenticationService() {
        this.apiClient = ApiClientService.getInstance();
        String apiUri = Config.getInstance().getProperty("AUTHENTICATION_API_URI");
        String apiHost = Config.getInstance().getProperty("API_HOST");
        try {
            if (apiUri != null) {
                signInUri = new URI(apiUri);
            } else if (apiHost != null) {
                signInUri = new URI(HTTPS, apiHost, SIGN_IN_ENDPOINT, "");
            } else {
                throw new ConfigurationException();
            }
        } catch (URISyntaxException | ConfigurationException e) {
            throw new RuntimeException("No valid API host in config");
        }
    }

    public static AuthenticationService getInstance() {
        if (instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }

    private CompletableFuture<HttpResponse<String>> sendSignInRequestAsync(URI uri, SignInRequest request) {
        return this.apiClient.sendPostRequestAsync(uri, request);
    }

    private CompletableFuture<HttpResponse<String>> sendSignInRequestAsync(SignInRequest request) {
        return sendSignInRequestAsync(this.signInUri, request);
    }

    private SignInResponse processSignInResponse(HttpResponse<String> response) {
        int status = response.statusCode();
        //TODO: rework this to deserialise the actual error message if available
        if (status == 403) {
            return new SignInResponse("Invalid username or password");
        }
        if (status != 200) {
            return new SignInResponse("Authentication API error: " + status);
        }
        return JsonConverter.fromJson(response.body(), SignInResponse.class);

    }

    private boolean handleSignInResult(SignInResponse signInResponse) {
        if (signInResponse.error == null) {
            return true;
        } else {
            Client.getInstance().showError(signInResponse.error);
            return false;
        }
    }

    public void signIn(String username, String password) {
        SignInRequest signInRequest;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String hash = new PasswordConverterService(md).convert(username, password);
            signInRequest = new SignInRequest(username, hash, SignInRequest.SignInType.SHA256);
        } catch (NoSuchAlgorithmException e) {
            signInRequest = new SignInRequest(username, password, SignInRequest.SignInType.PLAIN);
        }
        this.sendSignInRequestAsync(signInRequest)
                .thenApply(this::processSignInResponse)
                .thenApply(this::handleSignInResult)
                .thenAccept(b -> {if (b) {Client.getInstance().addToSendingMessagesAndSend(
                        Message.makeLogInMessage(SERVER_NAME, username, password));}}); //temporary to keep reverse compatibility
    }

}
