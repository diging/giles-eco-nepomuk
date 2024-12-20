package edu.asu.diging.gilesecosystem.nepomuk.core.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.properties.Properties;
import edu.asu.diging.gilesecosystem.nepomuk.core.kafka.impl.StorageDeletionRequestReceiver;
import edu.asu.diging.gilesecosystem.nepomuk.core.kafka.impl.StorageRequestReceiver;
import edu.asu.diging.gilesecosystem.util.properties.IPropertiesManager;



import edu.asu.diging.gilesecosystem.requests.kafka.KafkaConfig;

@Configuration
@EnableKafka
public class NepomukKafkaConfig implements KafkaConfig {
    
    @Autowired
    private IPropertiesManager propertyManager;

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        // list of host:port pairs used for establishing the initial connections
        // to the Kakfa cluster
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                getHosts());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "geco.consumer.nepomuk." + new Random().nextInt(100));
        // consumer groups allow a pool of processes to divide the work of
        // consuming and processing records
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "gileseco.storage");

        return props;
    }

    @Bean
    public ConsumerFactory consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }

    @Bean
    public StorageRequestReceiver receiver() {
        return new StorageRequestReceiver();
    }
    
    @Bean
    public StorageDeletionRequestReceiver deletionReceiver() {
        return new StorageDeletionRequestReceiver();
    }

    @Override
    public String getHosts() {
        return propertyManager.getProperty(Properties.KAFKA_HOSTS);
    }

    @Override
    public String getProducerId() {
        return "geco.producer.nepomuk." + new Random().nextInt(100);
    }
}

