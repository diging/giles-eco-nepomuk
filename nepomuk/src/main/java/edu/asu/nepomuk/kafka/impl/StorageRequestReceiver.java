package edu.asu.nepomuk.kafka.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

public class StorageRequestReceiver {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @KafkaListener(topics = "giles.requests.storage")
    public void receiveMessage(String message) {
        logger.info("received message='{}'", message);
    }
}
