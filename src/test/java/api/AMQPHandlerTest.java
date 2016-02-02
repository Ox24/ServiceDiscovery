package api;

import application.Application;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import org.junit.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import representation.service.Role;
import representation.service.Service;
import util.DbManager;

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

    private static String serviceName = "TestService";
    private static String roleName = "TestRole";
    private static String serviceId;
    private static Service service;

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

        service = new Service(serviceName, new Role(roleName));
        service = DbManager.registerService(service);
        serviceId = service.getServiceId();
    }

    @Test
    public void testGetAllServices() throws Exception{
        String response;
        String corrId = UUID.randomUUID().toString();
        Map<String, Object> header = new HashMap<>();
        Object a = "";
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

    @Test
    public void testGetServiceById() throws Exception{
        String response;
        String corrId = UUID.randomUUID().toString();
        Map<String, Object> header = new HashMap<>();
        Object a = serviceId;
        header.put("getServiceById", a);
        ObjectMapper mapper = new ObjectMapper();
        Service service1;

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
                service1 = mapper.readValue(response, Service.class);
                break;
            }
        }
        Assert.assertEquals(serviceId, service1.getServiceId());
    }

    @Test
    public void testGetServiceByName() throws Exception{
        String response;
        String corrId = UUID.randomUUID().toString();
        Map<String, Object> header = new HashMap<>();
        Object a = serviceName;
        header.put("getServicesByName", a);
        ObjectMapper mapper = new ObjectMapper();
        Service[] service1;

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
                service1 = mapper.readValue(response, Service[].class);
                break;
            }
        }
        Assert.assertEquals(serviceName, service1[0].getServiceName());
    }

    @Test
    public void testRegisterService() throws Exception{
        String response;
        String corrId = UUID.randomUUID().toString();
        Map<String, Object> header = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        Service tmp_Service = new Service("TestService", new Role("TEstRole"));
        Service response_Service;
        Object a = mapper.writeValueAsString(tmp_Service);
        header.put("registerService", a);

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
                response_Service = mapper.readValue(response, Service.class);
                break;
            }
        }
        Assert.assertNotEquals(tmp_Service.getServiceId(), response_Service.getServiceId());

        header.clear();

        header.put("unregisterService", response_Service.getServiceId());
        com.rabbitmq.client.AMQP.BasicProperties props2 = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .headers(header)
                .build();

        channel.basicPublish("", requestQueueName, props2, message.getBytes("UTF-8"));
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery(5000);
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response = new String(delivery.getBody(),"UTF-8");
                break;
            }
        }

        Assert.assertEquals("Deleted", response);
    }

    @Test
    public void testUpdateService() throws Exception{
        String response;
        String corrId = UUID.randomUUID().toString();
        Map<String, Object> header = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        Service tmp_Service = new Service("TestService", new Role("TEstRole"));
        Service response_Service;
        Object a = mapper.writeValueAsString(tmp_Service);
        header.put("registerService", a);

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
                response_Service = mapper.readValue(response, Service.class);
                break;
            }
        }
        Assert.assertNotEquals(tmp_Service.getServiceId(), response_Service.getServiceId());

        header.clear();

        header.put("updateService", response_Service.getServiceId());
        response_Service.setRole(new Role("UpdatedRole"));
        com.rabbitmq.client.AMQP.BasicProperties props2 = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .headers(header)
                .build();

        channel.basicPublish("", requestQueueName, props2, mapper.writeValueAsString(response_Service).getBytes("UTF-8"));
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery(5000);
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response = new String(delivery.getBody(),"UTF-8");
                break;
            }
        }
        Assert.assertEquals("Updated", response);
    }

    @Test
    public void testGetServicesByRoleName() throws Exception{
        String response;
        String corrId = UUID.randomUUID().toString();
        Map<String, Object> header = new HashMap<>();
        Object a = roleName;
        header.put("getServicesByRoleName", a);
        ObjectMapper mapper = new ObjectMapper();
        Service[] service1;

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
                service1 = mapper.readValue(response, Service[].class);
                break;
            }
        }
        Assert.assertEquals(roleName, service1[0].getRole().getName());
    }



    @AfterClass
    public static void shutdown() throws Exception{
        DbManager.unregisterServiceByID(serviceId);
        channel.close();
        connection.close();
    }
}
