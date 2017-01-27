package edu.asu.diging.gilesecosystem.nepomuk.core.apps;

import java.util.List;

import edu.asu.diging.gilesecosystem.util.store.IStorableObject;

public interface IRegisteredApp extends IStorableObject {

    public abstract String getId();

    public abstract void setId(String id);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract List<String> getTokenIds();

    public abstract void setTokenIds(List<String> tokenIds);

}