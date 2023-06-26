package edu.asu.diging.gilesecosystem.nepomuk.core.service.impl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.gilesecosystem.nepomuk.core.exception.NepomukFileStorageException;
import edu.asu.diging.gilesecosystem.nepomuk.core.files.IFilesManager;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.IFileHandlerRegistry;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.IFileTypeHandler;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.IRequestProcessor;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.properties.Properties;
import edu.asu.diging.gilesecosystem.requests.ICompletedStorageDeletionRequest;
import edu.asu.diging.gilesecosystem.requests.ICompletedStorageRequest;
import edu.asu.diging.gilesecosystem.requests.IRequestFactory;
import edu.asu.diging.gilesecosystem.requests.IStorageDeletionRequest;
import edu.asu.diging.gilesecosystem.requests.exceptions.MessageCreationException;
import edu.asu.diging.gilesecosystem.requests.impl.CompletedStorageDeletionRequest;
import edu.asu.diging.gilesecosystem.requests.impl.CompletedStorageRequest;
import edu.asu.diging.gilesecosystem.requests.impl.StorageDeletionRequest;
import edu.asu.diging.gilesecosystem.requests.kafka.IRequestProducer;
import edu.asu.diging.gilesecosystem.septemberutil.service.ISystemMessageHandler;
import edu.asu.diging.gilesecosystem.util.properties.IPropertiesManager;
import edu.asu.diging.gilesecosystem.nepomuk.core.model.IFile;
import edu.asu.diging.gilesecosystem.nepomuk.core.model.impl.File;

public class RequestProcessorTest {
    
    @Mock
    private IFileHandlerRegistry fileHandlerRegistry;
    
    @Mock
    private IPropertiesManager propertiesManager;
    
    @Mock
    private ISystemMessageHandler messageHandler;
    
    @Mock
    private IRequestProducer requestProducer;
    
    @Mock
    private IRequestFactory<ICompletedStorageDeletionRequest, CompletedStorageDeletionRequest> deletionRequestFactory;
    
    @Mock
    private IFilesManager filesManager;
    
    @Mock
    private IRequestFactory<ICompletedStorageRequest, CompletedStorageRequest> requestFactory;
    
    @Mock
    private IFileTypeHandler fileTypeHandler;
    
    @InjectMocks
    private IRequestProcessor requestProcessor;
    
    IFile file1, file2;
    IStorageDeletionRequest storageDeletionRequest;
    ICompletedStorageDeletionRequest completedStorageDeletionRequest;
    IFileTypeHandler handler;
    
    private String FILE_ID_1 = "fileId1";
    private String FILE_ID_2= "fileId2";
    private String GILES_ID_1 = "fileIdGiles1";
    private String GILES_ID_2= "fileIdGiles2";
    private String CONTENT_TYPE = "contentType";
    private String DOCUMENT_ID = "documentId";
    private String FILENAME = "filename";
    private long SIZE = 1000;
    private String UPLOAD_DATE = "date";
    private String UPLOAD_ID = "uploadId";
    
    @Before
    public void setUp() throws NepomukFileStorageException, InstantiationException, IllegalAccessException {
        requestProcessor = new RequestProcessor();
        MockitoAnnotations.initMocks(this);
        file1 = createFile(FILE_ID_1, GILES_ID_1);
        file2 = createFile(FILE_ID_2, GILES_ID_2);
        ArrayList<IFile> files = new ArrayList<IFile>();
        files.add(file1);
        storageDeletionRequest = createStorageDeletionRequest();
        completedStorageDeletionRequest = createCompletedStorageDeletionRequest();
        handler = mock(IFileTypeHandler.class);
        Mockito.when(filesManager.getFilesByDocumentId(file1.getDocumentId())).thenReturn(files);
        Mockito.when(propertiesManager.getProperty(Properties.KAFKA_TOPIC_STORAGE_DELETE_COMPLETE_REQUEST)).thenReturn("topic_delete_storage_request_complete");
        Mockito.when(fileHandlerRegistry.getHandler(file1.getFileType())).thenReturn(handler);
        Mockito.when(deletionRequestFactory.createRequest("REQ123", UPLOAD_ID)).thenReturn(completedStorageDeletionRequest);
    }
    
    @Test
    public void test_processRequest_allFilesDeleted_success() throws NepomukFileStorageException, MessageCreationException {
        Mockito.when(filesManager.getFilesByDocumentId(file1.getDocumentId())).thenReturn(new ArrayList<IFile>());
        doNothing().when(handler).deleteFile(file1);
        requestProcessor.processRequest(storageDeletionRequest);
        Mockito.verify(requestProducer, times(1)).sendRequest(completedStorageDeletionRequest, "topic_delete_storage_request_complete");
    }
    
    @Test(expected=NepomukFileStorageException.class)
    public void test_processRequest_deletionFailure_throwsNepomukFileStorageException() throws MessageCreationException, NepomukFileStorageException {
        Mockito.doThrow(new NepomukFileStorageException()).when(handler).deleteFile(Mockito.any());
        requestProcessor.processRequest(storageDeletionRequest);
        Mockito.verify(requestProducer, times(0)).sendRequest(completedStorageDeletionRequest, "topic_delete_storage_request_complete");
    }
    
    @Test
    public void test_processRequest_multipleFilesToDelete_success() throws MessageCreationException, NepomukFileStorageException {
        doNothing().when(handler).deleteFile(file1);
        doNothing().when(handler).deleteFile(file2);
        ArrayList<IFile> files = new ArrayList<IFile>();
        files.add(file2);
        files.add(file1);
        Mockito.when(filesManager.getFilesByDocumentId(storageDeletionRequest.getDocumentId())).thenReturn(files);
        requestProcessor.processRequest(storageDeletionRequest);
        Mockito.verify(requestProducer, times(1)).sendRequest(completedStorageDeletionRequest, "topic_delete_storage_request_complete");
    }
    
    private File createFile(String fileId, String gilesId) {
        File file = new File();
        file.setId(fileId);
        file.setContentType(CONTENT_TYPE);
        file.setDocumentId(DOCUMENT_ID);
        file.setFilename(FILENAME);
        file.setSize(SIZE);
        file.setUploadDate(UPLOAD_DATE);
        file.setUploadId(UPLOAD_ID);
        file.setUsername("github_3123");
        file.setGilesFileId(gilesId);
        return file;
    }
    
    private StorageDeletionRequest createStorageDeletionRequest() {
        StorageDeletionRequest storageDeletionRequest = new StorageDeletionRequest();
        storageDeletionRequest.setRequestId("REQ123");
        storageDeletionRequest.setUploadId(UPLOAD_ID);
        storageDeletionRequest.setDocumentId(DOCUMENT_ID);
        
        return storageDeletionRequest;
    }
    
    private CompletedStorageDeletionRequest createCompletedStorageDeletionRequest() {
        CompletedStorageDeletionRequest completedStorageDeletionRequest = new CompletedStorageDeletionRequest();
        completedStorageDeletionRequest.setRequestId("REQ123");
        completedStorageDeletionRequest.setUploadId(UPLOAD_ID);

        return completedStorageDeletionRequest;
    }
}
