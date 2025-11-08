package org.springframework.samples.petclinic.rest.dto;

/**
 * Generic wrapper for statistics API responses.
 */
public class StatisticsResponseDTO<T> {
    private T data;
    private boolean success;
    private String message;

    public StatisticsResponseDTO() {
        this.success = true;
    }

    public StatisticsResponseDTO(T data) {
        this.data = data;
        this.success = true;
    }

    public StatisticsResponseDTO(T data, String message) {
        this.data = data;
        this.success = true;
        this.message = message;
    }

    public static <T> StatisticsResponseDTO<T> success(T data) {
        return new StatisticsResponseDTO<>(data);
    }

    public static <T> StatisticsResponseDTO<T> success(T data, String message) {
        return new StatisticsResponseDTO<>(data, message);
    }

    public static <T> StatisticsResponseDTO<T> error(String message) {
        StatisticsResponseDTO<T> response = new StatisticsResponseDTO<>();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
