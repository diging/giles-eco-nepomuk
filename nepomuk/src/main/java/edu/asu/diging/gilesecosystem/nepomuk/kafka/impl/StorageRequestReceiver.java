package edu.asu.diging.gilesecosystem.nepomuk.kafka.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.diging.gilesecosystem.nepomuk.service.IRequestProcessor;
import edu.asu.diging.gilesecosystem.requests.IStorageRequest;
import edu.asu.diging.gilesecosystem.requests.impl.StorageRequest;

public class StorageRequestReceiver {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IRequestProcessor requestProcessor;
    
    @KafkaListener(topics = "giles.requests.storage")
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
