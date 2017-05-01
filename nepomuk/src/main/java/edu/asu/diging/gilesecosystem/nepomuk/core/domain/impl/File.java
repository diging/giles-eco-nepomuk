package edu.asu.diging.gilesecosystem.nepomuk.core.domain.impl;

import javax.jdo.annotations.Index;
import javax.persistence.Entity;
import javax.persistence.Id;

import edu.asu.diging.gilesecosystem.nepomuk.core.domain.IFile;
import edu.asu.diging.gilesecosystem.requests.FileType;

/**
 * 
 * @deprecated
 *      Use {@link edu.asu.diging.gilesecosystem.nepomuk.core.model.impl.File} instead. 
 *      This class only exists for migration purposes.
 *      
 * @author jdamerow
 *
 */
@Deprecated
@Entity
public class File implements IFile {

    @Index private String uploadId;
    @Index private String filename;
    @Index private String username;
    @Index private String documentId;
    @Id private String id;
    private String uploadDate;
    private String processedDate;
    private long size;
    private String filepath; 
    private FileType fileType;
    private String contentType;
    @Index private String gilesFileId;
    
    public File() {
    }

    public File(String filename) {
        this.filename = filename;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.giles.core.impl.IFile#getUploadId()
     */
    @Override
    public String getUploadId() {
        return uploadId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.giles.core.impl.IFile#setUploadId(java.lang.String)
     */
    @Override
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.giles.core.impl.IFile#getFilename()
     */
    @Override
    public String getFilename() {
        return filename;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.giles.core.impl.IFile#setFilename(java.lang.String)
     */
    @Override
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.giles.core.impl.IFile#getUsername()
     */
    @Override
    public String getUsername() {
        return username;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.giles.core.impl.IFile#setUsername(java.lang.String)
     */
    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.giles.core.impl.IFile#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.giles.core.impl.IFile#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getDocumentId() {
        if (documentId == null) {
            return id;
        }
        return documentId;
    }

    @Override
    public void setDocumentId(String zoteroDocumentId) {
        this.documentId = zoteroDocumentId;
    }

    @Override
    public String getUploadDate() {
        return uploadDate;
    }

    @Override
    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String getFilepath() {
        return filepath;
    }

    @Override
    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
    
    @Override
    public FileType getFileType() {
        return fileType;
    }

    @Override
    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    @Override
    public String getProcessedDate() {
        return processedDate;
    }

    @Override
    public void setProcessedDate(String processedDate) {
        this.processedDate = processedDate;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String getGilesFileId() {
        return gilesFileId;
    }

    @Override
    public void setGilesFileId(String gilesFileId) {
        this.gilesFileId = gilesFileId;
    }
    
}
