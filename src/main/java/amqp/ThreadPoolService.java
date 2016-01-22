package amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import util.DbManager;
import util.RetryStrategy;
import util.UtilConst;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeoutException;

/**
 * Created by Timur on 1/22/2016.
 * Copyright Timur Tasci, ISW Universität Stuttgart
 */
public class ThreadPoolService implements Runnable {

    private Connection connection = null;
    private Channel channel;
    private QueueingConsumer consumer;
    private static final String REQUEST_QUEUE_NAME = "ServiceDiscovery_Request";

    /**
     * Init connection and channel to rabbitmq for given thread
     * @param host
     */
    private void init(String host){
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
        init(UtilConst.MQM_LOCATION);
        while (true) try {
            QueueingConsumer.Delivery request = this.consumer.nextDelivery();
            BasicProperties properties = request.getProperties();
            com.rabbitmq.client.AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
                    .correlationId(properties.getCorrelationId())
                    .contentType("application/json")
                    .build();
            Set<String> headers = properties.getHeaders().keySet();
            if(!headers.isEmpty()){
                ObjectMapper mapper = new ObjectMapper();
                switch (headers.toArray()[0].toString()){
                    case "getAllServices":
                        this.channel.basicPublish("",properties.getReplyTo(),replyProps,mapper.writeValueAsBytes(DbManager.getAllServices()));
                        break;
                    case "getServiceById":
                        String serviceId = (String) properties.getHeaders().get("getServiceById");
                        this.channel.basicPublish("",properties.getReplyTo(),replyProps,mapper.writeValueAsBytes(DbManager.getServiceById(serviceId)));
                        break;
                    case "getServicesByName":
                        break;
                    case "getServicesByRoleName":
                        break;
                    case "registerService":
                        break;
                    case "unregisterService":

                }
            }
            this.channel.basicAck(request.getEnvelope().getDeliveryTag(), false);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
