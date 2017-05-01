package edu.asu.diging.gilesecosystem.nepomuk.core.migrate.impl;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import edu.asu.diging.gilesecosystem.nepomuk.core.domain.IFile;
import edu.asu.diging.gilesecosystem.nepomuk.core.domain.impl.File;
import edu.asu.diging.gilesecosystem.nepomuk.core.exception.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.nepomuk.core.files.IFilesManager;
import edu.asu.diging.gilesecosystem.requests.FileType;

@Service
public class MigrateToSql {

    @PersistenceContext(unitName="FilesPU")
    private EntityManager em;
    
    @Autowired
    private IFilesManager filesManager;
    
    
    @Async
    public Future<MigrationResult> migrateUserData(String username) throws UnstorableObjectException {
        int fileCount = migrateFiles(username);
        
        return new AsyncResult<MigrationResult>(new MigrationResult(fileCount, ZonedDateTime.now()));
    }
    
    private int migrateFiles(String username) throws UnstorableObjectException {
        int counter = 0;
        List<IFile> files = new ArrayList<IFile>();
        TypedQuery<File> query = em.createQuery("SELECT t FROM " + File.class.getName()  + " t WHERE t.username = '" + username + "'", File.class);
        query.getResultList().forEach(x -> files.add(x));
        for (IFile file : files) {
            if (filesManager.getFile(file.getId()) == null) {
                filesManager.saveFile(mapFile(file));
                counter++;
            }
        }
        return counter;
    }
    
    private edu.asu.diging.gilesecosystem.nepomuk.core.model.IFile mapFile(IFile file) {
        edu.asu.diging.gilesecosystem.nepomuk.core.model.IFile newFile = new edu.asu.diging.gilesecosystem.nepomuk.core.model.impl.File();
        newFile.setContentType(file.getContentType());
        newFile.setDocumentId(file.getDocumentId());
        newFile.setFilename(file.getFilename());
        newFile.setFilepath(file.getFilepath());
        newFile.setId(file.getId());
        newFile.setSize(file.getSize());
        newFile.setUploadDate(file.getUploadDate());
        newFile.setUploadId(file.getUploadId());
        newFile.setUsername(file.getUsername());
        if (file.getFileType() != null) {
            newFile.setFileType(FileType.valueOf(file.getFileType().name()));
        }
        newFile.setGilesFileId(file.getGilesFileId());
        newFile.setProcessedDate(file.getProcessedDate());
        return newFile;
    }
}
