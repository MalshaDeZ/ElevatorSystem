package md.exceptions;

public abstract class ForbiddenException extends RuntimeException{
    private static final String ERROR_MESSAGE_HEADER = "forbidden action";

    public ForbiddenException(String data) {
        super(ERROR_MESSAGE_HEADER + " : " + String.join(",", data));
    }
}
