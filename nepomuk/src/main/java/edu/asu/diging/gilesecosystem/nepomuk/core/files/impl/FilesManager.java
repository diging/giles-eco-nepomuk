package edu.asu.diging.gilesecosystem.nepomuk.core.files.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.gilesecosystem.nepomuk.core.exception.NoUniqueResultException;
import edu.asu.diging.gilesecosystem.nepomuk.core.exception.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.nepomuk.core.files.IFilesDatabaseClient;
import edu.asu.diging.gilesecosystem.nepomuk.core.files.IFilesManager;
import edu.asu.diging.gilesecosystem.nepomuk.core.model.IFile;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.IFileHandlerRegistry;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.IFileTypeHandler;

@PropertySource("classpath:/config.properties")
@Service
@Transactional
public class FilesManager implements IFilesManager {

    private Logger logger = LoggerFactory.getLogger(FilesManager.class);

    @Autowired
    private IFilesDatabaseClient databaseClient;

    @Autowired
    private IFileHandlerRegistry fileHandlerRegistry;

    
    @Override
    public List<IFile> getFilesByUploadId(String uploadId) {
        return databaseClient.getFilesByUploadId(uploadId);
    }

    @Override
    public IFile getFile(String id) {
        if (id == null) {
            return null;
        }
        return databaseClient.getFileById(id);
    }

    @Override
    public IFile getFileByPath(String path) {
        List<IFile> files = databaseClient.getFilesByProperty("filepath", path);
        if (files == null || files.isEmpty()) {
            return null;
        }

        return files.get(0);
    }

    @Override
    public byte[] getFileContent(IFile file) {
        IFileTypeHandler handler = fileHandlerRegistry.getHandler(file
                .getFileType());
        return handler.getFileContent(file);
    }

    @Override
    public IFile saveFile(IFile file) throws UnstorableObjectException {
        if (file.getId() == null) {
            file.setId(databaseClient.generateId());
        }
        databaseClient.saveFile(file);
        return file;
    }

    @Override
    public IFile getFile(String uploadId, String documentId, String filename) throws NoUniqueResultException {
        return databaseClient.getFile(uploadId, documentId, filename);
    }
    
    @Override
    public String getRelativePathOfFile(IFile file) {
        IFileTypeHandler handler = fileHandlerRegistry.getHandler(file
                .getFileType());
        return handler.getRelativePathOfFile(file);
    }
    
    @Override
    public List<String> getKnownUsernames() {
        return databaseClient.getUsernames();
    }
    
    @Override
    public void deleteFile(String fileId) {
        databaseClient.deleteFile(fileId);
    }

}
