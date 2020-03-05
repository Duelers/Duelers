package services;

import com.google.gson.Gson;
import shared.models.services.WebApiRequest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class ApiClientService {
    private static ApiClientService instance;
    private final Gson gson = new Gson();
    private HttpClient httpClient;

    private ApiClientService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public static ApiClientService getInstance() {
        if (instance == null) {
            instance = new ApiClientService();
        }
        return instance;
    }

    public CompletableFuture<HttpResponse<String>> sendPostRequestAsync(URI uri, WebApiRequest request) {
        String requestBody = gson.toJson(request);
        HttpRequest httpRequest  = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        return this.httpClient
                .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
    }


}
