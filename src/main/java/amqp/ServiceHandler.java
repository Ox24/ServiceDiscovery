package amqp;

import amqp.inteface.ServiceHandlerInterface;
import representation.service.Service;
import util.DbManager;

import java.util.List;

/**
 * Created by Timur on 1/22/2016.
 * Copyright Timur Tasci, ISW Universität Stuttgart
 */
public class ServiceHandler implements ServiceHandlerInterface{
    @Override
    public List<Service> getAllServices() {
        return DbManager.getAllServices();
    }

    @Override
    public Service getServiceById(String id) {
        return DbManager.getServiceById(id);
    }

    @Override
    public List<Service> getServicesByName(String serviceName) {
        return DbManager.getServiceByName(serviceName);
    }

    @Override
    public List<Service> getServicesByRoleName(String roleName) {
        return DbManager.getServicesByRoleName(roleName);
    }

    @Override
    public Service registerService(Service service) {
        return DbManager.registerService(service);
    }

    @Override
    public boolean unregisterService(String id) {
        return DbManager.unregisterServiceByID(id);
    }

}
