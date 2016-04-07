package uk.co.jaspalsvoice.jv.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.views.custom.medicine.Medicine;
import uk.co.jaspalsvoice.jv.views.custom.medicine.MedicineListAdapter;

/**
 * Created by Ana on 2/8/2016.
 */
public class MedicinesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_medicines);

        RecyclerView medicines = (RecyclerView) findViewById(R.id.medicines);
        medicines.setAdapter(new MedicineListAdapter(getTestValues()));
        medicines.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<Medicine> getTestValues() {
        List<Medicine> list = new ArrayList<>();
        Medicine m1 = new Medicine();
        m1.setName("Medicine Name 1");
        m1.setDosage("Dosage 1");
        m1.setReason("Reason 1");
        m1.setFrequency("Frequency 1");
        list.add(m1);

        Medicine m2 = new Medicine();
        m2.setName("Medicine Name 2");
        m2.setDosage("Dosage 2");
        m2.setReason("Reason 2");
        m2.setFrequency("Frequency 2");
        list.add(m2);

        Medicine m3 = new Medicine();
        m3.setName("Medicine Name 3");
        m3.setDosage("Dosage 3");
        m3.setReason("Reason 3");
        m3.setFrequency("Frequency 3");
        list.add(m3);

        Medicine m4 = new Medicine();
        list.add(m4);

        return list;
    }
}
