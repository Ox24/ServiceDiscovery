package application;

import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import util.DbManager;
import util.UtilConst;

@SpringBootApplication
@ComponentScan({"api"})
public class Application {

    public static void main(String[] args) {
        DbManager.init();
        SpringApplication.run(Application.class, args);
    }
}
