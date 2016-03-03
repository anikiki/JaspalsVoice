package uk.co.jaspalsvoice.jv.task;

import android.text.Spannable;

/**
 * Created by Ana on 1/9/2016.
 */
public interface KeypadListener {
    void onAppendMessage(String msg);

    void onAppendMessage(Spannable msg);

    String getMessage();

    void setMessage(String msg);

    void clearSuggestions();

    void setCurrentCase(String textCase);

    void fetchWords(String text);
}
