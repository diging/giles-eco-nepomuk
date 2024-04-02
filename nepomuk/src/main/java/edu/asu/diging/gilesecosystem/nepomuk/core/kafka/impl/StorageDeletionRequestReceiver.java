package edu.asu.diging.gilesecosystem.nepomuk.core.kafka.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.diging.gilesecosystem.nepomuk.core.service.IDeletionRequestProcessor;
import edu.asu.diging.gilesecosystem.requests.IStorageDeletionRequest;
import edu.asu.diging.gilesecosystem.requests.impl.StorageDeletionRequest;
import edu.asu.diging.gilesecosystem.septemberutil.properties.MessageType;
import edu.asu.diging.gilesecosystem.septemberutil.service.ISystemMessageHandler;

@PropertySource("classpath:/config.properties")
public class StorageDeletionRequestReceiver {
    @Autowired
    private IDeletionRequestProcessor deletionRequestProcessor;

    @Autowired
    private ISystemMessageHandler messageHandler;
    
    /**
     * Kafka listener method for receiving and processing a delete storage request message.
     * @param message The message containing the delete storage request.
     */
    @KafkaListener(topics = "${topic_delete_storage_request}")
    public void receiveDeleteMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        IStorageDeletionRequest request = null;
        try {
            request = mapper.readValue(message, StorageDeletionRequest.class);
        } catch (IOException e) {
            messageHandler.handleMessage("Could not unmarshall request.", e, MessageType.ERROR);
            return;
        }
        deletionRequestProcessor.processRequest(request);
    }
}
