package api;

import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import representation.service.*;
import util.DbManager;
import util.UtilConst;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Timur on 1/14/2016.
 */

@RestController
public class ServiceController {

    @RequestMapping("/api/services")
    public List<Service> getAllServices(){
        return DbManager.getAllServices();
    }

    @RequestMapping("/api/service/name/{serviceName}")
    public List<Service> getServicesByName(@PathVariable String serviceName){
        return DbManager.getServiceByName(serviceName);
    }

    @RequestMapping("/api/service/id/{id}")
    public Service getServiceById(@PathVariable String id){
        return DbManager.getServiceById(id);
    }

    @RequestMapping("/api/service/role/{roleName}")
    public List<Service> getServiceByRoleName(@PathVariable String roleName){
        return DbManager.getServicesByRoleName(roleName);
    }

    @RequestMapping(value = "/api/service/register", method = RequestMethod.POST)
    public Service registerService(@RequestBody Service service){
        return DbManager.registerService(service);
    }

    @RequestMapping(value = "/api/service/unregister/{serviceID}", method = RequestMethod.DELETE)
    public HttpStatus unregisterService(@PathVariable String serviceID){
        if(DbManager.unregisterServiceByID(serviceID))
            return HttpStatus.OK;
        return HttpStatus.CONFLICT;
    }

}
