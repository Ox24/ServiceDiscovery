package util;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;

/**
 * Created by Timur on 16.01.2016.
 * Copyright Timur Tasci
 */
public class DbManager {
    private static DbManager ourInstance = new DbManager();
    public static ODatabaseDocumentTx db;

    public static DbManager getInstance() {
        return ourInstance;
    }

    private DbManager() {
        db = new ODatabaseDocumentTx(UtilConst.DATABASE_LOCATION);
    }

    public static ODatabaseDocumentTx getDb(){
        return db;
    }

}
