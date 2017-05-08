package edu.asu.diging.gilesecosystem.nepomuk.core.service.handlers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import edu.asu.diging.gilesecosystem.nepomuk.core.files.IFileStorageManager;
import edu.asu.diging.gilesecosystem.nepomuk.core.model.IFile;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.IFileTypeHandler;
import edu.asu.diging.gilesecosystem.requests.FileType;

@Service
public class ImageFileHandler extends AbstractFileHandler implements IFileTypeHandler {

    @Autowired
    @Qualifier("imageStorageManager")
    private IFileStorageManager storageManager;
    
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
