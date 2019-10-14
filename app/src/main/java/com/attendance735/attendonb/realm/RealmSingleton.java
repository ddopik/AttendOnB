package com.attendance735.attendonb.realm;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmObject;


/**
 * Created by ddopik on 10/10/2017.
 */

/**
 * this class contain Realm Common operation across Apps
 */
public class RealmSingleton {

    private Context appContext;
    private Realm realm;

    public RealmSingleton(Realm realm) {
        this.realm = realm;
    }

    //    public static <T> List<T> toList(String json, Class<T> type,  ObjectMapperProperties objectMapperProperties)
    public <E extends RealmObject> void saveNewItemToRealm(E object) {

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(object);
        realm.commitTransaction();

    }

    public <E extends RealmObject> E getSingleRealmItem(Class<E> className, String id_Key, int id) {
        return realm.where(className).equalTo(id_Key, id).findFirst();
    }

    public int getLastID(Class className, String id_key) {
        // increment index
        Number currentIdNum = realm.where(className).max(id_key);
        int nextId;
        if (currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }
    /**
     * Clear all Realm Configuration and Tables data
     */
    public static void clearRealm() {
        Realm.deleteRealm(Realm.getDefaultConfiguration());
    }
}
