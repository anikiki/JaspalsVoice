package uk.co.jaspalsvoice.jv.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ana on 3/21/2016.
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "jv.db";
    private static final int DATABASE_VERSION = 1;

    // Table, columns
    public static final String TABLE_MEDICAL_TEAM = "medical_team";
    public static final String COLUMN_MT_UUID = "uuid";
    public static final String COLUMN_MT_ID = "id";
    public static final String COLUMN_MT_DOCTOR_TYPE = "type";
    public static final String COLUMN_MT_NAME = "name";
    public static final String COLUMN_MT_CONTACT_DETAILS = "contact";


    // Table, columns
    public static final String TABLE_MEDICINES = "medicines";
    public static final String COLUMN_M_UUID = "uuid";
    public static final String COLUMN_M_ID = "id";
    public static final String COLUMN_M_NAME = "name";
    public static final String COLUMN_M_DOSAGE = "dosage";
    public static final String COLUMN_M_REASON = "reason";
    public static final String COLUMN_M_FREQUENCY = "frequency";

    // Creation statement for TABLE_MEDICAL_TEAM
    private static final String CREATE_TABLE_MEDICAL_TEAM = "CREATE TABLE "
            + TABLE_MEDICAL_TEAM + "("
            + COLUMN_MT_UUID + " TEXT UNIQUE ON CONFLICT REPLACE, "
            + COLUMN_MT_ID + " TEXT, "
            + COLUMN_MT_DOCTOR_TYPE + " TEXT, "
            + COLUMN_MT_NAME + " TEXT, "
            + COLUMN_MT_CONTACT_DETAILS + " TEXT);";

    // Creation statement for TABLE_MEDICINES
    private static final String CREATE_TABLE_MEDICINES = "CREATE TABLE "
            + TABLE_MEDICINES + "("
            + COLUMN_M_UUID + " TEXT UNIQUE ON CONFLICT REPLACE, "
            + COLUMN_M_ID + " TEXT, "
            + COLUMN_M_NAME + " TEXT, "
            + COLUMN_M_DOSAGE + " TEXT, "
            + COLUMN_M_REASON + " TEXT, "
            + COLUMN_M_FREQUENCY + " TEXT);";

    public DbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MEDICAL_TEAM);
        db.execSQL(CREATE_TABLE_MEDICINES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}