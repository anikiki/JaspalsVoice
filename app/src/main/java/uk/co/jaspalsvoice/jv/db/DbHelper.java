package uk.co.jaspalsvoice.jv.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import uk.co.jaspalsvoice.jv.models.Doctor;
import uk.co.jaspalsvoice.jv.models.Medicine;


/**
 * Created by Ana on 3/21/2016.
 */
public class DbHelper {
    private final static String TAG = DbHelper.class.getSimpleName();

    private ExecutorService executor = Executors.newFixedThreadPool(1);
    private final SQLiteDatabase sqlite;

    private static final String[] MEDICAL_TEAM_COLUMN_NAMES = new String[] {
            DbOpenHelper.COLUMN_MT_UUID,
            DbOpenHelper.COLUMN_MT_ID,
            DbOpenHelper.COLUMN_MT_DOCTOR_TYPE,
            DbOpenHelper.COLUMN_MT_NAME,
            DbOpenHelper.COLUMN_MT_CONTACT_DETAILS};

    private static final String[] MEDICINES_COLUMN_NAMES = new String[] {
            DbOpenHelper.COLUMN_M_UUID,
            DbOpenHelper.COLUMN_M_ID,
            DbOpenHelper.COLUMN_M_DOSAGE,
            DbOpenHelper.COLUMN_M_NAME,
            DbOpenHelper.COLUMN_M_REASON,
            DbOpenHelper.COLUMN_M_FREQUENCY};

    public DbHelper(DbOpenHelper DbOpenHelper) {
        sqlite = DbOpenHelper.getWritableDatabase();
    }

    /**
     * Inserts medical team in the db.
     */
    public Future<Long> insertOrReplaceDoctor(final List<Doctor> doctors) {
        return executor.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                long insertedRows = 0;
                try {
                    sqlite.beginTransaction();
                    for (Doctor doctor : doctors) {
                        if (sqlite.insertWithOnConflict(DbOpenHelper.TABLE_MEDICAL_TEAM, null, doctor.toContentValues(), SQLiteDatabase.CONFLICT_REPLACE) >= 0) {
                            insertedRows++;
                        }
                    }
                    Log.d(TAG, "Insert succeeded, inserted rows:" + insertedRows);
                    sqlite.setTransactionSuccessful();
                    return insertedRows;
                } finally {
                    sqlite.endTransaction();
                }
            }
        });
    }

    /**
     * Inserts medicines in the db.
     */
    public Future<Long> insertOrReplaceMedicine(final List<Medicine> medicines) {
        return executor.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                long insertedRows = 0;
                try {
                    sqlite.beginTransaction();
                    for (Medicine medicine : medicines) {
                        if (sqlite.insertWithOnConflict(DbOpenHelper.TABLE_MEDICINES, null, medicine.toContentValues(), SQLiteDatabase.CONFLICT_REPLACE) >= 0) {
                            insertedRows++;
                        }
                    }
                    Log.d(TAG, "Insert succeeded, inserted rows:" + insertedRows);
                    sqlite.setTransactionSuccessful();
                    return insertedRows;
                } finally {
                    sqlite.endTransaction();
                }
            }
        });
    }

    /**
     * Gets from db a list containing all doctors.
     */
    public Map<String, Doctor> readAllDoctors() {
        Map<String, Doctor> doctors = new HashMap<>();
        Cursor allDoctors = null;
        try {
            allDoctors = readAllFuture(DbOpenHelper.TABLE_MEDICAL_TEAM, MEDICAL_TEAM_COLUMN_NAMES, DbOpenHelper.COLUMN_MT_ID).get();
            if (allDoctors != null) {
                if (allDoctors.moveToFirst()) {
                    while (!allDoctors.isAfterLast()) {
                        Doctor doctor = new Doctor();
                        doctor.setName(allDoctors.getString(allDoctors.getColumnIndex(DbOpenHelper.COLUMN_MT_NAME)));
                        doctor.setType(allDoctors.getString(allDoctors.getColumnIndex(DbOpenHelper.COLUMN_MT_DOCTOR_TYPE)));
                        doctor.setContact(allDoctors.getString(allDoctors.getColumnIndex(DbOpenHelper.COLUMN_MT_CONTACT_DETAILS)));
                        doctors.put(doctor.getType(), doctor);
                        allDoctors.moveToNext();
                    }
                }
            }
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (allDoctors != null) {
                allDoctors.close();
            }
        }
        return doctors;
    }

    /**
     * Gets from db a list containing all medicines.
     */
    public List<Medicine> readAllMedicines() {
        List<Medicine> medicines = new ArrayList<>();
        Cursor allMedicines = null;
        try {
            allMedicines = readAllFuture(DbOpenHelper.TABLE_MEDICINES, MEDICINES_COLUMN_NAMES, DbOpenHelper.COLUMN_M_ID).get();
            if (allMedicines != null) {
                if (allMedicines.moveToFirst()) {
                    while (!allMedicines.isAfterLast()) {
                        Medicine medicine = new Medicine();
                        medicine.setName(allMedicines.getString(allMedicines.getColumnIndex(DbOpenHelper.COLUMN_M_NAME)));
                        medicine.setReason(allMedicines.getString(allMedicines.getColumnIndex(DbOpenHelper.COLUMN_M_REASON)));
                        medicine.setDosage(allMedicines.getString(allMedicines.getColumnIndex(DbOpenHelper.COLUMN_M_DOSAGE)));
                        medicine.setFrequency(allMedicines.getString(allMedicines.getColumnIndex(DbOpenHelper.COLUMN_M_FREQUENCY)));
                        medicines.add(medicine);
                        allMedicines.moveToNext();
                    }
                }
            }
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (allMedicines != null) {
                allMedicines.close();
            }
        }
        return medicines;
    }

    /**
     * Reads from db.
     */
    private Future<Cursor> readAllFuture(final String table, final String[] columns, final String order) {
        return executor.submit(new Callable<Cursor>() {
            @Override
            public Cursor call() throws Exception {
                return sqlite.query(table, columns,
                        null,
                        null,
                        null,
                        null,
                        order + " ASC"
                );
            }
        });
    }
}
