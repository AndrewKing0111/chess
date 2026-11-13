package client;

import com.google.gson.Gson;
import model.*;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) throws ResponseException {
        serverUrl = url;
        clear();
    }

    public void clear() throws ResponseException {
        var request = buildRequest("DELETE", "/db", null, false, null);
        sendRequest(request);
    }

    public void createGame(String gameName, String authToken) throws ResponseException {
        var request = buildRequest("POST", "/game", Map.of("gameName", gameName), true, authToken);
        sendRequest(request);
    }

    public void joinGame(String playerColor, String gameID) throws ResponseException {
        var request = buildRequest("PUT", "/game", Map.of("playerColor", playerColor, "gameID", gameID), false, null);
        sendRequest(request);
    }

    public GameListData listGames(String authToken) throws ResponseException {
        var request = buildRequest("GET", "/game", null, true, authToken);
        var response = sendRequest(request);
        return handleResponse(response, GameListData.class);
    }

    public AuthData loginUser(String username, String password) throws ResponseException {
        var request = buildRequest("POST", "/session", Map.of("username", username, "password", password), false, null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public AuthData registerUser(String username, String password, String email) throws ResponseException {
        var request = buildRequest("POST", "/user", Map.of("username", username, "password", password, "email", email), false, null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    private HttpRequest buildRequest(String method, String path, Object body, boolean header, String authToken) {
        HttpRequest.Builder request;
        if (header) {
            request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + path))
                    .method(method, makeRequestBody(body))
                    .header("authorization", authToken);
        } else {
            request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + path))
                    .method(method, makeRequestBody(body));
        }
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();

            if (body != null && !body.isBlank()) {
                throw ResponseException.fromJson(body, status);
            }

            throw new ResponseException(
                    ResponseException.fromHttpStatusCode(status),
                    "HTTP Error: " + status
            );
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}