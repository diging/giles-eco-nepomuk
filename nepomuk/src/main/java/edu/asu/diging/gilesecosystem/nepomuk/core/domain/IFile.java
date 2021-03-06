package edu.asu.diging.gilesecosystem.nepomuk.core.domain;

import edu.asu.diging.gilesecosystem.nepomuk.core.store.IStorableObject;
import edu.asu.diging.gilesecosystem.requests.FileType;

/**
 * 
 * @deprecated
 *      Use {@link edu.asu.diging.gilesecosystem.nepomuk.core.model.IFile} instead. 
 *      This interface only exists for migration purposes.
 *      
 * @author jdamerow
 *
 */
@Deprecated
public interface IFile extends IStorableObject {

    public abstract String getUploadId();

    public abstract void setUploadId(String uploadId);

    public abstract String getFilename();

    public abstract void setFilename(String filename);

    public abstract String getUsername();

    public abstract void setUsername(String username);

    public abstract String getId();

    public abstract void setId(String id);

    public abstract void setDocumentId(String zoteroDocumentId);

    public abstract String getDocumentId();

    public abstract void setUploadDate(String uploadDate);

    public abstract String getUploadDate();

    public abstract void setSize(long size);

    public abstract long getSize();

    public abstract void setFilepath(String filepath);

    public abstract String getFilepath();

    public abstract void setFileType(FileType fileType);

    public abstract FileType getFileType();

    public abstract void setProcessedDate(String processedDate);

    public abstract String getProcessedDate();

    public abstract void setContentType(String contentType);

    public abstract String getContentType();

    public abstract void setGilesFileId(String gilesFileId);

    public abstract String getGilesFileId();

}