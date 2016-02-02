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

    /**
     * Main function of this worker.
     * First init the connection and the queue listing to.
     * Infitloop for request/response with services
     */
    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        String responseMessage = "An error occured, Pls try again";
        QueueingConsumer.Delivery request = null;
        init(UtilConst.MQM_LOCATION);
        while (true) {
            try {
                request = this.consumer.nextDelivery();
                BasicProperties properties = request.getProperties();
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
                        .correlationId(properties.getCorrelationId())
                        .contentType("application/json")
                        .build();
                Set<String> headers = properties.getHeaders().keySet();
                if (!headers.isEmpty()) {
                    ObjectMapper mapper = new ObjectMapper();
                    switch (headers.toArray()[0].toString()) {
                        case "getAllServices":
                            this.channel.basicPublish("", properties.getReplyTo(), replyProps, mapper.writeValueAsBytes(DbManager.getAllServices()));
                            break;
                        case "getServiceById":
                            String serviceId = properties.getHeaders().get("getServiceById").toString();
                            this.channel.basicPublish("", properties.getReplyTo(), replyProps, mapper.writeValueAsBytes(DbManager.getServiceById(serviceId)));
                            break;
                        case "getServicesByName":
                            String serviceName = properties.getHeaders().get("getServicesByName").toString();
                            this.channel.basicPublish("", properties.getReplyTo(), replyProps, mapper.writeValueAsBytes(DbManager.getServiceByName(serviceName)));
                            break;
                        case "getServicesByRoleName":
                            this.channel.basicPublish("",
                                    properties.getReplyTo(),
                                    replyProps,
                                    mapper.writeValueAsBytes(DbManager.getServicesByRoleName(properties.getHeaders().get("getServicesByRoleName").toString())));
                            break;
                        case "registerService":
                            Service tmpService = mapper.readValue(properties.getHeaders().get("registerService").toString().getBytes(), Service.class);
                            this.channel.basicPublish("", properties.getReplyTo(), replyProps, mapper.writeValueAsBytes(DbManager.registerService(tmpService)));
                            break;
                        case "unregisterService":
                            responseMessage = "An error occured, Pls try again";
                            if (DbManager.unregisterServiceByID(properties.getHeaders().get("unregisterService").toString())) {
                                responseMessage = "Deleted";
                            }
                            this.channel.basicPublish("", properties.getReplyTo(), replyProps, responseMessage.getBytes("UTF-8"));
                            break;
                        case "updateService":
                            responseMessage = "An error occured, Pls try again";
                            Service updateService = mapper.readValue(request.getBody(), Service.class);
                            if (DbManager.updateServiceByID(properties.getHeaders().get("updateService").toString(), updateService))
                                responseMessage = "Updated";
                            this.channel.basicPublish("", properties.getReplyTo(), replyProps, responseMessage.getBytes());
                            break;
                    }
                }
                this.channel.basicAck(request.getEnvelope().getDeliveryTag(), false);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
