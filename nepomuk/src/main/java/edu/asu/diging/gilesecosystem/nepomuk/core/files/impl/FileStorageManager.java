package edu.asu.diging.gilesecosystem.nepomuk.core.files.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

import edu.asu.diging.gilesecosystem.nepomuk.core.exception.NepomukFileStorageException;
import edu.asu.diging.gilesecosystem.nepomuk.core.files.IFileStorageManager;

@Service
public class FileStorageManager implements IFileStorageManager {

    private String baseDirectory;
    
    private String fileTypeFolder;

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.giles.files.impl.IFileSystemManager#saveFile(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, byte[])
     */
    @Override
    public void saveFile(String username, String uploadId, String documentId,
            String filename, byte[] bytes) throws NepomukFileStorageException {
        String filePath = getAndCreateStoragePath(username, uploadId,
                documentId);

        File file = new File(filePath + File.separator + filename);
        BufferedOutputStream stream;
        try {
            stream = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new NepomukFileStorageException("Could not store file.", e);
        }
        try {
            stream.write(bytes);
            stream.close();
        } catch (IOException e) {
            throw new NepomukFileStorageException("Could not store file.", e);
        }
    }

    @Override
    public String getAndCreateStoragePath(String username, String uploadId,
            String documentId) {
        String path = getStoragePath(username, uploadId, documentId);
        createDirectory(path);
        return path;
    }

    /**
     * This method returns the path to a file relative to the file type folder,
     * which is inside the base folder. This method should be used when creating 
     * paths for Digilib (assuming that Digilib is configured to read from the 
     * image folder).
     */
    @Override
    public String getFileFolderPathInTypeFolder(String username, String uploadId,
            String fileId) {
        StringBuffer filePath = new StringBuffer();
        filePath.append(username);
        filePath.append(File.separator);
        filePath.append(uploadId);
        filePath.append(File.separator);
        filePath.append(fileId);

        return filePath.toString();
    }
    
    /**
     * This method returns the path to requested file relative to the base folder
     * for all uploaded files (including the file type folder). This method should be used
     * in most cases.
     * 
     * @param username
     * @param uploadId
     * @param fileId
     * @return
     */
    @Override
    public String getFileFolderPathInBaseFolder(String username, String uploadId,
            String fileId) {
        StringBuffer filePath = new StringBuffer();
        filePath.append(fileTypeFolder);
        filePath.append(File.separator);
        filePath.append(username);
        filePath.append(File.separator);
        filePath.append(uploadId);
        filePath.append(File.separator);
        filePath.append(fileId);
        
        return filePath.toString();
    }

    public String getBaseDirectory() {
        return baseDirectory;
    }

    public void setBaseDirectory(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    private boolean createDirectory(String dirPath) {

        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return true;
    }

    public void setFileTypeFolder(String fileTypeFolder) {
        this.fileTypeFolder = fileTypeFolder;
    }
    
    private String getStoragePath(String username, String uploadId,
            String documentId) {
        return baseDirectory + File.separator 
                + getFileFolderPathInBaseFolder(username, uploadId, documentId);
    }
    
    public void deleteFile(String username, String uploadId, String documentId, String fileName) throws NepomukFileStorageException {
        String originalFilePath = getStoragePath(username, uploadId, documentId);
        Path path = Paths.get(originalFilePath + File.separator + fileName);
        try {
            Files.delete(path);
        } catch (IOException ex) {
            throw new NepomukFileStorageException("Could not delete file.", ex);
        }
        String documentFolder = getStoragePath(username, uploadId, documentId);
        File docFolder = new File(documentFolder);
        if (docFolder.isDirectory() && docFolder.list().length == 0) {
            // If another process adds a file to the folder just before deletion, we do not want to delete the folder.
            // The goal is to ensure that the folder remains intact even if new files are added during the deletion process.
            // Using the File.delete() instead of Files.delete(path) as an exception is not required to be thrown in this scenario.
            boolean deleted = docFolder.delete();
            if (deleted) {
                Path documentFolderPath = Paths.get(documentFolder);
                Path uploadFolderDirectory = documentFolderPath.getParent();
                File uploadFolder = new File(uploadFolderDirectory.toString());
                if (uploadFolder.isDirectory() && uploadFolder.list().length == 0) {
                    uploadFolder.delete();
                }
            }
        }
    }

    @Override
    public boolean checkIfFileExists(String username, String uploadId, String documentId, String fileName) {
        Path path = Paths.get(getStoragePath(username, uploadId, documentId) + File.separator + fileName);
        return Files.exists(path);
    }
}
