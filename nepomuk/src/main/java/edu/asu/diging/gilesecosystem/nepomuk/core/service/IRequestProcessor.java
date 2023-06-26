package edu.asu.diging.gilesecosystem.nepomuk.core.service;

import edu.asu.diging.gilesecosystem.nepomuk.core.exception.NepomukFileStorageException;
import edu.asu.diging.gilesecosystem.requests.IStorageDeletionRequest;
import edu.asu.diging.gilesecosystem.requests.IStorageRequest;

public interface IRequestProcessor {

    public abstract void processRequest(IStorageRequest request);

    /**
     * Processes a storage deletion request.
     * @param request The storage deletion request to process.
     * @throws NepomukFileStorageException If an error occurs during the storage deletion process.
     */
    public abstract void processRequest(IStorageDeletionRequest request) throws NepomukFileStorageException;

}
