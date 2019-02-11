import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


public class TwitterClient {


    private static Logger log = LoggerFactory.getLogger(TwitterClient.class.getName());
    public static void main(String[] args) throws InterruptedException {



        String consumerKey = "t3EcuQyYKwcAweLLJAMqQHLtS";
        String consumerSecret = "GxFNaQDcNntxQ0Iwl6nLY1fECwoqqrddzSXDorfNJWIIv8wmRz";

        String token = "47693175-oksr5aCAYowQTVYgZlVQ5JMlzV2WiYlntIJ4qVKaa";
        String tokenSecret = "1mqG7rJRa9FPP3bCkqgHsyM7oZiL6dqttDxjPeiA4JyN7";

        /** Set up your blocking queues: Be sure to size these properly based on expected TPS of your stream */
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(1000);
        BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>(1000);

/** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
// Optional: set up some followings and track terms
      //  List<Long> followings = Lists.newArrayList(1234L, 566788L);
        List<String> terms = Lists.newArrayList("bitcoin");
     //   hosebirdEndpoint.followings(followings);
        hosebirdEndpoint.trackTerms(terms);

// These secrets should be read from a config file
        Authentication hosebirdAuth = new OAuth1(consumerKey, consumerSecret, token, tokenSecret);

        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")                              // optional: mainly for the logs
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue))
                .eventMessageQueue(eventQueue);                          // optional: use this if you want to process client events

        Client hosebirdClient = builder.build();
// Attempts to establish a connection.
        hosebirdClient.connect();


        // on a different thread, or multiple different threads....
        while (!hosebirdClient.isDone()) {
            //String msg = msgQueue.take();
            String msg = msgQueue.poll(5, TimeUnit.SECONDS);

          //  System.out.println("MESSAGE FROM \n "+msg);


            JSONObject obj = new JSONObject(msg);
            String textMessage = obj.getString("text");
            log.info("MSG "+textMessage);


            KafkaMessageProducer.writeToTopic(textMessage);

        }


    }
}
