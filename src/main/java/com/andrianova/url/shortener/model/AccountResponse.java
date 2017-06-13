package com.andrianova.url.shortener.model;

/**
 * Created by natal on 24-May-17.
 */
public class AccountResponse {
    private boolean success;
    private String description;
    private String password;

    public AccountResponse() {
    }

    public AccountResponse(boolean success, String description) {
        this.success = success;
        this.description = description;
    }

    public AccountResponse(boolean success, String description, String password) {
        this(success, description);
        this.password = password;
    }

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
