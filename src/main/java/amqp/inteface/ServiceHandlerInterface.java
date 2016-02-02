package amqp.inteface;

import representation.service.Service;

import java.util.List;

/**
 * Created by Timur on 1/22/2016.
 * Copyright Timur Tasci, ISW Universität Stuttgart
 *
 * Service Handler to manage all request from amqp or rest api and future OPC UA api
 */
public interface ServiceHandlerInterface {

    List<Service> getAllServices();

    Service getServiceById(String ID);

    List<Service> getServicesByName(String serviceName);

    List<Service> getServicesByRoleName(String roleName);

    Service registerService(Service service);

    boolean unregisterService(String id);

}
