package edu.asu.diging.gilesecosystem.nepomuk.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import edu.asu.diging.gilesecosystem.nepomuk.core.model.IFile;
import edu.asu.diging.gilesecosystem.requests.FileType;

@Entity
@Table(indexes={
        @Index(columnList="uploadId", name="IDX_UPLOAD_ID"),
        @Index(columnList="username", name="IDX_USERNAME"),
        @Index(columnList="documentId", name="IDX_USERNAME"),
        @Index(columnList="gilesFileId", name="IDX_GILES_FILE_ID"),
        @Index(columnList="filename", name="IDX_FILENAME"),
})
public class File implements IFile {

    @Id private String id;
    
    private String uploadId;
    private String filename;
    private String username;
    private String documentId;
    private String uploadDate;
    private String processedDate;
    private long size;
    private String filepath; 
    private FileType fileType;
    private String contentType;
    private String gilesFileId;
    
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
