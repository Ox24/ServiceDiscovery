package util;

import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
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
        if(db.isClosed())
            db.open(DB_USER,DB_PW);
        try{
            db.save(service);
        }catch (Exception e){
            return null;
        }
        finally {
            db.close();
        }
        return service;
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

    public static synchronized Service getServiceByName(String name){
        List<Service> result;
        Service service;
        ODatabaseRecordThreadLocal.INSTANCE.set(db.getUnderlying());
        if(db.isClosed())
            db.open(DB_USER,DB_PW);
        try {
            result = db.query(
                    new OSQLSynchQuery<Service>("select * from Service where ServiceName = '" + name + "'"));
            service = createServiceFromDatabase(result.get(0));
        }finally {
            db.close();
        }
        return service;
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

    private static Service createServiceFromDatabase(Service service){
        ODatabaseRecordThreadLocal.INSTANCE.set(db.getUnderlying());
        return db.detachAll(service, true);
    }

}
