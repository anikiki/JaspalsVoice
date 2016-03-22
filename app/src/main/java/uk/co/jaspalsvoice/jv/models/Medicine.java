package uk.co.jaspalsvoice.jv.models;

import android.content.ContentValues;

import uk.co.jaspalsvoice.jv.db.DbOpenHelper;

/**
 * Created by Ana on 3/21/2016.
 */
public class Medicine {

    private String uuid;
    private String id;
    private String name;
    private String dosage;
    private String reason;
    private String frequency;

    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(DbOpenHelper.COLUMN_M_UUID, getUuid());
        cv.put(DbOpenHelper.COLUMN_M_ID, getId());
        cv.put(DbOpenHelper.COLUMN_M_NAME, getName());
        cv.put(DbOpenHelper.COLUMN_M_DOSAGE, getDosage());
        cv.put(DbOpenHelper.COLUMN_M_FREQUENCY, getFrequency());
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}
