package edu.asu.nepomuk.db4o.impl;

import java.io.Serializable;

import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ServerConfiguration;

import edu.asu.nepomuk.core.impl.Document;
import edu.asu.nepomuk.core.impl.File;
import edu.asu.nepomuk.core.impl.Upload;

public class DatabaseManager implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3325272288078647257L;
    private ObjectServer server;
    private String databasePath;

    public void init() {
        close();
        ServerConfiguration configuration = Db4oClientServer
                .newServerConfiguration();
        configuration.file().blockSize(80);
        configuration.common().objectClass(Document.class).cascadeOnUpdate(true);
        configuration.common().objectClass(Document.class).cascadeOnDelete(true);
        configuration.common().objectClass(Document.class).cascadeOnActivate(true);
        configuration.common().objectClass(Document.class).objectField("username").indexed(true);
        configuration.common().objectClass(Document.class).objectField("documentId").indexed(true);
        configuration.common().objectClass(Document.class).objectField("uploadId").indexed(true);
        configuration.common().objectClass(Document.class).objectField("id").indexed(true);
        
        configuration.common().objectClass(File.class).cascadeOnUpdate(true);
        configuration.common().objectClass(File.class).cascadeOnDelete(true);
        configuration.common().objectClass(File.class).cascadeOnActivate(true);
        configuration.common().objectClass(File.class).objectField("username").indexed(true);
        configuration.common().objectClass(File.class).objectField("id").indexed(true);
        
        configuration.common().objectClass(Upload.class).cascadeOnUpdate(true);
        configuration.common().objectClass(Upload.class).cascadeOnDelete(true);
        configuration.common().objectClass(Upload.class).cascadeOnActivate(true);
        configuration.common().objectClass(Upload.class).objectField("username").indexed(true);
        configuration.common().objectClass(Upload.class).objectField("id").indexed(true);
        
        server = Db4oClientServer.openServer(configuration, databasePath, 0);
    }

    public ObjectContainer getClient() {
        ObjectContainer container = server.openClient();
        return container;
    }

    public String getDatabasePath() {
        return databasePath;
    }

    public void setDatabasePath(String databasePath) {
        this.databasePath = databasePath;
    }

    private void close() {
        if (server != null) {
            server.close();
        }
        server = null;
    }

    public void shutdown() {
        close();
    }
}
