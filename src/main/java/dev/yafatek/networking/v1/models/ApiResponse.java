package dev.yafatek.networking.v1.models;

import java.util.Objects;

public class ApiResponse<T> {
    private boolean status;
    private String code;
    private String message;
    private T body;

    public ApiResponse() {
    }

    public ApiResponse(boolean status, String code, String message, T body) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.body = body;
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

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiResponse<?> that = (ApiResponse<?>) o;
        return status == that.status &&
                Objects.equals(code, that.code) &&
                Objects.equals(message, that.message) &&
                Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, code, message, body);
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "status=" + status +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", body=" + body +
                '}';
    }
}
