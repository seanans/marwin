package com.marwin.customerservice.shared;

public class ApiResponse<T>{
    private T data;
    private String errorMessage;

    public ApiResponse(T data, String errorMessage) {
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public ApiResponse(T data) {
        this.data = data;
    }

    public ApiResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ApiResponse() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setData(data);
        return response;
    }

    public static  <T> ApiResponse<T> error(String errorMessage) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setErrorMessage(errorMessage);
        return response;
    }
    public boolean isSuccess() {
        return data != null;
    }

    public boolean hasError() {
        return errorMessage != null;
    }

}
