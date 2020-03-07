package models;

public class ErrorObject {
    private int statusCode;
    private String errorMessage;

    public ErrorObject(int statusCode, String errorMessage){
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    public ErrorObject(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
