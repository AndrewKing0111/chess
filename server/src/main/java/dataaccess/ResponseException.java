package dataaccess;

/**
 * Indicates there was an error connecting to the database
 */
public class ResponseException extends Exception {

    public enum Code {
        ServerError,
        ClientError
    }

    final private Code code;

    public ResponseException(Code code, String message) {
        super(message);
        this.code = code;
    }
}
