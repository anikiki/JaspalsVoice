package uk.co.jaspalsvoice.jv;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.jaspalsvoice.jv.task.FetchWordsTask;
import uk.co.jaspalsvoice.jv.task.InitWordsTask;
import uk.co.jaspalsvoice.jv.task.InsertWordTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int START_TIMER_MESSAGE_CODE = 100;
    private static final long START_TIMER_MESSAGE_DELAY = 1000;
    private static final int KEY_MESSAGE_WHAT = 101;
    private static final int CLEAR_MESSAGE = 102;
    private static final int SET_TEXT_CASE = 103;

    private static final String TEXT_CASE_Aa = "Aa";
    private static final String TEXT_CASE_A = "A";
    private static final String TEXT_CASE_a = "a";

    private static class KeypadHandler extends android.os.Handler {
        private WeakReference<MainActivity> activityWeakReference;
        private StringBuilder messageCodes;
        private MainActivity activity;
        private Map<String, String> letters;
        private boolean highlighted;
        private boolean uppercase;
        private int textCase;
        private String[] textCases;
        private String[] wordSeparators;
        private StringBuilder currentWord;

        public KeypadHandler(MainActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
            messageCodes = new StringBuilder();
            letters = populateLetters();
            uppercase = true;
            textCase = 0;
            textCases = new String[] {TEXT_CASE_Aa, TEXT_CASE_A, TEXT_CASE_a};
            wordSeparators = new String[] {" ", ".", ",", ";"};
            currentWord = new StringBuilder(100);
        }

        @Override
        public void handleMessage(Message msg) {
            activity = activityWeakReference.get();
            int msgCode = msg.arg1;
            Log.i(TAG, "handleMessage - START Message received: what[" + msg.what + "] arg1[" + msg.arg1 + "]");
            if (msg.what == SET_TEXT_CASE) {
                displayOnScreen();
                textCase = (textCase < 2) ? (textCase + 1) : 0;
                setCurrentCase();
                return;
            }
            if (msg.what == CLEAR_MESSAGE) {
                clear();
                resetCurrentWord();
            } else if (msg.what == START_TIMER_MESSAGE_CODE) {
                displayOnScreen();
            } else {
                removeMessages(START_TIMER_MESSAGE_CODE);
                sendEmptyMessageDelayed(START_TIMER_MESSAGE_CODE, START_TIMER_MESSAGE_DELAY);

                initUppercase();

                if (activity.getPreviousCode() != -1 && activity.getPreviousCode() != msgCode) {
                    displayOnScreen();
                }
                messageCodes.append(msgCode);
                highlightOnScreen();
            }
            activity.setPreviousCode(msgCode);
            Log.i(TAG, "handleMessage - END");
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
                activity.messageTextView.append(messageCodes.toString());
            } else {
                if (isEndOfWord(currentLetter)) {
                    resetCurrentWord();
                } else {
                    currentWord.append(currentLetter);
                }
                if (highlighted) {
                    String text = activity.messageTextView.getText().toString();
                    if (text.length() > 0) {
                        activity.messageTextView.setText(text.substring(0, text.length() - 1));
                    }
                }
                Log.i(TAG, "displayOnScreen: currentWord: " + currentWord);
                new FetchWordsTask(activity.getApplicationContext(), activity.onResultsListener).execute(currentWord.toString());
                String textToDisplay = getTextToDisplayWithCorrectCase(currentLetter);
                activity.messageTextView.append(textToDisplay);
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
            activity.suggestions0View.setText(null);
            activity.suggestions1View.setText(null);
            activity.suggestions2View.setText(null);
            activity.suggestions3View.setText(null);
        }

        private String getTextToDisplayWithCorrectCase(String text) {
            String currentCase = textCases[textCase];
            if (TEXT_CASE_Aa.equals(currentCase)) {
                if (uppercase) {
                    text = text.toUpperCase();
                } else {
                    text = text.toLowerCase();
                }
            } else if (TEXT_CASE_A.equals(currentCase)) {
                text = text.toUpperCase();
            } else if (TEXT_CASE_a.equals(currentCase)) {
                text = text.toLowerCase();
            }
            return text;
        }

        private void initUppercase() {
            if (TextUtils.isEmpty(activity.messageTextView.getText().toString()) && TEXT_CASE_Aa.equals(textCases[textCase])) {
                uppercase = true;
            }
        }

        private void highlightOnScreen() {
            String currentLetter = getCurrentLetter();
            Log.i(TAG, "highlightOnScreen - START current letter: " + currentLetter);
            if (currentLetter != null) {
                String textToDisplay = getTextToDisplayWithCorrectCase(currentLetter);
                Spannable spannableContent = new SpannableString(textToDisplay);
                spannableContent.setSpan(new BackgroundColorSpan(Color.RED), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                String text = activity.messageTextView.getText().toString();
                if (messageCodes.length() > 1) {
                    activity.messageTextView.setText(text.substring(0, text.length() - 1));
                }
                activity.messageTextView.append(spannableContent);
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
            if (TEXT_CASE_Aa.equals(textCases[textCase])) {
                uppercase = true;
            }
            activity.currentTextCaseView.setText(textCases[textCase]);
        }
    }

    private TextView currentTextCaseView;
    private TextView messageTextView;
    private TextView suggestions0View;
    private TextView suggestions1View;
    private TextView suggestions2View;
    private TextView suggestions3View;
    private KeypadHandler handler;
    private int previousCode = -1;

    private FetchWordsTask.OnResultsListener onResultsListener = new FetchWordsTask.OnResultsListener() {
        @Override
        public void onUpdateUi(List<String> text) {
            suggestions0View.setText(text.size() >= 1 ? text.get(0) : null);
            suggestions1View.setText(text.size() >= 2 ? text.get(1) : null);
            suggestions2View.setText(text.size() >= 3 ? text.get(2) : null);
            suggestions3View.setText(text.size() >= 4 ? text.get(3) : null);
        }
    };

    private View.OnClickListener onSuggestionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener keyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            handler.removeMessages(START_TIMER_MESSAGE_CODE);

            Message keyMessage = Message.obtain();
            keyMessage.what = KEY_MESSAGE_WHAT;
            switch (v.getId()) {
                case R.id.one:
                    keyMessage.arg1 = 1;
                    break;
                case R.id.two:
                    keyMessage.arg1 = 2;
                    break;
                case R.id.three:
                    keyMessage.arg1 = 3;
                    break;
                case R.id.four:
                    keyMessage.arg1 = 4;
                    break;
                case R.id.five:
                    keyMessage.arg1 = 5;
                    break;
                case R.id.six:
                    keyMessage.arg1 = 6;
                    break;
                case R.id.seven:
                    keyMessage.arg1 = 7;
                    break;
                case R.id.eight:
                    keyMessage.arg1 = 8;
                    break;
                case R.id.nine:
                    keyMessage.arg1 = 9;
                    break;
                case R.id.zero:
                    keyMessage.arg1 = 0;
                    break;
            }

            handler.sendMessage(keyMessage);
        }
    };

    private View.OnClickListener changeCaseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            handler.sendEmptyMessage(SET_TEXT_CASE);
        }
    };

    private View.OnClickListener clearScreenClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            resetHandlerState();
            messageTextView.setText(null);
        }
    };

    private View.OnClickListener deleteLetterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            resetHandlerState();
            String text = messageTextView.getText().toString();
            if (!TextUtils.isEmpty(text)) {
                messageTextView.setText(text.substring(0, text.length() - 1));
            }
        }
    };

    private ActionMode.Callback selectionCallback = new ActionMode.Callback() {

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            menu.removeItem(android.R.id.selectAll);
            menu.removeItem(android.R.id.cut);
            menu.removeItem(android.R.id.copy);
            menu.add(0, 0, 0, "Add to dictionary").setIcon(R.drawable.add_to_dictionary);
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case 0:
                    int min = 0;
                    int max = messageTextView.getText().length();
                    if (messageTextView.isFocused()) {
                        final int selStart = messageTextView.getSelectionStart();
                        final int selEnd = messageTextView.getSelectionEnd();

                        min = Math.max(0, Math.min(selStart, selEnd));
                        max = Math.max(0, Math.max(selStart, selEnd));
                    }
                    // Perform your definition lookup with the selected text
                    final CharSequence selectedText = messageTextView.getText().subSequence(min, max);
                    Log.i(TAG, "onActionItemClicked: selected text = " + selectedText);
                    new InsertWordTask(new WeakReference<Context>(MainActivity.this)).execute(selectedText.toString());
                    // Finish and close the ActionMode
                    mode.finish();
                    return true;
                default:
                    break;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new KeypadHandler(this);

        currentTextCaseView = (TextView) findViewById(R.id.text_case);
        messageTextView = (TextView) findViewById(R.id.message_text);
        messageTextView.setCustomSelectionActionModeCallback(selectionCallback);
        suggestions0View = (TextView) findViewById(R.id.suggestions_0);
        suggestions1View = (TextView) findViewById(R.id.suggestions_1);
        suggestions2View = (TextView) findViewById(R.id.suggestions_2);
        suggestions3View = (TextView) findViewById(R.id.suggestions_3);

        findViewById(R.id.one).setOnClickListener(keyClickListener);
        findViewById(R.id.two).setOnClickListener(keyClickListener);
        findViewById(R.id.three).setOnClickListener(keyClickListener);
        findViewById(R.id.four).setOnClickListener(keyClickListener);
        findViewById(R.id.five).setOnClickListener(keyClickListener);
        findViewById(R.id.six).setOnClickListener(keyClickListener);
        findViewById(R.id.seven).setOnClickListener(keyClickListener);
        findViewById(R.id.eight).setOnClickListener(keyClickListener);
        findViewById(R.id.nine).setOnClickListener(keyClickListener);
        findViewById(R.id.zero).setOnClickListener(keyClickListener);

        findViewById(R.id.star).setOnClickListener(changeCaseClickListener);

        findViewById(R.id.clear_screen).setOnClickListener(clearScreenClickListener);
        findViewById(R.id.delete_letter).setOnClickListener(deleteLetterClickListener);

        new InitWordsTask(getApplicationContext()).execute(getResources());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Context Menu");
        menu.add(0, v.getId(), 0, "Action 1");
    }

    public int getPreviousCode() {
        return previousCode;
    }

    public void setPreviousCode(int previousCode) {
        this.previousCode = previousCode;
    }

    private void resetHandlerState() {
        handler.removeMessages(START_TIMER_MESSAGE_CODE);
        handler.removeMessages(KEY_MESSAGE_WHAT);
        handler.sendEmptyMessage(CLEAR_MESSAGE);
    }
}
