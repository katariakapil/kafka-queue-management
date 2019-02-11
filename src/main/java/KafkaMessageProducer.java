
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaMessageProducer {

    public static final String FIRST_TOPIC = "first_topic_kapil";
    public static final String FIRST_TOPIC_PROCCESSED = "first_topic_kapil_converted";

    public static void main(String[] args) {


        writeToTopic("Just for testing");



    }

    public static void writeToTopic(String msg) {

        String bootstrapServer = "localhost:9092";

        Properties properties = new Properties();

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);

        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

        ProducerRecord<String,String> record = new ProducerRecord<String, String>(FIRST_TOPIC,msg);

        producer.send(record);


        //this will write to queue
        producer.flush();
        producer.close();
    }
}
