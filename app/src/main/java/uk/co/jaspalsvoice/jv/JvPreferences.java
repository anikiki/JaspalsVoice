package uk.co.jaspalsvoice.jv;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ana on 3/21/2016.
 */
public class JvPreferences {

    private static final String PREFS_FILE_NAME = "JvPreferences.txt";

    private static final String PERSONAL_DETAILS_NAME = "pd_name";
    private static final String PERSONAL_DETAILS_PREFERRED_NAME = "pd_preferred_name";
    private static final String PERSONAL_DETAILS_LIVE_WITH = "pd_live_with";
    private static final String PERSONAL_DETAILS_EMAIL = "pd_email";
    private static final String PERSONAL_DETAILS_DATE_OF_BIRTH = "pd_dob";
    private static final String PERSONAL_DETAILS_MAIN_CARER = "pd_main_carer";
    private static final String PERSONAL_DETAILS_CARER_TEL = "pd_carer_tel";
    private static final String PERSONAL_DETAILS_NEED_TRANSLATOR = "pd_need_translator";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public JvPreferences(Context context) {
        preferences = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public String getPersonalDetailsName() {
        return preferences.getString(PERSONAL_DETAILS_NAME, "");
    }

    public void setPersonalDetailsName(String name) {
        editor.putString(PERSONAL_DETAILS_NAME, name).apply();
    }

    public String getPersonalDetailsPreferredName() {
        return preferences.getString(PERSONAL_DETAILS_PREFERRED_NAME, "");
    }

    public void setPersonalDetailsPreferredName(String name) {
        editor.putString(PERSONAL_DETAILS_PREFERRED_NAME, name).apply();
    }

    public String getPersonalDetailsLiveWith() {
        return preferences.getString(PERSONAL_DETAILS_LIVE_WITH, "");
    }

    public void setPersonalDetailsLiveWith(String name) {
        editor.putString(PERSONAL_DETAILS_LIVE_WITH, name).apply();
    }

    public String getPersonalDetailsEmail() {
        return preferences.getString(PERSONAL_DETAILS_EMAIL, "");
    }

    public void setPersonalDetailsEmail(String email) {
        editor.putString(PERSONAL_DETAILS_EMAIL, email).apply();
    }

    public String getPersonalDetailsDateOfBirth() {
        return preferences.getString(PERSONAL_DETAILS_DATE_OF_BIRTH, "");
    }

    public void setPersonalDetailsDateOfBirth(String dob) {
        editor.putString(PERSONAL_DETAILS_DATE_OF_BIRTH, dob).apply();
    }

    public String getPersonalDetailsMainCarer() {
        return preferences.getString(PERSONAL_DETAILS_MAIN_CARER, "");
    }

    public void setPersonalDetailsMainCarer(String name) {
        editor.putString(PERSONAL_DETAILS_MAIN_CARER, name).apply();
    }

    public String getPersonalDetailsCarerTel() {
        return preferences.getString(PERSONAL_DETAILS_CARER_TEL, "");
    }

    public void setPersonalDetailsCarerTel(String tel) {
        editor.putString(PERSONAL_DETAILS_CARER_TEL, tel).apply();
    }

    public boolean getPersonalDetailsNeedTranslator() {
        return preferences.getBoolean(PERSONAL_DETAILS_NEED_TRANSLATOR, false);
    }

    public void setPersonalDetailsNeedTranslator(boolean needTranslator) {
        editor.putBoolean(PERSONAL_DETAILS_NEED_TRANSLATOR, needTranslator).apply();
    }
}
