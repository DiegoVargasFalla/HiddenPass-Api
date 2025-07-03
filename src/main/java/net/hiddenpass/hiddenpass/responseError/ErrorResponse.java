package net.hiddenpass.hiddenpass.responseError;

import java.time.LocalDate;
import java.util.Objects;

public class ErrorResponse {

    private int status;
    private String message;
    private LocalDate date;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.date = LocalDate.now();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = Objects.requireNonNullElseGet(date, LocalDate::now);
    }
}
