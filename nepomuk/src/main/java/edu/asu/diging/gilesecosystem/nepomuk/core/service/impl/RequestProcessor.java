package edu.asu.diging.gilesecosystem.nepomuk.core.service.impl;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;

import javax.annotation.PostConstruct;

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

import edu.asu.diging.gilesecosystem.nepomuk.core.domain.IFile;
import edu.asu.diging.gilesecosystem.nepomuk.core.domain.impl.File;
import edu.asu.diging.gilesecosystem.nepomuk.core.exception.NepomukFileStorageException;
import edu.asu.diging.gilesecosystem.nepomuk.core.files.IFileStorageManager;
import edu.asu.diging.gilesecosystem.nepomuk.core.files.IFilesManager;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.IFileHandlerRegistry;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.IFileTypeHandler;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.IRequestProcessor;
import edu.asu.diging.gilesecosystem.util.properties.IPropertiesManager;
import edu.asu.diging.gilesecosystem.nepomuk.rest.FilesController;
import edu.asu.diging.gilesecosystem.requests.ICompletedStorageRequest;
import edu.asu.diging.gilesecosystem.requests.IRequestFactory;
import edu.asu.diging.gilesecosystem.requests.IStorageRequest;
import edu.asu.diging.gilesecosystem.requests.RequestStatus;
import edu.asu.diging.gilesecosystem.requests.exceptions.MessageCreationException;
import edu.asu.diging.gilesecosystem.requests.impl.CompletedStorageRequest;
import edu.asu.diging.gilesecosystem.requests.kafka.IRequestProducer;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.properties.Properties;

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
    
    @Autowired
    private IRequestProducer requestProducer;
    
    @Autowired
    private IRequestFactory<ICompletedStorageRequest, CompletedStorageRequest> requestFactory;

    @PostConstruct
    public void init() {
        requestFactory.config(CompletedStorageRequest.class);
    }            

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
        
        if (newFile != null) {
            ICompletedStorageRequest completedRequest;
            try {
                completedRequest = requestFactory.createRequest(request.getRequestId(), request.getUploadId());
            } catch (InstantiationException | IllegalAccessException e) {
                // this should never happen, so we just fail silently...
                logger.error("Could not create request.", e);
                return;
            }
            
            String restEndpoint = propertiesManager.getProperty(Properties.APP_BASE_URL) + propertiesManager.getProperty(Properties.REST_ENDPOINT_PREFIX);
            if (restEndpoint.endsWith("/")) {
                restEndpoint = restEndpoint.substring(0, restEndpoint.length()-1);
            }
            String fileEndpoint = restEndpoint + FilesController.GET_FILE_URL.replace(FilesController.FILE_ID_PLACEHOLDER, newFile.getId());
            
            completedRequest.setDocumentId(request.getDocumentId());
            completedRequest.setStoredFileId(newFile.getId());
            completedRequest.setFileId(request.getFileId());
            completedRequest.setFilename(request.getFilename());
            completedRequest.setFileType(request.getFileType());
            completedRequest.setStatus(RequestStatus.COMPLETE);
            completedRequest.setStorageDate(OffsetDateTime.now(ZoneId.of("UTC")).toString());
            completedRequest.setUploadDate(request.getUploadDate());
            completedRequest.setUsername(request.getUsername());
            completedRequest.setDownloadUrl(fileEndpoint);
            completedRequest.setDownloadPath(handler.getRelativePathOfFile(newFile));
            
            try {
                requestProducer.sendRequest(completedRequest, propertiesManager.getProperty(Properties.KAFKA_TOPIC_STORAGE_COMPLETE));
            } catch (MessageCreationException e) {
                logger.error("Could not send message.", e);
            }
        }
    }
    
    protected byte[] downloadFile(String url) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());    
        
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
        headers.set("Authorization", "token " + propertiesManager.getProperty(Properties.GILES_ACCESS_TOKEN));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);
        if(response.getStatusCode().equals(HttpStatus.OK)) {    
            return response.getBody();
        }
        return null;
    }
}
