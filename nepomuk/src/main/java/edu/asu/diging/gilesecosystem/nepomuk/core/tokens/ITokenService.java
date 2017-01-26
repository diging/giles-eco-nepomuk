package edu.asu.diging.gilesecosystem.nepomuk.core.tokens;

import edu.asu.diging.gilesecosystem.nepomuk.core.apps.IRegisteredApp;


public interface ITokenService {

    public abstract IAppToken generateAppToken(IRegisteredApp app);

}