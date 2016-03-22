package uk.co.jaspalsvoice.jv.models;

import android.content.ContentValues;

import uk.co.jaspalsvoice.jv.db.DbOpenHelper;

/**
 * Created by Ana on 3/21/2016.
 */
public class Doctor {

    private String uuid;
    private String id;
    private String type;
    private String name;
    private String contact;

    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(DbOpenHelper.COLUMN_MT_UUID, getUuid());
        cv.put(DbOpenHelper.COLUMN_MT_ID, getId());
        cv.put(DbOpenHelper.COLUMN_MT_DOCTOR_TYPE, getType());
        cv.put(DbOpenHelper.COLUMN_MT_NAME, getName());
        cv.put(DbOpenHelper.COLUMN_MT_CONTACT_DETAILS, getContact());
        return cv;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
