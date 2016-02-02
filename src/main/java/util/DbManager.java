package util;

import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePoolFactory;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import representation.service.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Timur on 16.01.2016.
 * Copyright Timur Tasci
 */
public class DbManager {

    private static final String DB_USER = "admin";
    private static final String DB_PW = "admin";
    private static final String ENTITY_SERVICE = "representation.service";

    private static OObjectDatabaseTx db;

    public static synchronized void init(){
        try{
            db = new OObjectDatabaseTx(UtilConst.DATABASE_LOCATION).create();
        } catch (Exception e){
            db = new OObjectDatabaseTx(UtilConst.DATABASE_LOCATION);
        }
        if(db.isClosed())
            db.open(DB_USER, DB_PW);
        db.getEntityManager().registerEntityClasses(ENTITY_SERVICE);
        db.close();
    }

    public static synchronized Service registerService(Service service){
        service.setServiceId(UUID.randomUUID().toString());
        service.setVersion((long)0);
        service.setStatus(UtilConst.SERVICE_STATUS_ONLINE);
        List<Service> result;
        if(db.isClosed())
            db.open(DB_USER,DB_PW);
        try{
            result = createServiceFromDatabase(db.query(
                    new OSQLSynchQuery<Service>("select * from Service where ServiceName = '"
                            + service.getServiceName()
                            + "'"
                            + "OR ServiceId = '"
                            + service.getServiceId() + "'")));
            if(result.isEmpty())
                db.save(service);
        }catch (Exception e){
            return null;
        }
        finally {
            db.close();
        }
        if(result.isEmpty())
            return service;
        return result.get(0);
    }

    public static synchronized List<Service> getAllServices(){
        LinkedList<Service> list = new LinkedList<>();
        if(db.isClosed())
            db.open(DB_USER,DB_PW);
        try {
            for (Service service : db.browseClass(Service.class)) {
                list.add(db.detachAll(service, true));
            }
        }finally {
            db.close();
        }
        return list;
    }

    public static synchronized List<Service> getServiceByName(String name){
        List<Service> result;
        ODatabaseRecordThreadLocal.INSTANCE.set(db.getUnderlying());
        if(db.isClosed())
            db.open(DB_USER,DB_PW);
        try {
            result = createServiceFromDatabase(db.query(
                    new OSQLSynchQuery<Service>("select * from Service where ServiceName = '" + name + "'")));

        }finally {
            db.close();
        }
        return result;
    }

    public static synchronized Service getServiceById(String id){
        List<Service> result;
        ODatabaseRecordThreadLocal.INSTANCE.set(db.getUnderlying());
        if(db.isClosed())
            db.open(DB_USER,DB_PW);
        try {
            result = createServiceFromDatabase(db.query(
                    new OSQLSynchQuery<Service>("select * from Service where ServiceId = '" + id + "'")));

        }finally {
            db.close();
        }
        return result.get(0);
    }

    public static synchronized List<Service> getServicesByRoleName(String name){
        List<Service> result;
        ODatabaseRecordThreadLocal.INSTANCE.set(db.getUnderlying());
        if(db.isClosed())
            db.open(DB_USER,DB_PW);
        try {
            result = createServiceFromDatabase(db.query(
                    new OSQLSynchQuery<Service>("select * from Service where role.name = '" + name + "'")));

        }finally {
            db.close();
        }
        return result;
    }

    public static synchronized boolean unregisterServiceByID(String ID){
        List<Service> result;
        if(db.isClosed())
            db.open(DB_USER,DB_PW);
        try {
            result = db.query(
                    new OSQLSynchQuery<Service>("select * from Service where ServiceId = '" + ID + "'"));
            for (Service service : result){
                db.delete(service);
            }
        }catch (Exception e){
            return false;
        }
        finally {
            db.close();
        }
        return true;
    }

    public static synchronized boolean updateServiceByID(String ID, Service service){
        List<Service> result;
        List<Service> handlerles_services;
        if(db.isClosed())
            db.open(DB_USER,DB_PW);
        try {
            result = db.query(
                    new OSQLSynchQuery<Service>("select * from Service where ServiceId = '" + ID + "'"));
            handlerles_services = createServiceFromDatabase(result);
            for (Service db_service : result){
                db_service.setRole(service.getRole());
                db_service.setVersion(handlerles_services.get(0).getVersion() + 1);
                db.save(db_service);
            }
        }catch (Exception e){
            return false;
        }
        finally {
            db.close();
        }
        return true;
    }

    private static synchronized List<Service> createServiceFromDatabase(List<Service> services){
        ODatabaseRecordThreadLocal.INSTANCE.set(db.getUnderlying());
        List<Service> handlerLessServices = new LinkedList<>();
        for(Service service : services){
            handlerLessServices.add(db.detachAll(service,true));
        }
        return handlerLessServices;
    }

}
