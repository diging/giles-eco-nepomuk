package edu.asu.diging.gilesecosystem.nepomuk.core.tokens.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.gilesecosystem.nepomuk.core.apps.IRegisteredApp;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.properties.Properties;
import edu.asu.diging.gilesecosystem.util.properties.IPropertiesManager;
import edu.asu.diging.gilesecosystem.nepomuk.core.tokens.IAppToken;
import edu.asu.diging.gilesecosystem.nepomuk.core.tokens.ITokenService;

/**
 * Class to create new user tokens for access to the REST api.
 * 
 * @author Julia Damerow
 *
 */
@Service
public class TokenService implements ITokenService {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IPropertiesManager propertiesManager;

    @Override
    public IAppToken generateAppToken(IRegisteredApp app) {
        String tokenId = UUID.randomUUID().toString();
        String compactJws = Jwts.builder()
                .setSubject(app.getName())
                .claim("appId", app.getId())
                .claim("tokenId", tokenId)
                .signWith(SignatureAlgorithm.HS256, propertiesManager.getProperty(Properties.NEPOMUK_SIGNING_KEY))
                .compact();
        
        IAppToken token = new AppToken();
        token.setToken(compactJws);
        token.setId(tokenId);
        token.setAppId(app.getId());
        return token;
    }
    
    @Override
    public IAppToken getAppTokenContents(String token) {
        IAppToken appToken = new AppToken();
        
        try {
            Jws<Claims> jws = Jwts.parser().setSigningKey(propertiesManager.getProperty(Properties.NEPOMUK_SIGNING_KEY)).parseClaimsJws(token);
            Claims claims = jws.getBody(); 
            
            appToken.setAppId(claims.get("appId", String.class));
            appToken.setId(claims.get("tokenId", String.class));
        
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // currently app tokens don't expire, so we'll just return null
            return null;
        } catch (SignatureException e) {
            return null;
        } 
        
        return appToken;
    }
}
