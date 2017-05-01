package edu.asu.diging.gilesecosystem.nepomuk.core.files.impl;

import edu.asu.diging.gilesecosystem.nepomuk.core.exception.NepomukFileStorageException;
import edu.asu.diging.gilesecosystem.nepomuk.core.model.IFile;

public class StorageStatus {

    public final static int SUCCESS = 0;
    public final static int FAILURE = 1;

    private IFile file;
    private NepomukFileStorageException exception;
    private int status;

    public StorageStatus(IFile file, NepomukFileStorageException exception,
            int status) {
        super();
        this.file = file;
        this.exception = exception;
        this.status = status;
    }

    public IFile getFile() {
        return file;
    }

    public void setFile(IFile file) {
        this.file = file;
    }

    public NepomukFileStorageException getException() {
        return exception;
    }

    public void setException(NepomukFileStorageException exception) {
        this.exception = exception;
    }

    /**
     * Returns if an upload was successful.
     * 
     * @return 0 = SUCCESS, 1 = FAILURE
     */
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
