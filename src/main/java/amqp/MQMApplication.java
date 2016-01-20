package amqp;

import com.rabbitmq.client.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import util.UtilConst;

import java.io.IOException;

/**
 * Created by Timur on 1/14/2016.
 */

/**
 * Handles all messages for service options
 */
@Component
@Scope("prototype")
public class MQMApplication extends java.lang.Thread{

    @Override
    public void run() {
        AMQPHandler.init(UtilConst.MQM_LOCATION);
        while (true){
            QueueingConsumer.Delivery request = AMQPHandler.getNextRequest();
            //TODO: Handle different Service requests from header
        }
    }
}
