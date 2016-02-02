package amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import representation.service.Service;
import util.DbManager;
import util.RetryStrategy;
import util.UtilConst;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeoutException;

/**
 * Created by Timur on 02.02.2016.
 * Copyright Timur Tasci
 */
public class HeartBeatService implements Runnable {


    private Connection connection = null;
    private Channel channel;
    private QueueingConsumer consumer;
    private static final String REQUEST_QUEUE_NAME = "ServiceDiscovery_Heartbeat";

    /**
     * Init connection and channel to rabbitmq for given thread
     *
     * @param host for cluster to connect
     */
    private void init(String host) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        RetryStrategy retryStrategy = new RetryStrategy(3, 1000);
        while (retryStrategy.shouldRetry() && this.connection == null) try {
            this.connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            try {
                retryStrategy.errorOccured();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        try {
            this.channel = this.connection.createChannel();
            this.channel.queueDeclare(REQUEST_QUEUE_NAME, false, false, false, null);
            this.channel.basicQos(1);
            this.consumer = new QueueingConsumer(this.channel);
            this.channel.basicConsume(REQUEST_QUEUE_NAME, false, this.consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        QueueingConsumer.Delivery request;
        init(UtilConst.MQM_LOCATION);
        while (true) {
            try {
                request = this.consumer.nextDelivery();
                BasicProperties properties = request.getProperties();
                Set<String> headers = properties.getHeaders().keySet();
                if (!headers.isEmpty()) {
                    switch (headers.toArray()[0].toString()) {
                        case "heartbeat":

                    }
                }
                this.channel.basicAck(request.getEnvelope().getDeliveryTag(), false);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
