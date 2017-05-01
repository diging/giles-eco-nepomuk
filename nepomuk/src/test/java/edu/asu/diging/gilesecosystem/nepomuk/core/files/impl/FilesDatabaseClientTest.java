package edu.asu.diging.gilesecosystem.nepomuk.core.files.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.gilesecosystem.nepomuk.core.exception.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.nepomuk.core.files.IFilesDatabaseClient;
import edu.asu.diging.gilesecosystem.nepomuk.core.model.IFile;
import edu.asu.diging.gilesecosystem.nepomuk.core.model.impl.File;
import junit.framework.Assert;

public class FilesDatabaseClientTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<File> typedQuery;

    @InjectMocks
    private IFilesDatabaseClient clientToTest;

    private final String ID = "id";
    private final String ID2 = "id2";
    private final String FILENAME = "filename";
    private final String UPLOAD_ID = "uploadId";
    private final String USERNAME = "username";
    private final String FILEPATH = "filepath";

    @Before
    public void setUp() {
        clientToTest = new FilesDatabaseClient();
        MockitoAnnotations.initMocks(this);

        File file = new File();
        file.setId(ID);
        file.setFilename(FILENAME);

        Mockito.when(em.find(File.class, ID)).thenReturn(file);
    }

    @Test
    public void test_saveFile_success() throws UnstorableObjectException {
        IFile file = new File();
        file.setId("id");

        clientToTest.saveFile(file);

        Mockito.verify(em).persist(file);
        Mockito.verify(em).flush();
    }

    @Test(expected = UnstorableObjectException.class)
    public void test_saveFile_noId() throws UnstorableObjectException {
        IFile file = new File();

        clientToTest.saveFile(file);
    }

    @Test
    public void test_getFileById_exists() {
        IFile file = clientToTest.getFileById(ID);
        Assert.assertEquals(ID, file.getId());
        Assert.assertEquals(FILENAME, file.getFilename());
    }

    @Test
    public void test_getFileById_doesNotExist() {
        IFile file = clientToTest.getFileById("notExisting");
        Assert.assertNull(file);
    }

    @Test
    public void test_getFilesByUploadId_success() {
        Mockito.when(
                em.createQuery(
                        "SELECT t FROM edu.asu.diging.gilesecosystem.nepomuk.core.model.impl.File t WHERE t.uploadId = '"
                                + UPLOAD_ID + "'", File.class)).thenReturn(typedQuery);

        File file = new File();
        file.setId(ID);
        file.setUploadId(UPLOAD_ID);

        File file2 = new File();
        file2.setId(ID2);
        file2.setUploadId(UPLOAD_ID);

        List<File> files = new ArrayList<>();
        files.add(file2);
        files.add(file);

        Mockito.when(typedQuery.getResultList()).thenReturn(files);

        List<IFile> storedFiles = clientToTest.getFilesByUploadId(UPLOAD_ID);
        Assert.assertEquals(2, storedFiles.size());

        List<String> ids = storedFiles.stream().map(f -> f.getId())
                .collect(Collectors.toList());
        Assert.assertTrue(ids.contains(ID));
        Assert.assertTrue(ids.contains(ID2));
        
        List<IFile> uploadIds = storedFiles.stream().filter(f -> f.getUploadId().equals(UPLOAD_ID))
                .collect(Collectors.toList());
        Assert.assertTrue("There are several upload ids in the list, but there shouldn't.", uploadIds.size() == 2);
    }

    @Test
    public void test_getFilesByUploadId_noResults() {
        Mockito.when(
                em.createQuery(
                        "SELECT t FROM edu.asu.diging.gilesecosystem.nepomuk.core.model.impl.File t WHERE t.uploadId = '"
                                + UPLOAD_ID + "'", File.class)).thenReturn(typedQuery);

        List<File> files = new ArrayList<>();
        Mockito.when(typedQuery.getResultList()).thenReturn(files);

        List<IFile> storedFiles = clientToTest.getFilesByUploadId(UPLOAD_ID);
        Assert.assertEquals(0, storedFiles.size());
    }

    @Test
    public void test_getFilesByUsername_success() {
        Mockito.when(
                em.createQuery(
                        "SELECT t FROM edu.asu.diging.gilesecosystem.nepomuk.core.model.impl.File t WHERE t.username = '"
                                + USERNAME + "'", File.class)).thenReturn(typedQuery);

        File file = new File();
        file.setId(ID);
        file.setUsername(USERNAME);

        File file2 = new File();
        file2.setId(ID2);
        file2.setUsername(USERNAME);

        List<File> files = new ArrayList<>();
        files.add(file2);
        files.add(file);

        Mockito.when(typedQuery.getResultList()).thenReturn(files);

        List<IFile> storedFiles = clientToTest.getFilesByUsername(USERNAME);
        Assert.assertEquals(2, storedFiles.size());

        List<String> ids = storedFiles.stream().map(f -> f.getId())
                .collect(Collectors.toList());
        Assert.assertTrue(ids.contains(ID));
        Assert.assertTrue(ids.contains(ID2));
        
        List<IFile> uploadIds = storedFiles.stream().filter(f -> f.getUsername().equals(USERNAME))
                .collect(Collectors.toList());
        Assert.assertTrue("There are several usernames in the list, but there shouldn't.", uploadIds.size() == 2);
    }
    
    @Test
    public void test_getFilesByUsername_noResults() {
        Mockito.when(
                em.createQuery(
                        "SELECT t FROM edu.asu.diging.gilesecosystem.nepomuk.core.model.impl.File t WHERE t.username = '"
                                + USERNAME + "'", File.class)).thenReturn(typedQuery);

        List<File> files = new ArrayList<>();
        
        Mockito.when(typedQuery.getResultList()).thenReturn(files);

        List<IFile> storedFiles = clientToTest.getFilesByUsername(USERNAME);
        Assert.assertEquals(0, storedFiles.size());
    }
    
    @Test
    public void test_getFile_success() {
        Mockito.when(
                em.createQuery(
                        "SELECT t FROM edu.asu.diging.gilesecosystem.nepomuk.core.model.impl.File t WHERE t.filename = '"
                                + FILENAME + "'", File.class)).thenReturn(typedQuery);
        
        File file = new File();
        file.setId(ID);
        file.setFilename(FILENAME);

        List<File> files = new ArrayList<>();
        files.add(file);

        Mockito.when(typedQuery.getResultList()).thenReturn(files);
        
        IFile retrievedFile = clientToTest.getFile(FILENAME);
        Assert.assertNotNull(retrievedFile);
        Assert.assertEquals(FILENAME, retrievedFile.getFilename());
        Assert.assertEquals(ID, retrievedFile.getId());
        
    }
    
    @Test
    public void test_getFile_noResult() {
        Mockito.when(
                em.createQuery(
                        "SELECT t FROM edu.asu.diging.gilesecosystem.nepomuk.core.model.impl.File t WHERE t.filename = '"
                                + FILENAME + "'", File.class)).thenReturn(typedQuery);
        
        List<File> files = new ArrayList<>();
       
        Mockito.when(typedQuery.getResultList()).thenReturn(files);
        
        IFile retrievedFile = clientToTest.getFile(FILENAME);
        Assert.assertNull(retrievedFile);
        
    }
    
    @Test
    public void test_getFilesByProperty_success() {
        Mockito.when(
                em.createQuery(
                        "SELECT t FROM edu.asu.diging.gilesecosystem.nepomuk.core.model.impl.File t WHERE t.filePath = '"
                                + FILEPATH + "'", File.class)).thenReturn(typedQuery);
        
        File file = new File();
        file.setId(ID);
        file.setFilepath(FILEPATH);

        File file2 = new File();
        file2.setId(ID2);
        file2.setFilepath(FILEPATH);

        List<File> files = new ArrayList<>();
        files.add(file2);
        files.add(file);

        Mockito.when(typedQuery.getResultList()).thenReturn(files);
       
        List<IFile> storedFiles = clientToTest.getFilesByProperty("filePath", FILEPATH);
        Assert.assertEquals(2, storedFiles.size());

        List<String> ids = storedFiles.stream().map(f -> f.getId())
                .collect(Collectors.toList());
        Assert.assertTrue(ids.contains(ID));
        Assert.assertTrue(ids.contains(ID2));
        
        List<IFile> filePaths = storedFiles.stream().filter(f -> f.getFilepath().equals(FILEPATH))
                .collect(Collectors.toList());
        Assert.assertTrue("There are several file paths in the list, but there shouldn't.", filePaths.size() == 2);
    }
    
    @Test
    public void test_getFilesByProperty_noResults() {
        Mockito.when(
                em.createQuery(
                        "SELECT t FROM edu.asu.diging.gilesecosystem.nepomuk.core.model.impl.File t WHERE t.filePath = '"
                                + FILEPATH + "'", File.class)).thenReturn(typedQuery);
        
        List<File> files = new ArrayList<>();
        
        Mockito.when(typedQuery.getResultList()).thenReturn(files);
       
        List<IFile> storedFiles = clientToTest.getFilesByProperty("filePath", FILEPATH);
        Assert.assertEquals(0, storedFiles.size());
    }
}
