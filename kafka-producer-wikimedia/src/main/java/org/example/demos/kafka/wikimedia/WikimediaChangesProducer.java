package org.example.demos.kafka.wikimedia;

import com.launchdarkly.eventsource.EventSource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class WikimediaChangesProducer {

    private static final Logger log = LoggerFactory.getLogger(WikimediaChangesProducer.class.getName());

    public static void main(String[] args) throws InterruptedException {
        log.info("I am Wikimedia Producer");
        String bootstrapServers = "127.0.0.1:9092";

        // Create Producer Properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Set safe producer configs (Kafka <= 2.8)
        /*
          properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
          properties.setProperty(ProducerConfig.ACKS_CONFIG, "all"); // same as setting -1
          properties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
         */

        // High throughput producer properties
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20");
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32*1024));


        // Create Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        String topic = "wikimedia.recentChange";

        com.launchdarkly.eventsource.EventHandler eventHandler = new WikimediaChangeHandler(producer, topic);
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        EventSource.Builder builder = new EventSource.Builder((com.launchdarkly.eventsource.EventHandler) eventHandler, URI.create(url));
        EventSource eventSource = builder.build();

        // Start stream processing on a different thread
        eventSource.start();

        // Main thread will finish execution and stream processing thread will continue but nothing will happen.
        // To avoid this, set interruption time.
        TimeUnit.MINUTES.sleep(2);

        // Flush and close the Producer
    }
}
