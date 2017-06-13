package com.andrianova.url.shortener.model;

/**
 * Created by natal on 25-May-17.
 */
public class RegisteredUrl {
    private String url;
    private Integer redirectType;

    public RegisteredUrl() {
    }

    public RegisteredUrl(String url) {
        this.url = url;
    }

    public RegisteredUrl(String url, Integer redirectType) {
        this(url);
        this.redirectType = redirectType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(Integer redirectType) {
        this.redirectType = redirectType;
    }

    @Override
    public String toString() {
        return "RegisteredUrl{" +
                "url='" + url + '\'' +
                ", redirectType='" + redirectType + '\'' +
                '}';
    }
}
