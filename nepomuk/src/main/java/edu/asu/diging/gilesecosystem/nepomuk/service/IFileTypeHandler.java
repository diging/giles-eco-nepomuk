package edu.asu.diging.gilesecosystem.nepomuk.service;

import java.util.List;

import edu.asu.diging.gilesecosystem.nepomuk.core.IFile;
import edu.asu.diging.gilesecosystem.nepomuk.exception.NepomukFileStorageException;
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
}
