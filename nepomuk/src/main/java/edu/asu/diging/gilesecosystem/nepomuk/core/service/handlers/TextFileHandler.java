package edu.asu.diging.gilesecosystem.nepomuk.core.service.handlers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import edu.asu.diging.gilesecosystem.nepomuk.core.files.IFileStorageManager;
import edu.asu.diging.gilesecosystem.requests.FileType;
import edu.asu.diging.gilesecosystem.util.properties.IPropertiesManager;

@PropertySource("classpath:/config.properties")
@Service
public class TextFileHandler extends AbstractFileHandler {

    @Autowired
    @Qualifier("textStorageManager")
    private IFileStorageManager textStorageManager;
    
    @Autowired
    private IPropertiesManager propertyManager;

    @Override
    public List<FileType> getHandledFileTypes() {
        List<FileType> types = new ArrayList<FileType>();
        types.add(FileType.TEXT);
        return types;
    }

    @Override
    protected IFileStorageManager getStorageManager() {
        return textStorageManager;
    }

}
