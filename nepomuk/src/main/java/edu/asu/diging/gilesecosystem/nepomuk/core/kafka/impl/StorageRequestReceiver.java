package edu.asu.diging.gilesecosystem.nepomuk.core.kafka.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.diging.gilesecosystem.nepomuk.core.service.IRequestProcessor;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.properties.IPropertiesManager;
import edu.asu.diging.gilesecosystem.requests.IStorageRequest;
import edu.asu.diging.gilesecosystem.requests.impl.StorageRequest;

@PropertySource("classpath:/config.properties")
public class StorageRequestReceiver {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IRequestProcessor requestProcessor;
    
    @Autowired
    private IPropertiesManager propertiesManager;
    
    @KafkaListener(topics = "${topic_storage_request}")
    public void receiveMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        IStorageRequest request = null;
        try {
            request = mapper.readValue(message, StorageRequest.class);
        } catch (IOException e) {
            logger.error("Could not unmarshall request.", e);
            // FIXME: handel this case
            return;
        }
        
        requestProcessor.processRequest(request);
    }
}