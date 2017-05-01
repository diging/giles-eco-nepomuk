package edu.asu.diging.gilesecosystem.nepomuk.core.service.apps.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.gilesecosystem.nepomuk.core.apps.IRegisteredApp;
import edu.asu.diging.gilesecosystem.nepomuk.core.apps.IRegisteredAppDatabaseClient;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.apps.IRegisteredAppManager;
import edu.asu.diging.gilesecosystem.nepomuk.core.exception.TokenGenerationErrorException;
import edu.asu.diging.gilesecosystem.util.exceptions.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.nepomuk.core.tokens.IAppToken;
import edu.asu.diging.gilesecosystem.nepomuk.core.tokens.ITokenService;
import edu.asu.diging.gilesecosystem.septemberutil.properties.MessageType;
import edu.asu.diging.gilesecosystem.septemberutil.service.ISystemMessageHandler;

@Transactional
@Service
public class RegisteredAppsManager implements IRegisteredAppManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IRegisteredAppDatabaseClient databaseClient;
    
    @Autowired
    private ITokenService tokenService;

    @Autowired
    private ISystemMessageHandler messageHandler;

    @Override
    public IRegisteredApp storeApp(IRegisteredApp app) {

        String id = databaseClient.generateId();
        app.setId(id);

        try {
            databaseClient.store(app);
        } catch (UnstorableObjectException e) {
            messageHandler.handleMessage("Could not store app.", e, MessageType.ERROR);
            return null;
        }
        return app;
    }
    
    @Override
    public IRegisteredApp getApp(String id) {
        return databaseClient.getAppById(id);
    }
    
    @Override
    public void deleteApp(String id) {
        IRegisteredApp app = getApp(id);
        databaseClient.delete(app);
    }
    
    @Override
    public List<IRegisteredApp> getRegisteredApps() {
        IRegisteredApp[] apps = databaseClient.getAllRegisteredApps();
        return Arrays.asList(apps);
    }
    
    @Override
    public IAppToken createToken(IRegisteredApp app) throws TokenGenerationErrorException {
        IAppToken token = tokenService.generateAppToken(app);
        
        if (app.getTokenIds() == null) {
            app.setTokenIds(new ArrayList<String>());
        }
        
        app.getTokenIds().add(token.getId());
        try {
            databaseClient.storeModifiedApp(app);
        } catch (UnstorableObjectException e) {
            throw new TokenGenerationErrorException("Token was generated but app could not be stored.", e);
        }
        
        return token;
    }
}
