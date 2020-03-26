package org.projectcardboard.client.models.request;

import shared.models.services.WebApiRequest;

public class SignUpRequest implements WebApiRequest {
  public final String username;
  public final String password;

  public enum SignUpType {
    PLAIN, SHA256
  }

  public final SignUpType type;

  public SignUpRequest(String username, String password, SignUpType type) {
    this.username = username;
    this.password = password;
    this.type = type;
  }

}
