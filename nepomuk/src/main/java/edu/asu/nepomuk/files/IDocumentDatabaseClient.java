package edu.asu.nepomuk.files;

import java.util.List;

import edu.asu.nepomuk.core.IDocument;
import edu.asu.nepomuk.db4o.IDatabaseClient;
import edu.asu.nepomuk.exception.UnstorableObjectException;

public interface IDocumentDatabaseClient extends IDatabaseClient<IDocument> {

    public abstract IDocument saveDocument(IDocument document) throws UnstorableObjectException;

    public abstract IDocument getDocumentById(String id);

    public abstract List<IDocument> getDocumentByUploadId(String uploadId);

    public abstract List<IDocument> getDocumentByExample(IDocument doc);

    public abstract List<IDocument> getDocumentsByUsername(String username);

}