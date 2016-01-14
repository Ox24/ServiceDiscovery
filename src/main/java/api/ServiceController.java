package api;

import org.springframework.web.bind.annotation.RequestMapping;
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
        return new Service(1, new Role("RoleID", interfaces));
    }

}
