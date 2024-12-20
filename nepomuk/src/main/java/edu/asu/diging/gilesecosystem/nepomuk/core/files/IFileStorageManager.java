package edu.asu.diging.gilesecosystem.nepomuk.core.files;

import edu.asu.diging.gilesecosystem.nepomuk.core.exception.NepomukFileStorageException;


public interface IFileStorageManager {

    public abstract void saveFile(String username, String uploadId,
            String fileId, String filename, byte[] bytes)
            throws NepomukFileStorageException;

    /**
     * Method that returns the path of a file in the digilib folder structure.
     * Note, this method does not return an absolute path and it does not
     * include the digilib base directory.
     * 
     */
    public abstract String getFileFolderPathInTypeFolder(String username, String uploadId,
            String documentId);

    /**
     * Method to get the absolute path to a file directory. This method makes
     * sure that the path exists and all necessary directories are created.
     * 
     * @param username
     *            username of user who uploaded an image
     * @param uploadId
     *            id of upload a file was part of
     * @param documentId
     *            id of document
     * @return absolute path to the file directory
     */
    public abstract String getAndCreateStoragePath(String username,
            String uploadId, String documentId);

    public abstract String getFileFolderPathInBaseFolder(String username, String uploadId,
            String fileId);
    
    /**
     * Method to delete a file from storage
     * 
     * @param username
     *            username of user who uploaded an image
     * @param uploadId
     *            id of upload a file was part of
     * @param documentId
     *            id of document
     * @param fileId
     *            id of file
     * @param deleteEmptyFolders
     *            if empty folders need to be deleted 
     * @throws NepomukFileStorageException 
     */
    public abstract void deleteFile(String username, String uploadId, String documentId, String fileId) throws NepomukFileStorageException;

    /**
     * Checks if a file exists at the specified location.
     * @param username   The username associated with the file.
     * @param uploadId   The unique identifier for the upload.
     * @param documentId The identifier for the document.
     * @param fileName   The name of the file to check.
     * @return {@code true} if the file exists at the specified location, {@code false} otherwise.
     */
    public abstract boolean checkIfFileExists(String username, String uploadId, String documentId, String fileName);
}
