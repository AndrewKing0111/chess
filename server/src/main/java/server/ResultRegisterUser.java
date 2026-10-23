package server;

import model.AuthData;
import model.UserData;

public class ResultRegisterUser {
    private final String username;
    private final String authToken;

    public ResultRegisterUser(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }
}
