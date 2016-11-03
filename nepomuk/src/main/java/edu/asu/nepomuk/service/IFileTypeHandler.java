package edu.asu.nepomuk.service;

import java.util.List;

import edu.asu.nepomuk.core.IDocument;
import edu.asu.nepomuk.core.IFile;
import edu.asu.nepomuk.core.IUpload;
import edu.asu.nepomuk.exception.NepomukFileStorageException;

public interface IFileTypeHandler {
    
    public static final String DEFAULT_HANDLER = "DEFAULT";

    /**
     * Returns a list of content types that this handler can
     * process.
     * @return
     */
    List<String> getHandledFileTypes();
    
    boolean processFile(String username, IFile file, IDocument document, IUpload upload, byte[] content) throws NepomukFileStorageException;

    String getRelativePathOfFile(IFile file);
    
    String getFileUrl(IFile file);
    
    byte[] getFileContent(IFile file);
}
