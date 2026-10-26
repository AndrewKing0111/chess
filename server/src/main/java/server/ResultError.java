package server;

import dataAccess.DataAccessException;

public class ResultError {
    private String message;

    public ResultError(DataAccessException error) {
        this.message = "Error: " + error.getMessage();
    }

    public String getMessage() {
        return message;
    }
}
