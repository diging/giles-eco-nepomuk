package edu.asu.diging.gilesecosystem.nepomuk.core.files.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.gilesecosystem.nepomuk.core.domain.IFile;
import edu.asu.diging.gilesecosystem.nepomuk.core.domain.impl.File;
import edu.asu.diging.gilesecosystem.nepomuk.core.exception.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.nepomuk.core.files.IFilesDatabaseClient;
import edu.asu.diging.gilesecosystem.nepomuk.core.store.objectdb.DatabaseClient;

@Component
public class FilesDatabaseClient extends DatabaseClient<IFile> implements
        IFilesDatabaseClient {

    @PersistenceContext(unitName="FilesPU")
    private EntityManager em;

    @PreDestroy
    public void preDestroy() {
        em.close();
    }
    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.giles.files.IFilesDatabaseClient#addFile(edu.asu.giles.core.impl
     * .File)
     */
    @Transactional
    @Override
    public IFile saveFile(IFile file) throws UnstorableObjectException {
       return store(file);
    }

    @Override
    public IFile getFileById(String id) {
        IFile file = new File();
        file.setId(id);
        return em.find(File.class, id);
    }

    @Override
    public List<IFile> getFilesByUploadId(String uploadId) {
        List<IFile> results = new ArrayList<IFile>();
        searchByProperty("uploadId", uploadId, File.class).forEach(f -> results.add((IFile)f));
        return results;
    }
    
    @Override
    public List<IFile> getFilesByUsername(String username) {
        List<IFile> results = new ArrayList<IFile>();
        searchByProperty("username", username, File.class).forEach(f -> results.add((IFile)f));
        return results;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.giles.files.IFilesDatabaseClient#getFile(java.lang.String)
     */
    @Override
    public IFile getFile(String filename) {
        List<? extends IFile> files = searchByProperty("filename", filename, File.class);
        if (files.isEmpty()) {
            return null;
        }
        return files.get(0);
    }

    @Override
    protected String getIdPrefix() {
        return "FILE";
    }

    @Override
    protected Object getById(String id) {
        return getFileById(id);
    }

    @Override
    protected EntityManager getClient() {
        return em;
    }

    @Override
    public List<IFile> getFilesByProperty(String propName, String propValue) {
        List<IFile> results = new ArrayList<IFile>();
        searchByProperty(propName, propValue, File.class).forEach(f -> results.add((IFile)f));
        return results;
    }

}
