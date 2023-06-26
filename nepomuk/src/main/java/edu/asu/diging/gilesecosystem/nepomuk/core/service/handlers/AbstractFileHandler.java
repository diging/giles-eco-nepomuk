package edu.asu.diging.gilesecosystem.nepomuk.core.service.handlers;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.diging.gilesecosystem.nepomuk.core.exception.NepomukFileStorageException;
import edu.asu.diging.gilesecosystem.nepomuk.core.exception.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.nepomuk.core.files.IFileStorageManager;
import edu.asu.diging.gilesecosystem.nepomuk.core.files.IFilesManager;
import edu.asu.diging.gilesecosystem.nepomuk.core.model.IFile;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.IFileTypeHandler;
import edu.asu.diging.gilesecosystem.septemberutil.properties.MessageType;
import edu.asu.diging.gilesecosystem.septemberutil.service.ISystemMessageHandler;

public abstract class AbstractFileHandler implements IFileTypeHandler {
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IFilesManager filesManager;

    @Autowired
    private ISystemMessageHandler messageHandler;
    
    protected byte[] getFileContentFromUrl(URL url) throws IOException {
        URLConnection con = url.openConnection();
        
        InputStream input = con.getInputStream();

        byte[] buffer = new byte[4096];
        
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        BufferedOutputStream output = new BufferedOutputStream(byteOutput);
       
        int n = -1;
        while ((n = input.read(buffer)) != -1) {
            output.write(buffer, 0, n);
        }
        input.close();
        output.flush();
        output.close();
        
        byteOutput.flush();
        byte[] bytes = byteOutput.toByteArray();
        byteOutput.close();
        return bytes;
    }
    
    @Override
    public byte[] getFileContent(IFile file) {
        String folderPath = getStorageManager().getAndCreateStoragePath(file.getUsername(), file.getUploadId(), file.getDocumentId());
        File fileObject = new File(folderPath + File.separator + file.getFilename());
        try {
            return getFileContentFromUrl(fileObject.toURI().toURL());
        } catch (IOException e) {
            messageHandler.handleMessage("Could not read file.", e, MessageType.ERROR);
            return null;
        }
    }
    
    public IFile processFile(IFile file, byte[] content) throws NepomukFileStorageException {
        if (content == null) {
            messageHandler.handleMessage("File " + file.getGilesFileId() + " is empty.", "File " + file.getGilesFileId() + " does not have any content.", MessageType.WARNING);
            // let's store an empty file here
            content = "".getBytes();
        }
        
        Tika tika = new Tika();
        String contentType = tika.detect(content);
        file.setContentType(contentType);
         
        getStorageManager().saveFile(file.getUsername(), file.getUploadId(),
                file.getDocumentId(), file.getFilename(), content);
        
        try {
            filesManager.saveFile(file);
        } catch (UnstorableObjectException e) {
            messageHandler.handleMessage("Could not store file.", e, MessageType.ERROR);
            return null;
        }
        return file;
    }
    
    public String getRelativePathOfFile(IFile file) {
        String directory = getStorageManager().getFileFolderPathInBaseFolder(file.getUsername(), file.getUploadId(), file.getDocumentId());
        return directory + File.separator + file.getFilename();
    }
    
    @Override
    public String getRelativePathInTypeFolder(IFile file) {
        String directory = getStorageManager().getFileFolderPathInTypeFolder(file.getUsername(), file.getUploadId(), file.getDocumentId());
        return directory + File.separator + file.getFilename();
    }
    
    protected abstract IFileStorageManager getStorageManager();
    
    public void deleteFile(IFile file) throws NepomukFileStorageException {
        if (getStorageManager().checkIfFileExists(file.getUsername(), file.getUploadId(), file.getDocumentId(), file.getFilename())) {
            getStorageManager().deleteFile(file.getUsername(), file.getUploadId(), file.getDocumentId(), file.getFilename(), true);
        }
        // files manager will be called to delete the file from the database. 
        // Even if the file does not exist in storage as we check above we need to remove the file's database entry as it can be an older file version.
        filesManager.deleteFile(file.getId());
    }
}

