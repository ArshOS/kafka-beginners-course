package org.example.demos.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemo {

    private static final Logger log = LoggerFactory.getLogger(ConsumerDemo.class.getSimpleName());

    public static void main(String[] args) {
        log.info("I am a Kafka Consumer!");

        // Consumer group
        String groupId = "my-java-application";

        String topic = "demo_java";

        // Create Consumer Properties (for localhost)
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");

        // Create Consumer configs
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());
        properties.setProperty("group.id", groupId);
        properties.setProperty("auto.offset.reset", "earliest");

        // Create a Consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // Subscribe to a topic
        consumer.subscribe(Arrays.asList(topic));

        // Pol for data
        while(true) {

            log.info("Polling");

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

            for(ConsumerRecord<String, String> record : records) {
                log.info("Key: " + record.key() + ", Value: " + record.value());
                log.info("Partition: " + record.partition() + ", Offset: " + record.offset());
            }
        }


    }
}
