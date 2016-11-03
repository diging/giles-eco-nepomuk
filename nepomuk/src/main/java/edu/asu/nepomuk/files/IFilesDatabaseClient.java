package edu.asu.nepomuk.files;

import java.util.List;

import edu.asu.nepomuk.core.IFile;
import edu.asu.nepomuk.db4o.IDatabaseClient;
import edu.asu.nepomuk.exception.UnstorableObjectException;

public interface IFilesDatabaseClient extends IDatabaseClient<IFile> {

    public abstract IFile saveFile(IFile file) throws UnstorableObjectException;

    public abstract IFile getFile(String filename);

    public abstract List<IFile> getFilesByExample(IFile file);

    public abstract List<IFile> getFilesByUploadId(String uploadId);

    public abstract IFile getFileById(String id);

    public abstract List<IFile> getFilesByUsername(String username);

}