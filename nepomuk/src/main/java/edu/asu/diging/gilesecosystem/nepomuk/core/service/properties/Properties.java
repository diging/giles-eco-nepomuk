package edu.asu.diging.gilesecosystem.nepomuk.core.service.properties;


public interface Properties {

    public final static String NEPOMUK_SIGNING_KEY = "nepomuk_signing_secret";
    
    public final static String APP_BASE_URL = "app_base_url";
    public final static String REST_ENDPOINT_PREFIX = "rest_endpoint";
    public final static String GILES_ACCESS_TOKEN = "giles_access_token";
    public final static String KAFKA_TOPIC_STORAGE_COMPLETE = "topic_storage_request_complete";
    
    public final static String KAFKA_HOSTS = "kafka_hosts";
    public final static String KAFKA_TOPIC_OCR_REQUEST = "request_ocr_topic";
    public final static String KAFKA_TOPIC_OCR_COMPLETE_REQUEST = "topic_orc_request_complete";
    public final static String KAFKA_TOPIC_STORAGE_REQUEST = "request_storage_topic";
    public final static String KAFKA_TOPIC_STORAGE_COMPLETE_REQUEST = "topic_storage_request_complete";
    public final static String KAFKA_TOPIC_TEXT_EXTRACTION_REQUEST = "request_text_extraction_topic";
    public final static String KAFKA_TOPIC_TEXT_EXTRACTION_COMPLETE_REQUEST = "topic_text_extraction_request_complete";
    public final static String KAFKA_TOPIC_IMAGE_EXTRACTION_REQUEST = "topic_image_extraction_request";
    public final static String KAFKA_TOPIC_IMAGE_EXTRACTION_COMPLETE_REQUEST = "topic_image_extraction_request_complete";
    
    public final static String GILES_TMP_FOLDER = "giles_files_tmp_dir";

}
