package edu.asu.diging.gilesecosystem.nepomuk.core.aspect.tokens;

import java.io.IOException;
import java.security.GeneralSecurityException;

import edu.asu.diging.gilesecosystem.nepomuk.core.aspect.CheckerResult;
import edu.asu.diging.gilesecosystem.nepomuk.core.exception.InvalidTokenException;
import edu.asu.diging.gilesecosystem.nepomuk.core.exception.ServerMisconfigurationException;

public interface IChecker {
    
    public String getId();

    public CheckerResult validateToken(String token, String appId)
            throws GeneralSecurityException, IOException, InvalidTokenException, ServerMisconfigurationException;

}