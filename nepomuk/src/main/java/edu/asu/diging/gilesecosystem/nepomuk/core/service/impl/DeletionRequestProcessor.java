package edu.asu.diging.gilesecosystem.nepomuk.core.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.diging.gilesecosystem.nepomuk.core.exception.NepomukFileStorageException;
import edu.asu.diging.gilesecosystem.nepomuk.core.files.IFilesManager;
import edu.asu.diging.gilesecosystem.nepomuk.core.model.IFile;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.IDeletionRequestProcessor;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.IFileHandlerRegistry;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.IFileTypeHandler;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.properties.Properties;
import edu.asu.diging.gilesecosystem.requests.ICompletedStorageDeletionRequest;
import edu.asu.diging.gilesecosystem.requests.IRequestFactory;
import edu.asu.diging.gilesecosystem.requests.IStorageDeletionRequest;
import edu.asu.diging.gilesecosystem.requests.RequestStatus;
import edu.asu.diging.gilesecosystem.requests.exceptions.MessageCreationException;
import edu.asu.diging.gilesecosystem.requests.impl.CompletedStorageDeletionRequest;
import edu.asu.diging.gilesecosystem.requests.kafka.IRequestProducer;
import edu.asu.diging.gilesecosystem.septemberutil.properties.MessageType;
import edu.asu.diging.gilesecosystem.septemberutil.service.ISystemMessageHandler;
import edu.asu.diging.gilesecosystem.util.properties.IPropertiesManager;

public class DeletionRequestProcessor implements IDeletionRequestProcessor {
    @Autowired
    private IFileHandlerRegistry fileHandlerRegistry;
    
    @Autowired
    private IPropertiesManager propertiesManager;
    
    @Autowired
    private IRequestProducer requestProducer;
    
    @Autowired
    private ISystemMessageHandler messageHandler;
    
    @Autowired
    private IFilesManager filesManager;

    @Autowired
    private IRequestFactory<ICompletedStorageDeletionRequest, CompletedStorageDeletionRequest> deletionRequestFactory;
    
    @PostConstruct
    public void init() {
        deletionRequestFactory.config(CompletedStorageDeletionRequest.class);
    }
    
    @Override
    public void processRequest(IStorageDeletionRequest request) {
        List<IFile> files = filesManager.getFilesByDocumentId(request.getDocumentId());
        RequestStatus status = RequestStatus.COMPLETE;
        for (IFile file : files) {
            IFileTypeHandler handler = fileHandlerRegistry.getHandler(file.getFileType());
            try {
                handler.deleteFile(file);
            } catch(NepomukFileStorageException ex) {
                messageHandler.handleMessage("Error deleting file with id " + file.getId(), ex, MessageType.ERROR);
                status = RequestStatus.FAILED;
            }          
        }
        ICompletedStorageDeletionRequest completedRequest;
        try {
            completedRequest = deletionRequestFactory.createRequest(request.getRequestId(), request.getUploadId());
        } catch (InstantiationException | IllegalAccessException e) {
            messageHandler.handleMessage("Request could not be created.", e, MessageType.ERROR);
            return;
        }
        completedRequest.setStatus(status);
        completedRequest.setDocumentId(request.getDocumentId());
        try {
            requestProducer.sendRequest(completedRequest, propertiesManager.getProperty(Properties.KAFKA_TOPIC_STORAGE_DELETE_COMPLETE_REQUEST));
        } catch (MessageCreationException e) {
            messageHandler.handleMessage("Request could not be send.", e, MessageType.ERROR);
        }
    }
}
