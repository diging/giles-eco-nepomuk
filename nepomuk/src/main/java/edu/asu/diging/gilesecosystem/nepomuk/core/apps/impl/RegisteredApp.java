package edu.asu.diging.gilesecosystem.nepomuk.core.apps.impl;

import java.util.List;

import edu.asu.diging.gilesecosystem.nepomuk.core.apps.IRegisteredApp;

public class RegisteredApp implements IRegisteredApp {

    private String id;
    private String name;
    private List<String> tokenIds;
    
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<String> getTokenIds() {
        return tokenIds;
    }

    @Override
    public void setTokenIds(List<String> tokenIds) {
        this.tokenIds = tokenIds;
    }     
    
}
