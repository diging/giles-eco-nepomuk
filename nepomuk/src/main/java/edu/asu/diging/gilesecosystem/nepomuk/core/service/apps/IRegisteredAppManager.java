package edu.asu.diging.gilesecosystem.nepomuk.core.service.apps;

import java.util.ArrayList;
import java.util.List;

import edu.asu.diging.gilesecosystem.nepomuk.core.apps.IRegisteredApp;
import edu.asu.diging.gilesecosystem.nepomuk.core.exception.TokenGenerationErrorException;
import edu.asu.diging.gilesecosystem.nepomuk.core.tokens.IAppToken;

public interface IRegisteredAppManager {

    public abstract IRegisteredApp storeApp(IRegisteredApp app);
    
    public abstract List<IRegisteredApp> getRegisteredApps();
    
    public abstract IAppToken createToken(IRegisteredApp app) throws TokenGenerationErrorException;

}