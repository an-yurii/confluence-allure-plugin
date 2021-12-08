package ru.yuriian;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class AccessTokenStorage {
    private String accessToken = null;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
