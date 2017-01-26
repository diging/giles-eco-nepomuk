package edu.asu.diging.gilesecosystem.nepomuk.core.tokens.impl;

import edu.asu.diging.gilesecosystem.nepomuk.core.tokens.IApiTokenContents;

/**
 * Class that holds the information that was encoded in a token.
 * 
 * @author Julia Damerow
 *
 */
public class ApiTokenContents implements IApiTokenContents {

    private String username;
    private boolean expired;
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isExpired() {
        return expired;
    }

    @Override
    public void setExpired(boolean expired) {
        this.expired = expired;
    }
     
}
