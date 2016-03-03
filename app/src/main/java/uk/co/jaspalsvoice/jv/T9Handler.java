package uk.co.jaspalsvoice.jv;

import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import uk.co.jaspalsvoice.jv.task.HandlerConstants;
import uk.co.jaspalsvoice.jv.task.KeypadListener;

/**
 * Created by Ana on 12/27/2015.
 */
public class T9Handler extends android.os.Handler {

    private static final String TAG = T9Handler.class.getSimpleName();

    private StringBuilder messageCodes;
    private Map<String, String> letters;
    private boolean highlighted;
    private boolean uppercase;
    private int textCase;
    private String[] textCases;
    private String[] wordSeparators;
    private StringBuilder currentWord;
    private int previousCode = -1;
    private KeypadListener listener;
    private int highlightColor;

    public T9Handler(int highlightColor) {
        this.highlightColor = highlightColor;
        messageCodes = new StringBuilder();
        letters = populateLetters();
        uppercase = true;
        textCase = 0;
        textCases = new String[] {HandlerConstants.TEXT_CASE_Aa, HandlerConstants.TEXT_CASE_A, HandlerConstants.TEXT_CASE_a};
        wordSeparators = new String[] {" ", ".", ",", ";"};
        currentWord = new StringBuilder(100);
    }

    @Override
    public void handleMessage(Message msg) {
        int msgCode = msg.arg1;
        Log.i(TAG, "handleMessage - START Message received: what[" + msg.what + "] arg1[" + msg.arg1 + "]");
        if (msg.what == HandlerConstants.SET_TEXT_CASE) {
            displayOnScreen();
            textCase = (textCase < 2) ? (textCase + 1) : 0;
            setCurrentCase();
            return;
        }
        if (msg.what == HandlerConstants.CLEAR_MESSAGE) {
            clear();
            resetCurrentWord();
        } else if (msg.what == HandlerConstants.CLEAR_LETTER) {
            clear();
            updateCurrentWord();
            if (currentWord.length() > 0) {
                listener.fetchWords(currentWord.toString());
            }
        } else if (msg.what == HandlerConstants.START_TIMER_MESSAGE_CODE) {
            displayOnScreen();
        } else {
            removeMessages(HandlerConstants.START_TIMER_MESSAGE_CODE);
            sendEmptyMessageDelayed(HandlerConstants.START_TIMER_MESSAGE_CODE, HandlerConstants.START_TIMER_MESSAGE_DELAY);

            initUppercase();

            if (getPreviousCode() != -1 && getPreviousCode() != msgCode) {
                displayOnScreen();
            }
            messageCodes.append(msgCode);
            highlightOnScreen();
        }
        setPreviousCode(msgCode);
        Log.i(TAG, "handleMessage - END");
    }

    public void setListener(KeypadListener listener) {
        this.listener = listener;
    }

    public void replaceWordWith(String fullText, String suggestion) {
        // Add a space at the end of the word when a suggestion is selected.
        listener.setMessage(fullText.substring(0, fullText.length() - currentWord.length()) + suggestion + " ");
        resetCurrentWord();
    }

    private Map<String, String> populateLetters() {
        Map<String, String> letters = new HashMap<>();
        letters.put("1", ".,-?!'@:;/()1");
        letters.put("2", "ABC2");
        letters.put("3", "DEF3");
        letters.put("4", "GHI4");
        letters.put("5", "JKL5");
        letters.put("6", "MNO6");
        letters.put("7", "PQRS7");
        letters.put("8", "TUV8");
        letters.put("9", "WXYZ9");
        letters.put("0", " 0");

        return letters;
    }

    private void displayOnScreen() {
        if (messageCodes.length() == 0) {
            return;
        }
        String currentLetter = getCurrentLetter();
        Log.i(TAG, "displayOnScreen - START Message codes: " + messageCodes + " letter: " + currentLetter + " highlighted: " + highlighted);
        if (currentLetter == null) {
            listener.onAppendMessage(messageCodes.toString());
        } else {
            if (isEndOfWord(currentLetter)) {
                resetCurrentWord();
            } else {
                currentWord.append(currentLetter);
            }
            if (highlighted) {
                String text = listener.getMessage();
                if (text.length() > 0) {
                    listener.setMessage(text.substring(0, text.length() - 1));
                }
            }
            Log.i(TAG, "displayOnScreen: currentWord: " + currentWord);
            // Don't search in dictionary when the user types space / symbols.
            if (Character.isLetter(currentLetter.charAt(0)) && currentWord.length() > 0) {
                listener.fetchWords(currentWord.toString());
            }
            String textToDisplay = getTextToDisplayWithCorrectCase(currentLetter);
            listener.onAppendMessage(textToDisplay);
        }
        clear();
        Log.i(TAG, "displayOnScreen END");
    }

    private boolean isEndOfWord(String letter) {
        for (int i = 0; i < wordSeparators.length; i++) {
            if (wordSeparators[i].equals(letter)) {
                return true;
            }
        }
        return false;
    }

    private void resetCurrentWord() {
        currentWord.delete(0, currentWord.length());
        listener.clearSuggestions();
    }

    private void updateCurrentWord() {
        String word = currentWord.toString();
        currentWord.delete(0, currentWord.length());
        if (word.length() > 0) {
            currentWord.append(word.substring(0, word.length() - 1));
        }
        listener.clearSuggestions();
    }

    private String getTextToDisplayWithCorrectCase(String text) {
        String currentCase = textCases[textCase];
        if (HandlerConstants.TEXT_CASE_Aa.equals(currentCase)) {
            if (uppercase) {
                text = text.toUpperCase();
            } else {
                text = text.toLowerCase();
            }
        } else if (HandlerConstants.TEXT_CASE_A.equals(currentCase)) {
            text = text.toUpperCase();
        } else if (HandlerConstants.TEXT_CASE_a.equals(currentCase)) {
            text = text.toLowerCase();
        }
        return text;
    }

    private void initUppercase() {
        if (TextUtils.isEmpty(listener.getMessage()) && HandlerConstants.TEXT_CASE_Aa.equals(textCases[textCase])) {
            uppercase = true;
        }
    }

    private void highlightOnScreen() {
        String currentLetter = getCurrentLetter();
        Log.i(TAG, "highlightOnScreen - START current letter: " + currentLetter);
        if (currentLetter != null) {
            String textToDisplay = getTextToDisplayWithCorrectCase(currentLetter);
            Spannable spannableContent = new SpannableString(textToDisplay);
            spannableContent.setSpan(new BackgroundColorSpan(highlightColor), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            String text = listener.getMessage();
            if (messageCodes.length() > 1) {
                listener.setMessage(text.substring(0, text.length() - 1));
            }
            listener.onAppendMessage(spannableContent);
            highlighted = true;
        }
        Log.i(TAG, "highlightOnScreen - END");
    }

    private String getCurrentLetter() {
        if (messageCodes.toString().length() == 0) {
            return null;
        }
        String first = messageCodes.substring(0, 1);
        String group = letters.get(first);
        if (group == null) {
            return null;
        }
        int groupLength = group.length();
        char[] lettersInGroup = group.toCharArray();
        int position = messageCodes.toString().length() % groupLength - 1;
        if (position < 0) {
            position = groupLength - 1;
        }
        return lettersInGroup[position] + "";
    }

    private void clear() {
        messageCodes.delete(0, messageCodes.length());
        highlighted = false;
        uppercase = false;
    }

    private void setCurrentCase() {
        if (HandlerConstants.TEXT_CASE_Aa.equals(textCases[textCase])) {
            uppercase = true;
        }
        listener.setCurrentCase(textCases[textCase]);
    }


    private int getPreviousCode() {
        return previousCode;
    }

    private void setPreviousCode(int previousCode) {
        this.previousCode = previousCode;
    }
}
