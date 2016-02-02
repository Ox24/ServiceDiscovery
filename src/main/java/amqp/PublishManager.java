package amqp;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import util.UtilConst;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Created by Timur on 04.12.2015.
 * Copyright Timur Tasci
 */
public class PublishManager {
    private static PublishManager manager;
    private static ConnectionFactory factory;
    private static Connection connection;
    public static HashMap<String, Channel> channels ;

    private HashMap<String, Channel> getChannelsMap(){
        return channels;
    }

    public static void addChannels(String topic, Channel channel){
        channels.put(topic, channel);
    }

    public static void init(String host){
        channels = new HashMap<String, Channel>();
        if(factory == null)
            factory = new ConnectionFactory();
        factory.setHost(host);
        try {
            connection = factory.newConnection();
            publishTopic(UtilConst.SERVICE_UPDATE_EXCHANGE);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public static void shutdown() throws IOException, TimeoutException {
        Iterator<Channel> iterator = channels.values().iterator();
        while (iterator.hasNext()){
            iterator.next().close();
        }
        connection.close();
    }

    public static void publishTopics(List<String> topics){
        for (String topic : topics){
            Channel channel = null;
            try {
                channel = connection.createChannel();
                addChannels(topic, channel);
                channel.exchangeDeclare(topic, "fanout");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Channel getChannelForTopic(String topic){
        return channels.get(topic);
    }

    public static void publishTopic(String topic) {
        Channel channel = null;
        try {
            channel = connection.createChannel();
            addChannels(topic, channel);
            channel.exchangeDeclare(topic, "fanout");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void pubStringToChannel(String topic, String message){
        Channel channel = getChannelForTopic(topic);
        try {
            channel.basicPublish(topic,"", null, message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
