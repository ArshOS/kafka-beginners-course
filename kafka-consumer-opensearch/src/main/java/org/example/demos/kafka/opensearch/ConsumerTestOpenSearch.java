package org.example.demos.kafka.opensearch;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerTestOpenSearch {

    private static final Logger log = LoggerFactory.getLogger(ConsumerTestOpenSearch.class.getName());

    public static void main(String[] args) {

        String bootstrapServers = "127.0.0.1:9092";
        String groupId = "test-group";
        String topic = "wikimedia.recentChange";

        Properties properties = new Properties();

        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        KafkaConsumer<String, String> testConsumer = new KafkaConsumer<>(properties);

        testConsumer.subscribe(Arrays.asList(topic));

        while (true) {

            ConsumerRecords<String, String> records = testConsumer.poll(Duration.ofSeconds(20));

            for (ConsumerRecord<String, String> record : records) {
                log.info("Topic:" + record.topic() + " Value: " + record.value());
            }
        }
    }
}
