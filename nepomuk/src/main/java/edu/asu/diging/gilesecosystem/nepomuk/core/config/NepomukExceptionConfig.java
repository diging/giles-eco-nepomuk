package edu.asu.diging.gilesecosystem.nepomuk.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.asu.diging.gilesecosystem.nepomuk.core.service.properties.Properties;
import edu.asu.diging.gilesecosystem.septemberutil.service.impl.SystemMessageHandler;
import edu.asu.diging.gilesecosystem.util.properties.IPropertiesManager;

/**
 * Class to initialize message handler class to send exception as kafka topic.
 * 
 * @author snilapwa
 *
 */
@Configuration
public class NepomukExceptionConfig {

    @Autowired
    private IPropertiesManager propertyManager;
    
    @Bean
    public SystemMessageHandler getMessageHandler() {
        return new SystemMessageHandler(propertyManager.getProperty(Properties.APPLICATION_ID));
    }
}
