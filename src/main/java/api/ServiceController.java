package api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import representation.service.*;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Timur on 1/14/2016.
 */

@RestController
public class ServiceController {

    @RequestMapping("/api/services")
    public Service getServices(){
        LinkedList<MessageInterface> interfaces = new LinkedList<MessageInterface>();
        interfaces.add(new MessageInterface("NameOFPublishStream", MessageInterfaceType.PUBLISH));
        interfaces.add(new MessageInterface("NameOFPublishStream2", MessageInterfaceType.PUBLISH));
        return new Service(1,"MaschineService", new Role("RoleID", interfaces));
    }

    @RequestMapping(value = "/api/service/register", method = RequestMethod.POST)
    public HttpStatus registerService(@RequestBody Service service){

        return HttpStatus.OK;
    }

}
