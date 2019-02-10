
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaMessageProducer {

    public static final String FIRST_TOPIC = "first_topic";

    public static void main(String[] args) {


        String bootstrapServer = "localhost:9092";

        Properties properties = new Properties();

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);

        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

        ProducerRecord<String,String> record = new ProducerRecord<String, String>(FIRST_TOPIC,"Kapil test");

        producer.send(record);


        //this will write to queue
        producer.flush();
        producer.close();



    }
}
