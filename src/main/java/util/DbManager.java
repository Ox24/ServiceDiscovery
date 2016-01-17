package util;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.sun.org.apache.bcel.internal.generic.RET;
import representation.service.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Timur on 16.01.2016.
 * Copyright Timur Tasci
 */
public class DbManager {

    private static final String DB_USER = "admin";
    private static final String DB_PW = "admin";
    private static final String ENTITY_SERVICE = "representation.service";

    private static OObjectDatabaseTx db;

    public static void init(){
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

    public static boolean registerService(Service service){
        if(db.isClosed())
            db.open(DB_USER,DB_PW);
        try{
            db.save(service);
        }catch (Exception e){
            return false;
        }
        finally {
            db.close();
        }
        return true;
    }

    public static List<Service> getAllServices(){
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

}
