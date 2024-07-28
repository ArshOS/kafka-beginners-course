package org.example.demos.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithCallback {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemoWithCallback.class.getSimpleName());

    public static void main(String[] args) {
        log.info("Hello World");

        // Create Producer Properties (for localhost)
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");

        // Set Producer Properties
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        // Create Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        for(int i=0; i<100; i++) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Create a Producer Record
            ProducerRecord<String, String> producerRecord =
                    new ProducerRecord<>("demo_java", "hello world " + i);

            // Send data
            producer.send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    if(e == null) {
                        log.info("Received new metadata \n"
                                + "Topic: " + metadata.topic() + "\n"
                                + "Partition: " + metadata.partition() + "\n"
                                + "Offset: " + metadata.offset() + "\n"
                                + "Timestamp: " + metadata.timestamp() + "\n");
                    } else {
                        log.error("Error while producing", e);
                    }
                }
            });
        }

        // Tell the producer to send all data and block until done -- synchronous
        producer.flush();

        // Flush and close the Producer
        producer.close();
    }
}
