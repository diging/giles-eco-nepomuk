package edu.asu.diging.gilesecosystem.nepomuk.core.service.properties;

import java.util.Map;

import edu.asu.diging.gilesecosystem.nepomuk.core.exception.NepomukPropertiesStorageException;

public interface IPropertiesManager {
    
    public final static String APP_BASE_URL = "app_base_url";
    public final static String REST_ENDPOINT_PREFIX = "rest_endpoint";
    
    public final static String SIGNING_KEY = "jwt_signing_secret";
    public final static String SIGNING_KEY_APPS = "jwt_signing_secret_apps";
  
    public final static String GILES_ACCESS_TOKEN = "giles_access_token";
    public final static String KAFKA_TOPIC_STORAGE_COMPLETE = "topic_storage_request_complete";
    public final static String KAFKA_HOSTS = "kafka_hosts";

    public abstract void setProperty(String key, String value) throws NepomukPropertiesStorageException;

    public abstract String getProperty(String key);

    public abstract void updateProperties(Map<String, String> props)
            throws NepomukPropertiesStorageException;

}
