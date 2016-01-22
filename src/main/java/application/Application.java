package application;

import amqp.ThreadPoolService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import util.DbManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@ComponentScan({"api", "amqp"})
public class Application {

    public static void main(String[] args) {
        DbManager.init();
        ExecutorService executor = Executors.newFixedThreadPool(5);
        SpringApplication.run(Application.class, args);
        for(int i = 0; i < 5; i++){
            executor.execute(new ThreadPoolService());
        }
    }
}
