package edu.asu.diging.gilesecosystem.nepomuk.service.impl;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import edu.asu.diging.gilesecosystem.nepomuk.core.IFile;
import edu.asu.diging.gilesecosystem.nepomuk.core.impl.File;
import edu.asu.diging.gilesecosystem.nepomuk.exception.NepomukFileStorageException;
import edu.asu.diging.gilesecosystem.nepomuk.exception.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.nepomuk.files.IFileStorageManager;
import edu.asu.diging.gilesecosystem.nepomuk.files.IFilesDatabaseClient;
import edu.asu.diging.gilesecosystem.nepomuk.files.IFilesManager;
import edu.asu.diging.gilesecosystem.nepomuk.service.IFileHandlerRegistry;
import edu.asu.diging.gilesecosystem.nepomuk.service.IFileTypeHandler;
import edu.asu.diging.gilesecosystem.nepomuk.service.IRequestProcessor;
import edu.asu.diging.gilesecosystem.nepomuk.service.properties.IPropertiesManager;
import edu.asu.diging.gilesecosystem.requests.IStorageRequest;

@Service
public class RequestProcessor implements IRequestProcessor {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IFilesManager filesManager;
    
    @Autowired
    private IFileStorageManager fileStorageManager;
    
    @Autowired
    private IFileHandlerRegistry fileHandlerRegistry;
    
    @Autowired
    private IPropertiesManager propertiesManager;


    /* (non-Javadoc)
     * @see edu.asu.diging.gilesecosystem.nepomuk.service.impl.IRequestProcessor#processRequest(edu.asu.diging.gilesecosystem.requests.IStorageRequest)
     */
    @Override
    public void processRequest(IStorageRequest request) {
        IFileTypeHandler handler = fileHandlerRegistry.getHandler(request.getFileType());
        
        IFile newFile = new File();
        newFile.setDocumentId(request.getDocumentId());
        newFile.setUploadId(request.getUploadId());
        newFile.setUploadDate(request.getUploadDate());
        newFile.setUsername(request.getUsername());
        newFile.setFilename(request.getFilename());
        newFile.setFileType(request.getFileType());
        
        byte[] content = downloadFile(request.getDownloadUrl());
        try {
            newFile = handler.processFile(newFile, content);
        } catch (NepomukFileStorageException e) {
            logger.error("Could not store file.", e);
            // FIXME: do something appropriate here
            return;
        }
    }
    
    protected byte[] downloadFile(String url) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());    
        
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
        headers.set("Authorization", "token " + propertiesManager.getProperty(IPropertiesManager.GILES_ACCESS_TOKEN));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);
        if(response.getStatusCode().equals(HttpStatus.OK)) {    
            return response.getBody();
        }
        return null;
    }
}
