package amqp;

import com.rabbitmq.client.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import util.UtilConst;

import java.io.IOException;

/**
 * Created by Timur on 1/14/2016.
 */
@Component
@Scope("prototype")
public class MQMApplication extends java.lang.Thread{

    @Override
    public void run() {
        SubscribeManager subscribeManager = SubscribeManager.getInstance();
        try {
            subscribeManager.init(UtilConst.MQM_LOCATION);
            subscribeManager.createChannel(UtilConst.EXCHANGE_NAME, "fanout");
            Consumer consumer = new DefaultConsumer(subscribeManager.getChannelFromExchange(UtilConst.EXCHANGE_NAME)) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");
                }
            };
            subscribeManager.getChannelFromExchange(UtilConst.EXCHANGE_NAME).basicConsume(
                    subscribeManager.bindToQueue(UtilConst.EXCHANGE_NAME), true, consumer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
