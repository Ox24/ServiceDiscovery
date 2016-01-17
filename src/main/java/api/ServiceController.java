package api;

import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import representation.service.*;
import util.UtilConst;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Timur on 1/14/2016.
 */

@RestController
public class ServiceController {

    OObjectDatabaseTx db = new OObjectDatabaseTx(UtilConst.DATABASE_LOCATION);

    @RequestMapping("/api/services")
    public List<Service> getServices(){
        /*LinkedList<MessageInterface> interfaces = new LinkedList<MessageInterface>();
        interfaces.add(new MessageInterface("NameOFPublishStream", MessageInterfaceType.PUBLISH));
        interfaces.add(new MessageInterface("NameOFPublishStream2", MessageInterfaceType.PUBLISH)); */
        LinkedList<Service> serviceList = new LinkedList<Service>();
        if(db.isClosed())
            db.open("admin","admin");
        try {
            for (Service service : db.browseClass(Service.class)) {
                Service service1 = db.detachAll(service, true);
                serviceList.add(service1);
                System.out.println(service1.getServiceName());
            }
        }finally {
            db.close();
        }
        return serviceList;
    }

    @RequestMapping(value = "/api/service/register", method = RequestMethod.POST)
    public HttpStatus registerService(@RequestBody Service service){
        if(db.isClosed())
            db.open("admin","admin");
        try{
            db.save(service);
        }finally {
            db.close();
        }
        return HttpStatus.OK;
    }

}
