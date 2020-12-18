package dev.yafatek.networking.v1.models;


public class ApiResponse<T> {
    private boolean status;
    private String code;
    private String message;
    private T results;
    private ErrorResponse errors;

    public ApiResponse() {
    }


    public ApiResponse(boolean status, String code, String message, T results, ErrorResponse errors) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.results = results;
        this.errors = errors;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }

    public ErrorResponse getErrors() {
        return errors;
    }

    public void setErrors(ErrorResponse errors) {
        this.errors = errors;
    }

}
