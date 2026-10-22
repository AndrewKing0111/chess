package server;

import dataaccess.DataAccessException;

public class ResultError {
    private String message;

    public ResultError(DataAccessException error) {
        this.message = "Error: " + error.getMessage();
    }
}
