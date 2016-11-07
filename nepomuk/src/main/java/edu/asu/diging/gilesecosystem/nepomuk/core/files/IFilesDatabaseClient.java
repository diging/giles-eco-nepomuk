package edu.asu.diging.gilesecosystem.nepomuk.core.files;

import java.util.List;

import edu.asu.diging.gilesecosystem.nepomuk.core.db4o.IDatabaseClient;
import edu.asu.diging.gilesecosystem.nepomuk.core.domain.IFile;
import edu.asu.diging.gilesecosystem.nepomuk.core.exception.UnstorableObjectException;

public interface IFilesDatabaseClient extends IDatabaseClient<IFile> {

    public abstract IFile saveFile(IFile file) throws UnstorableObjectException;

    public abstract IFile getFile(String filename);

    public abstract List<IFile> getFilesByExample(IFile file);

    public abstract List<IFile> getFilesByUploadId(String uploadId);

    public abstract IFile getFileById(String id);

    public abstract List<IFile> getFilesByUsername(String username);

}