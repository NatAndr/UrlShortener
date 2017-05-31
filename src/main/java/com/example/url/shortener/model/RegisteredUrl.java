package com.example.url.shortener.model;

/**
 * Created by natal on 25-May-17.
 */
public class RegisteredUrl {
    private String url;
    private String redirectType;

    public RegisteredUrl() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(String redirectType) {
        this.redirectType = redirectType;
    }
}
