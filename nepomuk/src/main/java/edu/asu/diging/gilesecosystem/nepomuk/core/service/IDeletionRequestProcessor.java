package edu.asu.diging.gilesecosystem.nepomuk.core.service;

import edu.asu.diging.gilesecosystem.requests.IStorageDeletionRequest;

public interface IDeletionRequestProcessor {
    /**
     * Processes a storage deletion request by deleting associated files and generating a completed storage deletion request.
     * @param request The storage deletion request to be processed.
     */
    void processRequest(IStorageDeletionRequest request);
}
