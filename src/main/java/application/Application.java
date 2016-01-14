package application;

import org.apache.tomcat.jni.Thread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"api"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }
}
