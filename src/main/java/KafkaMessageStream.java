import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.*;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.kstream.ForeachAction;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

public class KafkaMessageStream {

    public static void main(final String[] args) throws Exception {

        final String bootstrapServers = args.length > 0 ? args[0] : "localhost:9092";

        final Properties streamsConfiguration = new Properties();

        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "twitter-example-1");
        streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG, "twitter-example-client-1");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());


        // Records should be flushed every 10 seconds. This is less than the default
        // in order to keep this example interactive.
        streamsConfiguration.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);
        // For illustrative purposes we disable record caches
        streamsConfiguration.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);


        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();
        final StreamsBuilder builder = new StreamsBuilder();


        final KStream<String, String> textLines = builder.stream(KafkaMessageProducer.FIRST_TOPIC);

        final Pattern pattern = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS);

      /* textLines.foreach(new ForeachAction<String, String>() {
            public void apply(String key, String value) {
                System.out.println(key + "::" + value);
            }
        });
        */

       // System.out.println("Line read ... "+textLines.flatMap());

        final KTable<String, Long> wordCounts =

              textLines.flatMapValues(value -> Arrays.asList(pattern.split(value)))
                .groupBy((key, word) -> word)
                .count();


        // Write the `KTable<String, Long>` to the output topic.
        wordCounts.toStream().to(KafkaMessageProducer.FIRST_TOPIC_PROCCESSED, Produced.with(stringSerde, longSerde));

        final KafkaStreams streams = new KafkaStreams(builder.build(), streamsConfiguration);

        streams.cleanUp();
        streams.start();


        // Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

    }

}