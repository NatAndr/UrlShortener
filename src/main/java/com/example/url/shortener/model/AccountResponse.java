package com.example.url.shortener.model;

/**
 * Created by natal on 24-May-17.
 */
public class AccountResponse {
    private boolean success;
    private String description;
    private String password;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
