package api;

import application.Application;
import com.rabbitmq.client.*;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Timur on 1/22/2016.
 * Copyright Timur Tasci, ISW Universität Stuttgart
 */
public class AMQPHandlerTest {

    private static Connection connection = null;
    private static Channel channel;
    private static QueueingConsumer consumer;
    private static final String REQUEST_QUEUE_NAME = "ServiceDiscovery_Request";
    private static String replyQueueName;
    private static String requestQueueName = "ServiceDiscovery_Request";

    @BeforeClass
    public static void init() throws Exception{
        Application.main(new String[] {});
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();

        replyQueueName = channel.queueDeclare().getQueue();
        consumer = new QueueingConsumer(channel);
        channel.basicConsume(replyQueueName, true, consumer);
    }

    @Test
    public void sendRequest() throws Exception{
        String response;
        String corrId = UUID.randomUUID().toString();
        Map<String, Object> header = new HashMap<>();
        Object a = "I want this header back";
        header.put("getAllServices", a);

        String message = "I want a request";
        com.rabbitmq.client.AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .headers(header)
                .build();

        channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery(5000);
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response = new String(delivery.getBody(),"UTF-8");
                break;
            }
        }
        Assert.assertTrue(!response.isEmpty());

    }
}
