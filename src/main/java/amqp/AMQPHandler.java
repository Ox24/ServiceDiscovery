package amqp;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import util.RetryStrategy;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Timur on 1/20/2016.
 */
public class AMQPHandler {

    private static Connection connection = null;
    private static Channel channel;
    private static QueueingConsumer consumer;
    private static final String REQUEST_QUEUE_NAME = "ServiceDiscovery_Request";

    public static void init(String host){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        RetryStrategy retryStrategy = new RetryStrategy(3, 1000);
        while (retryStrategy.shouldRetry() && connection == null) try {
            connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            try {
                retryStrategy.errorOccured();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        try {
            channel = connection.createChannel();
            channel.queueDeclare(REQUEST_QUEUE_NAME, false, false, false, null);
            channel.basicQos(1);
            consumer = new QueueingConsumer(channel);
            channel.basicConsume(REQUEST_QUEUE_NAME, false, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static QueueingConsumer.Delivery getNextRequest(){
        try {
            return consumer.nextDelivery();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
