package edu.asu.diging.gilesecosystem.nepomuk.core.kafka.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.diging.gilesecosystem.nepomuk.core.service.IRequestProcessor;
import edu.asu.diging.gilesecosystem.requests.IStorageRequest;
import edu.asu.diging.gilesecosystem.requests.impl.StorageRequest;
import edu.asu.diging.gilesecosystem.septemberutil.properties.MessageType;
import edu.asu.diging.gilesecosystem.septemberutil.service.ISystemMessageHandler;

@PropertySource("classpath:/config.properties")
public class StorageRequestReceiver {

    @Autowired
    private IRequestProcessor requestProcessor;

    @Autowired
    private ISystemMessageHandler messageHandler;

    @KafkaListener(topics = "${topic_storage_request}")
    public void receiveMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        IStorageRequest request = null;
        try {
            request = mapper.readValue(message, StorageRequest.class);
        } catch (IOException e) {
            messageHandler.handleMessage("Could not unmarshall request.", e, MessageType.ERROR);
            // FIXME: handle this case
            return;
        }

        requestProcessor.processRequest(request);
    }
}
