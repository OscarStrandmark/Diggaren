package models;

public class ErrorObject {
    public int statusCode;
    public String message;

    public ErrorObject(int statusCode, String message){
        this.statusCode = statusCode;
        this.message = message;
    }
}
