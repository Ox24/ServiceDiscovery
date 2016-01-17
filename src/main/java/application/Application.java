package application;

import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import util.UtilConst;

@SpringBootApplication
@ComponentScan({"api"})
public class Application {

    public static void main(String[] args) {

        OObjectDatabaseTx db;
        try{
            db = new OObjectDatabaseTx(UtilConst.DATABASE_LOCATION).create();
        } catch (Exception e){
            db = new OObjectDatabaseTx(UtilConst.DATABASE_LOCATION);
        }
        if(db.isClosed())
            db.open("admin", "admin");
        db.getEntityManager().registerEntityClasses("representation.service");
        db.close();
        SpringApplication.run(Application.class, args);
    }
}
