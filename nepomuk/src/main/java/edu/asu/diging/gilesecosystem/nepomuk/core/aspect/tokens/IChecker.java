package edu.asu.diging.gilesecosystem.nepomuk.core.aspect.tokens;

import edu.asu.diging.gilesecosystem.nepomuk.core.aspect.CheckerResult;
import edu.asu.diging.gilesecosystem.nepomuk.core.exception.InvalidTokenException;

public interface IChecker {
    
    public String getId();

    public CheckerResult validateToken(String token)
            throws InvalidTokenException;

}