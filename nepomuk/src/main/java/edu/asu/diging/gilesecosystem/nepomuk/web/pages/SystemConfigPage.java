package edu.asu.diging.gilesecosystem.nepomuk.web.pages;

public class SystemConfigPage {

    private String nepomukUrl;
    private String gilesAccessToken;

    
    public String getNepomukUrl() {
        return nepomukUrl;
    }

    public void setNepomukUrl(String nepomukUrl) {
        this.nepomukUrl = nepomukUrl;
    }
    
    public String getGilesAccessToken() {
        return gilesAccessToken;
    }

    public void setGilesAccessToken(String gilesAccessToken) {
        this.gilesAccessToken = gilesAccessToken;
    }

}
