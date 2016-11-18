package edu.asu.diging.gilesecosystem.nepomuk.core.apps;

import com.db4o.ObjectSet;

import edu.asu.diging.gilesecosystem.nepomuk.core.db4o.IDatabaseClient;
import edu.asu.diging.gilesecosystem.nepomuk.core.exception.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.nepomuk.core.apps.IRegisteredApp;

public interface IRegisteredAppDatabaseClient extends IDatabaseClient<IRegisteredApp> {
    
    public abstract IRegisteredApp getAppById(String id);
    
    public abstract void storeModifiedApp(IRegisteredApp app) throws UnstorableObjectException;

    public abstract IRegisteredApp[] getAllRegisteredApps();

}