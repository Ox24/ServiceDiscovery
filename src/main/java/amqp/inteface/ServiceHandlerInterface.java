package amqp.inteface;

import representation.service.Service;

import java.util.List;

/**
 * Created by Timur on 1/22/2016.
 * Copyright Timur Tasci, ISW Universität Stuttgart
 */
public interface ServiceHandlerInterface {

    public List<Service> getAllServices();

    public Service getServiceById(String ID);

    public List<Service> getServicesByName(String serviceName);

    public List<Service> getServicesByRoleName(String roleName);

    public Service registerService(Service service);

    public boolean unregisterService(String id);


}
