package edu.asu.diging.gilesecosystem.nepomuk.core.files;

import java.util.List;

import javax.persistence.TypedQuery;

import edu.asu.diging.gilesecosystem.nepomuk.core.exception.NoUniqueResultException;
import edu.asu.diging.gilesecosystem.nepomuk.core.exception.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.nepomuk.core.model.IFile;
import edu.asu.diging.gilesecosystem.nepomuk.core.model.impl.File;
import edu.asu.diging.gilesecosystem.nepomuk.core.store.IDatabaseClient;

public interface IFilesDatabaseClient extends IDatabaseClient<IFile> {

    public abstract IFile saveFile(IFile file) throws UnstorableObjectException;

    public abstract IFile getFile(String filename);

    public abstract List<IFile> getFilesByUploadId(String uploadId);

    public abstract IFile getFileById(String id);

    public abstract List<IFile> getFilesByUsername(String username);
    
    public List<IFile> getFilesByProperty(String propName, String propValue);

    public abstract IFile getFile(String uploadId, String documentId, String filename)
            throws NoUniqueResultException;

    List<String> getUsernames();

    /**
     * Delete a file given the file ID.
     * @param fileId 
     *         ID of the file to be deleted
     */
    public abstract void deleteFile(String fileId);
}
