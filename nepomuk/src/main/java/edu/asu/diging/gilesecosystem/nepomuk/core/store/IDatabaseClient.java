package edu.asu.diging.gilesecosystem.nepomuk.core.store;

import edu.asu.diging.gilesecosystem.nepomuk.core.exception.UnstorableObjectException;


public interface IDatabaseClient<T extends IStorableObject> {

    public abstract String generateId();

    public abstract T store(T element) throws UnstorableObjectException;

    public abstract void delete(T element);

}