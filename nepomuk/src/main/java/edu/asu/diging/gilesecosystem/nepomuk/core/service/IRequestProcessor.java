package edu.asu.diging.gilesecosystem.nepomuk.core.service;

import edu.asu.diging.gilesecosystem.requests.IStorageRequest;

public interface IRequestProcessor {

    public abstract void processRequest(IStorageRequest request);

}
