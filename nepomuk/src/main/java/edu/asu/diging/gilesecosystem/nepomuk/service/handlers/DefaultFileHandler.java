package edu.asu.diging.gilesecosystem.nepomuk.service.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import edu.asu.diging.gilesecosystem.nepomuk.core.IFile;
import edu.asu.diging.gilesecosystem.nepomuk.files.IFileStorageManager;
import edu.asu.diging.gilesecosystem.nepomuk.files.IFilesDatabaseClient;
import edu.asu.diging.gilesecosystem.nepomuk.service.IFileTypeHandler;
import edu.asu.diging.gilesecosystem.nepomuk.service.properties.IPropertiesManager;
import edu.asu.diging.gilesecosystem.requests.FileType;

@PropertySource("classpath:/config.properties")
@Service
public class DefaultFileHandler extends AbstractFileHandler implements IFileTypeHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    @Qualifier("otherStorageManager")
    private IFileStorageManager storageManager;
    
    @Autowired
    private IPropertiesManager propertyManager;
    
    @Autowired
    private IFilesDatabaseClient databaseClient;
    
    
    @Override
    public List<FileType> getHandledFileTypes() {
        List<FileType> types = new ArrayList<FileType>();
        types.add(FileType.OTHER);
        return types;
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
