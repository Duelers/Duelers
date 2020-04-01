package server.services;

import shared.models.services.token.AuthenticationTokenResponse;

import java.util.concurrent.CompletableFuture;

public interface TokenVerificationService {

  public CompletableFuture<AuthenticationTokenResponse> verifyAuthenticationToken(String token);
}
