package edu.asu.diging.gilesecosystem.nepomuk.core.files;

import java.util.List;

import edu.asu.diging.gilesecosystem.nepomuk.core.exception.NoUniqueResultException;
import edu.asu.diging.gilesecosystem.nepomuk.core.exception.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.nepomuk.core.model.IFile;

public interface IFilesManager {

    /**
     * Get specified page of upload query. If pageSize is -1, default page size is 
     * used. This method makes sure that only valid page numbers are used. If page 
     * is smaller than 1, it is set to 1 before querying the database. If page is 
     * greater than the max page count, it is set to the last page.
     * 
     *  @param username Username of the user that uploads belong to
     *  @param page number of page that should be retrieved
     *  @param pageSize number of results per page. If -1, then the default page size is used.
     */
    
    public abstract List<IFile> getFilesByUploadId(String uploadId);

    public abstract IFile getFile(String id);

    public abstract IFile saveFile(IFile file) throws UnstorableObjectException;

    public abstract IFile getFileByPath(String path);

    public abstract String getRelativePathOfFile(IFile file);

    public abstract byte[] getFileContent(IFile file);

    public abstract IFile getFile(String uploadId, String documentId, String filename)
            throws NoUniqueResultException;

    List<String> getKnownUsernames();
    
    /**
     * Delete a file given the file ID.
     * @param fileId 
     *         ID of the file to be deleted
     */

    public abstract void deleteFile(String fileId);
}
