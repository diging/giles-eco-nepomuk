package edu.asu.nepomuk.service.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import edu.asu.nepomuk.core.IDocument;
import edu.asu.nepomuk.core.IFile;
import edu.asu.nepomuk.core.IUpload;
import edu.asu.nepomuk.exception.NepomukFileStorageException;
import edu.asu.nepomuk.files.IFileStorageManager;
import edu.asu.nepomuk.service.properties.IPropertiesManager;

@PropertySource("classpath:/config.properties")
@Service
public class TextFileHandler extends AbstractFileHandler {

    @Autowired
    @Qualifier("textStorageManager")
    private IFileStorageManager textStorageManager;
    
    @Autowired
    private IPropertiesManager propertyManager;

    @Override
    public List<String> getHandledFileTypes() {
        List<String> types = new ArrayList<String>();
        types.add(MediaType.TEXT_PLAIN_VALUE);
        return types;
    }

    @Override
    public boolean processFile(String username, IFile file, IDocument document,
            IUpload upload, byte[] content) throws NepomukFileStorageException {
        // we don't do anything with text files
        return false;
    }

    @Override
    public String getRelativePathOfFile(IFile file) {
        String directory = textStorageManager.getFileFolderPath(file.getUsername(), file.getUploadId(), file.getDocumentId());
        return directory + File.separator + file.getFilename();
    }

    @Override
    public String getFileUrl(IFile file) {
        String gilesUrl = propertyManager.getProperty(IPropertiesManager.GILES_URL).trim();
        String contentEndpoint = propertyManager.getProperty(IPropertiesManager.GILES_FILE_ENDPOINT).trim();
        String contentSuffix = propertyManager.getProperty(IPropertiesManager.GILES_FILE_CONTENT_SUFFIX).trim();
        
        return gilesUrl + contentEndpoint + file.getId() + contentSuffix;
    }

    @Override
    protected IFileStorageManager getStorageManager() {
        return textStorageManager;
    }

}
