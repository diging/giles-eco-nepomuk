package edu.asu.diging.gilesecosystem.nepomuk.core.service.handlers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import edu.asu.diging.gilesecosystem.nepomuk.core.files.IFileStorageManager;
import edu.asu.diging.gilesecosystem.nepomuk.core.model.IFile;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.IFileTypeHandler;
import edu.asu.diging.gilesecosystem.requests.FileType;

@PropertySource("classpath:/config.properties")
@Service
public class PdfFileHandler extends AbstractFileHandler implements
        IFileTypeHandler {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("pdfStorageManager")
    private IFileStorageManager pdfStorageManager;

    
    @Override
    public List<FileType> getHandledFileTypes() {
        List<FileType> types = new ArrayList<FileType>();
        types.add(FileType.PDF);
        return types;
    }


    @Override
    protected IFileStorageManager getStorageManager() {
        return pdfStorageManager;
    }


    public String getRelativePathInTypeFolder(IFile file) {
        return null;
    }

}
