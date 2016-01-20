package amqp;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import util.RetryStrategy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created by Timur on 03.01.2016.
 */

/**
 * Singleton which is used for Subcribe Management
 */
public class SubscribeManager {
    private static SubscribeManager ourInstance = new SubscribeManager();
    private Connection connection = null;
    private Map<String,Channel> channelMap = new HashMap<String, Channel>();

    public static SubscribeManager getInstance() {
        return ourInstance;
    }

    private SubscribeManager() {
    }

    /**
     * Initialize rabbitMq connection
     * @param host
     * @throws Exception
     */
    public void init(String host) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        RetryStrategy retryStrategy = new RetryStrategy(3, 1000);
        while (retryStrategy.shouldRetry() && connection == null){
            try {
                connection = factory.newConnection();
            } catch (IOException e) {
                retryStrategy.errorOccured();
            } catch (TimeoutException e) {
                retryStrategy.errorOccured();
            }
        }
    }

    /**
     * Create a subscribe channel
     * @param exchangeName
     * @param type
     * @throws Exception
     */
    public void createChannelWithExchange(String exchangeName, String type) throws Exception {
        Channel channel;
        if (connection != null){
            channel = connection.createChannel();
            channel.exchangeDeclare(exchangeName, type);
            channelMap.put(exchangeName, channel);
        } else {
            throw new Exception("No connection found");
        }
    }

    /**
     * Binds queue to channel
     * @param exchange
     * @return
     */
    public String bindToQueueWithExchange(String exchange){
        String queueName = null;
        Channel channel = getChannelFromExchange(exchange);
        try {
            queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, exchange, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return queueName;
    }

    /**
     * returns channel for subscribed exchange
     * @param exchange
     * @return
     */
    public Channel getChannelFromExchange (String exchange){
        return channelMap.get(exchange);
    }

}
