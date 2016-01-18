package application;

import amqp.MQMApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import util.DbManager;

@SpringBootApplication
@ComponentScan({"api", "amqp"})
public class Application {

    public static void main(String[] args) {
        DbManager.init();
        MQMApplication app = new MQMApplication();

        SpringApplication.run(Application.class, args);
        app.start();
    }
}
