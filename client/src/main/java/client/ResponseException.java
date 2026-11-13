package client;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ResponseException extends Exception {

    public enum Code {
        ServerError,
        ClientError,
    }

    final private Code code;

    public ResponseException(Code code, String message) {
        super(message);
        this.code = code;
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage(), "status", code));
    }

    public static ResponseException fromJson(String json, int httpStatus) {
        var map = new Gson().fromJson(json, HashMap.class);

        String message = map != null && map.get("message") != null
                ? map.get("message").toString()
                : "HTTP Error: " + httpStatus;

        Code code = fromHttpStatusCode(httpStatus);

        return new ResponseException(code, message);
    }

    public Code code() {
        return code;
    }

    public static Code fromHttpStatusCode(int httpStatusCode) {
        return switch (httpStatusCode) {
            case 500 -> Code.ServerError;
            case 400 -> Code.ClientError;
            case 401 -> Code.ClientError;
            case 403 -> Code.ClientError;
            default -> throw new IllegalArgumentException("Unknown HTTP status code: " + httpStatusCode);
        };
    }

    public int toHttpStatusCode() {
        return switch (code) {
            case ServerError -> 500;
            case ClientError -> 400;
        };
    }
}