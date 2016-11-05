package edu.asu.diging.gilesecosystem.nepomuk.service.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import edu.asu.diging.gilesecosystem.nepomuk.core.IFile;
import edu.asu.diging.gilesecosystem.nepomuk.files.IFileStorageManager;
import edu.asu.diging.gilesecosystem.nepomuk.files.IFilesDatabaseClient;
import edu.asu.diging.gilesecosystem.nepomuk.service.IFileTypeHandler;
import edu.asu.diging.gilesecosystem.nepomuk.service.properties.IPropertiesManager;
import edu.asu.diging.gilesecosystem.requests.FileType;

@Service
public class ImageFileHandler extends AbstractFileHandler implements IFileTypeHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IPropertiesManager propertyManager;
    
    @Autowired
    @Qualifier("imageStorageManager")
    private IFileStorageManager storageManager;
    
    @Autowired
    private IFilesDatabaseClient filesDbClient;


    @Override
    public List<FileType> getHandledFileTypes() {
        List<FileType> fileTypes = new ArrayList<FileType>();
        fileTypes.add(FileType.IMAGE);
        return fileTypes;
    }

    @Override
    public String getRelativePathOfFile(IFile file) {
        String directory = storageManager.getFileFolderPath(file.getUsername(), file.getUploadId(), file.getDocumentId());
        return directory + File.separator + file.getFilename();
    }

    @Override
    protected IFileStorageManager getStorageManager() {
        return storageManager;
    }

}
