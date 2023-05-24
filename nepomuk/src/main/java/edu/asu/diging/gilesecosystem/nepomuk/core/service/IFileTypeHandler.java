package edu.asu.diging.gilesecosystem.nepomuk.core.service;

import java.util.List;

import edu.asu.diging.gilesecosystem.nepomuk.core.exception.NepomukFileStorageException;
import edu.asu.diging.gilesecosystem.nepomuk.core.model.IFile;
import edu.asu.diging.gilesecosystem.requests.FileType;

public interface IFileTypeHandler {
    
    /**
     * Returns a list of content types that this handler can
     * process.
     * @return
     */
    List<FileType> getHandledFileTypes();
    
    IFile processFile(IFile file, byte[] content) throws NepomukFileStorageException;

    String getRelativePathOfFile(IFile file);
     
    byte[] getFileContent(IFile file);

    public abstract String getRelativePathInTypeFolder(IFile file);
    
    /**
     * This method deletes a file.
     * @param file 
     *        File to be deleted.
     */
    public abstract void deleteFile(IFile file); 
}
