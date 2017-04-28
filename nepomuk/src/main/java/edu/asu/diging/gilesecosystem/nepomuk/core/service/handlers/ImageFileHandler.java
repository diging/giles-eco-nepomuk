package edu.asu.diging.gilesecosystem.nepomuk.core.service.handlers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import edu.asu.diging.gilesecosystem.nepomuk.core.files.IFileStorageManager;
import edu.asu.diging.gilesecosystem.nepomuk.core.files.IFilesDatabaseClient;
import edu.asu.diging.gilesecosystem.nepomuk.core.model.IFile;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.IFileTypeHandler;
import edu.asu.diging.gilesecosystem.requests.FileType;
import edu.asu.diging.gilesecosystem.util.properties.IPropertiesManager;

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
    protected IFileStorageManager getStorageManager() {
        return storageManager;
    }

    public String getRelativePathInTypeFolder(IFile file) {
        return null;
    }

}
