package server.clientPortal.models.message;

public class ExceptionMessage {
    private String exceptionString;

    ExceptionMessage(String exceptionString) {
        this.exceptionString = exceptionString;
    }

    public String getExceptionString() {
        return exceptionString;
    }
}
