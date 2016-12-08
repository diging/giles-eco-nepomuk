package edu.asu.diging.gilesecosystem.nepomuk.core.tokens.impl;

import edu.asu.diging.gilesecosystem.nepomuk.core.tokens.IAppToken;

public class AppToken implements IAppToken {

    private String id;
    private String token;
    private String appId;
    
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }
    @Override
    public String getAppId() {
        return appId;
    }
    @Override
    public void setAppId(String appId) {
        this.appId = appId;
    }

}
