package org.example.demos.kafka.wikimedia;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WikimediaChangeHandler implements EventHandler {

    KafkaProducer<String, String> kafkaProducer;
    String topic;
    private final Logger log = LoggerFactory.getLogger(WikimediaChangeHandler.class.getName());

    WikimediaChangeHandler(KafkaProducer<String, String> kafkaProducer, String topic) {
        this.kafkaProducer = kafkaProducer;
        this.topic = topic;
    }

    @Override
    public void onOpen() {
        // This is when stream is open
        // Do nothing
    }

    @Override
    public void onClosed() {
        // This is closing reading from stream
        kafkaProducer.close();
    }

    @Override
    public void onMessage(String s, MessageEvent messageEvent) {
        // This is when a message is received from the open stream
        // We want to send it through the kafkaProducer

        // asynchronous

        // Make a kafka record with the data received and send it.

        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, messageEvent.getData());
        kafkaProducer.send(producerRecord);

    }

    @Override
    public void onComment(String s) {
        // Do nothing
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Error in stream reading", throwable);
    }
}
