package org.projectcardboard.client.services;

import Config.Config;
import org.projectcardboard.client.models.request.SignInRequest;
import org.projectcardboard.client.models.response.SignInResponse;

import javax.naming.ConfigurationException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;

public final class AuthenticationService {
  private static AuthenticationService instance;
  private final ApiClientService apiClient;
  private URI signInUri;
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

  private CompletableFuture<HttpResponse<String>> sendSignInRequestAsync(URI uri,
      SignInRequest request) {
    return this.apiClient.sendPostRequestAsync(uri, request);
  }

  private CompletableFuture<HttpResponse<String>> sendSignInRequestAsync(SignInRequest request) {
    return sendSignInRequestAsync(this.signInUri, request);
  }

  public CompletableFuture<SignInResponse> signIn(String username, String password) {
    SignInRequest signInRequest;
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      String hash = new PasswordConverterService(md).convertToHexString(username, password);
      signInRequest = new SignInRequest(username, hash, SignInRequest.SignInType.SHA256);
    } catch (NoSuchAlgorithmException e) {
      signInRequest = new SignInRequest(username, password, SignInRequest.SignInType.PLAIN);
    }
    return this.sendSignInRequestAsync(signInRequest)
        .thenApply(r -> this.apiClient.processResponse(r, SignInResponse.class));
  }

}
