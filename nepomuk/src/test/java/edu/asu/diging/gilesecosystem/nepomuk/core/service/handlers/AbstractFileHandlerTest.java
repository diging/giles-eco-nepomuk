package edu.asu.diging.gilesecosystem.nepomuk.core.service.handlers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.gilesecosystem.nepomuk.core.exception.NepomukFileStorageException;
import edu.asu.diging.gilesecosystem.nepomuk.core.files.IFileStorageManager;
import edu.asu.diging.gilesecosystem.nepomuk.core.files.IFilesManager;
import edu.asu.diging.gilesecosystem.nepomuk.core.model.IFile;
import edu.asu.diging.gilesecosystem.nepomuk.core.model.impl.File;
import edu.asu.diging.gilesecosystem.septemberutil.service.ISystemMessageHandler;

public class AbstractFileHandlerTest {
    @Mock
    private IFilesManager filesManager;
    
    @Mock
    private ISystemMessageHandler messageHandler;
    
    @Mock
    private IFileStorageManager fileStorageManager;
    
    @InjectMocks
    private AbstractFileHandler abstractFileHandler;
    
    IFile file;
    
    private String FILE_ID = "fileId1";
    private String GILES_ID = "fileIdGiles1";
    private String CONTENT_TYPE = "contentType";
    private String DOCUMENT_ID = "documentId";
    private String FILENAME = "filename";
    private long SIZE = 1000;
    private String UPLOAD_DATE = "date";
    private String UPLOAD_ID = "uploadId";
    
    @Before
    public void setUp() {
        abstractFileHandler = mock(AbstractFileHandler.class);
        MockitoAnnotations.initMocks(this);
        Mockito.when(abstractFileHandler.getStorageManager()).thenReturn(fileStorageManager);
        file = createFile(FILE_ID, GILES_ID);
    }
    
    @Test
    public void test_deleteFile_whenIsOldFileVersionFalse_success() {
        Mockito.doCallRealMethod().when(abstractFileHandler).deleteFile(file, false);
        abstractFileHandler.deleteFile(file, false);
        try {
            Mockito.verify(fileStorageManager, times(1)).deleteFile("github_3123", UPLOAD_ID, DOCUMENT_ID, FILENAME);
        } catch (NepomukFileStorageException e) {
            e.printStackTrace();
        }
        Mockito.verify(filesManager, times(1)).deleteFile(file.getId());
    }
    
    @Test
    public void test_deleteFile_whenIsOldFileVersionTrue_success() {
        Mockito.doCallRealMethod().when(abstractFileHandler).deleteFile(file, true);
        abstractFileHandler.deleteFile(file, true);
        try {
            Mockito.verify(fileStorageManager, times(0)).deleteFile("github_3123", UPLOAD_ID, DOCUMENT_ID, FILENAME);
        } catch (NepomukFileStorageException e) {
            e.printStackTrace();
        }
        Mockito.verify(filesManager, times(1)).deleteFile(file.getId());
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
    
}
