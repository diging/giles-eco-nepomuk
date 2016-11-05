package edu.asu.diging.gilesecosystem.nepomuk.service.handlers;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.diging.gilesecosystem.nepomuk.core.IFile;
import edu.asu.diging.gilesecosystem.nepomuk.exception.NepomukFileStorageException;
import edu.asu.diging.gilesecosystem.nepomuk.exception.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.nepomuk.files.IFileStorageManager;
import edu.asu.diging.gilesecosystem.nepomuk.files.IFilesDatabaseClient;
import edu.asu.diging.gilesecosystem.nepomuk.files.IFilesManager;
import edu.asu.diging.gilesecosystem.nepomuk.service.IFileTypeHandler;

public abstract class AbstractFileHandler implements IFileTypeHandler {
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IFilesManager filesManager;
    
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
            logger.error("Could not read file.", e);
            return null;
        }
    }
    
    public IFile processFile(IFile file, byte[] content) throws NepomukFileStorageException {
        getStorageManager().saveFile(file.getUsername(), file.getUploadId(),
                file.getDocumentId(), file.getFilename(), content);
        
        try {
            filesManager.saveFile(file);
        } catch (UnstorableObjectException e) {
            logger.error("Could not store file.", e);
            return null;
        }
        return file;
    }
    
    protected abstract IFileStorageManager getStorageManager();
    
}

