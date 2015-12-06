package uk.co.jaspalsvoice.jv;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int START_TIMER_MESSAGE_CODE = 100;
    private static final long START_TIMER_MESSAGE_DELAY = 1000;
    private static final int KEY_MESSAGE_WHAT = 101;
    private static final int CLEAR_MESSAGE = 102;

    private static class KeypadHandler extends android.os.Handler {
        private WeakReference<MainActivity> activityWeakReference;
        private StringBuilder messageCodes;
        private MainActivity activity;
        private Map<String, String> letters;
        private boolean highlighted;

        public KeypadHandler(MainActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
            messageCodes = new StringBuilder();
            letters = populateLetters();
        }

        @Override
        public void handleMessage(Message msg) {
            activity = activityWeakReference.get();
            int msgCode = msg.arg1;
            Log.i(TAG, "handleMessage - START Message received: what[" + msg.what + "] arg1[" + msg.arg1 + "]");
            if (msg.what == CLEAR_MESSAGE) {
                clear();
            } else if (msg.what == START_TIMER_MESSAGE_CODE) {
                displayOnScreen();
            } else {
                removeMessages(START_TIMER_MESSAGE_CODE);
                sendEmptyMessageDelayed(START_TIMER_MESSAGE_CODE, START_TIMER_MESSAGE_DELAY);
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
            letters.put("0", "0 ");

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
                if (highlighted) {
                    String text = activity.messageTextView.getText().toString();
                    if (text.length() > 0) {
                        activity.messageTextView.setText(text.substring(0, text.length() - 1));
                    }
                }
                activity.messageTextView.append(currentLetter);
            }
            clear();
            Log.i(TAG, "displayOnScreen END");
        }

        private void highlightOnScreen() {
            String currentLetter = getCurrentLetter();
            Log.i(TAG, "highlightOnScreen - START current letter: " + currentLetter);
            if (currentLetter != null) {
                Spannable spannableContent = new SpannableString(currentLetter);
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
        }
    }

    private TextView messageTextView;
    private KeypadHandler handler;
    private int previousCode = -1;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new KeypadHandler(this);

        messageTextView = (TextView) findViewById(R.id.message_text);

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

        findViewById(R.id.clear_screen).setOnClickListener(clearScreenClickListener);
        findViewById(R.id.delete_letter).setOnClickListener(deleteLetterClickListener);
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
