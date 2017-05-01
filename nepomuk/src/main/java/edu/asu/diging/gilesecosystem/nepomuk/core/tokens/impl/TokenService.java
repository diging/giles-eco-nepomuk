package edu.asu.diging.gilesecosystem.nepomuk.core.tokens.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.gilesecosystem.nepomuk.core.apps.IRegisteredApp;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.properties.Properties;
import edu.asu.diging.gilesecosystem.nepomuk.core.tokens.IAppToken;
import edu.asu.diging.gilesecosystem.nepomuk.core.tokens.ITokenService;
import edu.asu.diging.gilesecosystem.util.properties.IPropertiesManager;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Class to create new user tokens for access to the REST api.
 * 
 * @author Julia Damerow
 *
 */
@Service
public class TokenService implements ITokenService {
    
    @Autowired
    private IPropertiesManager propertiesManager;

    @Override
    public IAppToken generateAppToken(IRegisteredApp app) {
        String tokenId = UUID.randomUUID().toString();
        String compactJws = Jwts.builder()
                .setSubject(app.getName())
                .claim("appId", app.getId())
                .claim("tokenId", tokenId)
                .signWith(SignatureAlgorithm.HS256, propertiesManager.getProperty(Properties.SIGNING_KEY_APPS))
                .compact();
        
        IAppToken token = new AppToken();
        token.setToken(compactJws);
        token.setId(tokenId);
        token.setAppId(app.getId());
        return token;
    }
}
