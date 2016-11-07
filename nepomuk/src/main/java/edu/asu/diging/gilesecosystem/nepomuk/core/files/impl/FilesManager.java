package edu.asu.diging.gilesecosystem.nepomuk.core.files.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import edu.asu.diging.gilesecosystem.nepomuk.core.domain.IFile;
import edu.asu.diging.gilesecosystem.nepomuk.core.exception.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.nepomuk.core.files.IFilesDatabaseClient;
import edu.asu.diging.gilesecosystem.nepomuk.core.files.IFilesManager;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.IFileHandlerRegistry;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.IFileTypeHandler;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.properties.IPropertiesManager;

@PropertySource("classpath:/config.properties")
@Service
public class FilesManager implements IFilesManager {

    private Logger logger = LoggerFactory.getLogger(FilesManager.class);

    @Autowired
    private IPropertiesManager propertyManager;

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
        IFile file = new edu.asu.diging.gilesecosystem.nepomuk.core.domain.impl.File();
        file.setFilepath(path);

        List<IFile> files = databaseClient.getFilesByExample(file);
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
    public String getRelativePathOfFile(IFile file) {
        IFileTypeHandler handler = fileHandlerRegistry.getHandler(file
                .getFileType());
        return handler.getRelativePathOfFile(file);
    }

}
