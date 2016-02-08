package uk.co.jaspalsvoice.jv;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.co.jaspalsvoice.jv.activities.AboutMeActivity;
import uk.co.jaspalsvoice.jv.activities.DiagnosisActivity;
import uk.co.jaspalsvoice.jv.activities.FoodAllergiesActivity;
import uk.co.jaspalsvoice.jv.activities.GpActivity;
import uk.co.jaspalsvoice.jv.activities.LikesDislikesActivity;
import uk.co.jaspalsvoice.jv.activities.MedicinesActivity;
import uk.co.jaspalsvoice.jv.activities.PersonalDetailsActivity;
import uk.co.jaspalsvoice.jv.task.FetchWordsTask;
import uk.co.jaspalsvoice.jv.task.HandlerConstants;
import uk.co.jaspalsvoice.jv.task.InitWordsTask;
import uk.co.jaspalsvoice.jv.task.InsertWordTask;
import uk.co.jaspalsvoice.jv.task.KeypadListener;

public class MainActivity extends AppCompatActivity implements SuggestionsAdapter.Listener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final boolean t9Enabled = true;
    private boolean keypadEnabled = true;

    private String defaultSuggestion;

    private TextView t9View;

    private TextView currentTextCaseView;
    private EditText messageTextView;

    private Layout messageTextLayout;

    private TextView suggestions0View;
    private TextView suggestions1View;
    private TextView suggestions2View;
    private TextView suggestions3View;

    private ViewGroup flipView;

    private TextView oneView;
    private TextView twoView;
    private TextView threeView;
    private TextView fourView;
    private TextView fiveView;
    private TextView sixView;
    private TextView sevenView;
    private TextView eightView;
    private TextView nineView;
    private TextView zeroView;
    private TextView starView;

    private PopupWindow popupWindow;
    //    private KeypadHandler handler;
//    private T9Handler t9Handler;
    private Handler currentHandler;
    private ShareActionProvider shareActionProvider;
    private Intent shareIntent = new Intent(Intent.ACTION_SEND);
    private SuggestionsAdapter suggestionsAdapter;

    private TrieT9 trieT9 = new TrieT9();

    private FetchWordsTask.OnResultsListener onResultsListener = new FetchWordsTask.OnResultsListener() {
        @Override
        public void onUpdateUi(List<String> text) {
//            suggestions0View.setText(text.size() >= 1 ? text.get(0) : defaultSuggestion);
//            suggestions1View.setText(text.size() >= 2 ? text.get(1) : defaultSuggestion);
//            suggestions2View.setText(text.size() >= 3 ? text.get(2) : defaultSuggestion);
//            suggestions3View.setText(text.size() >= 4 ? text.get(3) : defaultSuggestion);
        }
    };

    private View.OnClickListener onFlipListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            keypadEnabled = !keypadEnabled;
            ViewUtils.flipTextView(oneView, getString(R.string.key_sym_1), getString(R.string.key_personal_details));
            ViewUtils.flipTextView(twoView, getString(R.string.key_abc_2), getString(R.string.key_medicines));
            ViewUtils.flipTextView(threeView, getString(R.string.key_def_3), getString(R.string.key_medical_allergies));
            ViewUtils.flipTextView(fourView, getString(R.string.key_ghi_4), getString(R.string.key_food_allergies));
            ViewUtils.flipTextView(fiveView, getString(R.string.key_jkl_5), getString(R.string.key_gp));
            ViewUtils.flipTextView(sixView, getString(R.string.key_mno_6), getString(R.string.key_likes_dislikes));
            ViewUtils.flipTextView(sevenView, getString(R.string.key_pqrs_7), getString(R.string.key_diagnosis));
            ViewUtils.flipTextView(eightView, getString(R.string.key_tuv_8), getString(R.string.key_about_me));
            ViewUtils.flipTextView(nineView, getString(R.string.key_wxyz_9), getString(R.string.new_line));
            ViewUtils.flipTextView(zeroView, getString(R.string.key_space_0), getString(R.string.new_line));
            ViewUtils.flipTextView(starView, getString(R.string.key_letter_case), getString(R.string.new_line));
        }
    };

    private View.OnClickListener onSuggestionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String currentText = messageTextView.getText().toString();
            String suggestion = ((TextView) v).getText().toString();
            // Don't do any replacement if we have the default suggestion.
            if (!defaultSuggestion.equals(suggestion)) {
                if (t9Enabled) {
                    ((T9Handler) currentHandler).replaceWordWith(currentText, suggestion);
                } else {
                    ((KeypadHandler) currentHandler).replaceWordWith(currentText, suggestion);
                }
            }
        }
    };

    private View.OnClickListener keyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (keypadEnabled) {
                currentHandler.removeMessages(HandlerConstants.START_TIMER_MESSAGE_CODE);

                Message keyMessage = Message.obtain();
                keyMessage.what = HandlerConstants.KEY_MESSAGE_WHAT;
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

                currentHandler.sendMessage(keyMessage);
            } else {
                switch (v.getId()) {
                    case R.id.one:
                        startActivity(new Intent(MainActivity.this, PersonalDetailsActivity.class));
                        break;
                    case R.id.two:
                        startActivity(new Intent(MainActivity.this, MedicinesActivity.class));
                        break;
                    case R.id.three:
                        startActivity(new Intent(MainActivity.this, MedicinesActivity.class));
                        break;
                    case R.id.four:
                        startActivity(new Intent(MainActivity.this, FoodAllergiesActivity.class));
                        break;
                    case R.id.five:
                        startActivity(new Intent(MainActivity.this, GpActivity.class));
                        break;
                    case R.id.six:
                        startActivity(new Intent(MainActivity.this, LikesDislikesActivity.class));
                        break;
                    case R.id.seven:
                        startActivity(new Intent(MainActivity.this, DiagnosisActivity.class));
                        break;
                    case R.id.eight:
                        startActivity(new Intent(MainActivity.this, AboutMeActivity.class));
                        break;
                    case R.id.nine:
                        break;
                    case R.id.zero:
                        break;
                }
            }
        }
    };

    private View.OnClickListener changeCaseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            currentHandler.sendEmptyMessage(HandlerConstants.SET_TEXT_CASE);
        }
    };

    private View.OnClickListener clearScreenClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            resetHandlerState(HandlerConstants.CLEAR_MESSAGE, currentHandler);
            messageTextView.setText(null);
            showCursor();
            if (popupWindow != null) {
                popupWindow.dismiss();
            }
        }
    };

    private View.OnClickListener deleteLetterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            resetHandlerState(HandlerConstants.CLEAR_LETTER, currentHandler);
            String text = messageTextView.getText().toString();
            if (!TextUtils.isEmpty(text)) {
                messageTextView.setText(text.substring(0, text.length() - 1));
                showCursor();
            }
        }
    };

    private ActionMode.Callback selectionCallback = new ActionMode.Callback() {
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            menu.removeItem(android.R.id.selectAll);
            menu.removeItem(android.R.id.cut);
            menu.removeItem(android.R.id.copy);
            menu.removeItem(android.R.id.paste);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                menu.removeItem(android.R.id.replaceText);
                menu.removeItem(android.R.id.shareText);
            }
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
                    final CharSequence selectedText = messageTextView.getText().subSequence(min, max);
                    Log.i(TAG, "onActionItemClicked: selected text = " + selectedText);
                    new InsertWordTask(new WeakReference<Context>(MainActivity.this)).execute(selectedText.toString());
                    mode.finish();
                    // return true;
                default:
                    break;
            }
            return false;
        }

    };

    private KeypadListener keyPadListener = new KeypadListener() {
        @Override
        public void onAppendMessage(String msg) {
            messageTextView.append(msg);
            showCursor();
        }

        @Override
        public void onAppendMessage(Spannable msg) {
            messageTextView.append(msg);
            showCursor();
        }

        @Override
        public String getMessage() {
            return messageTextView.getText().toString();
        }

        @Override
        public void setMessage(String msg) {
            messageTextView.setText(msg);
            showCursor();
        }

        @Override
        public void clearSuggestions() {
            suggestions0View.setText(defaultSuggestion);
            suggestions1View.setText(defaultSuggestion);
            suggestions2View.setText(defaultSuggestion);
            suggestions3View.setText(defaultSuggestion);
        }

        @Override
        public void setCurrentCase(String textCase) {
            currentTextCaseView.setText(textCase);
        }

        @Override
        public void fetchWords(String text) {
            // new FetchWordsTask(getApplicationContext(), onResultsListener).execute(text);
            Log.i(TAG, "fetchWords: text = " + text);
            final List<String> list = trieT9.search(encodeWord(text.toLowerCase()));
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    Log.i(TAG, "fetchWords: " + list.get(i));
                }
            }

            final int pos = messageTextView.getSelectionStart();
            int location[] = new int[2];
            messageTextView.getLocationOnScreen(location);
            messageTextLayout = messageTextView.getLayout();
            int line = messageTextLayout.getLineForOffset(pos);
            int baseline = messageTextLayout.getLineBaseline(line);
            int ascent = messageTextLayout.getLineAscent(line);
            float x = messageTextLayout.getPrimaryHorizontal(pos);
            float y = baseline + ascent;

            final View actionView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.suggestions_popup, (ViewGroup) findViewById(R.id.suggestions));
            ListView suggestionsListView = (ListView) actionView;
            suggestionsListView.setAdapter(suggestionsAdapter);
            suggestionsAdapter.setData(list);

            if (popupWindow == null) {
                popupWindow = new PopupWindow(actionView, 300, 550, false);
            } else {
                popupWindow.dismiss();
            }
            popupWindow.showAtLocation(actionView, Gravity.TOP | Gravity.LEFT, (int) x + location[0] + 50, (int) y + location[1]);
        }
    };

    private TextWatcher messageWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // No action.
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // No action.
        }

        @Override
        public void afterTextChanged(Editable s) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, s.toString());
            shareActionProvider.setShareIntent(shareIntent);
        }
    };

    private static HashMap<Integer, Set<Character>> keyboard;
    private static Map<Character, Integer> charMapping;

    private static void initMap() {
        if (keyboard != null && charMapping != null) {
            return;
        }
        keyboard = new HashMap<>();
        charMapping = new HashMap<>();

        HashSet<Character> keys = new HashSet<>();
        keys.add('a');
        keys.add('b');
        keys.add('c');
        keyboard.put(2, keys);

        charMapping.put('a', 2);
        charMapping.put('b', 2);
        charMapping.put('c', 2);


        keys = new HashSet<>();
        keys.add('d');
        keys.add('e');
        keys.add('f');
        keyboard.put(3, keys);

        charMapping.put('d', 3);
        charMapping.put('e', 3);
        charMapping.put('f', 3);

        keys = new HashSet<>();
        keys.add('g');
        keys.add('h');
        keys.add('i');
        keyboard.put(4, keys);

        charMapping.put('g', 4);
        charMapping.put('h', 4);
        charMapping.put('i', 4);

        keys = new HashSet<>();
        keys.add('j');
        keys.add('k');
        keys.add('l');
        keyboard.put(5, keys);

        charMapping.put('j', 5);
        charMapping.put('k', 5);
        charMapping.put('l', 5);

        keys = new HashSet<>();
        keys.add('m');
        keys.add('n');
        keys.add('o');
        keyboard.put(6, keys);

        charMapping.put('m', 6);
        charMapping.put('n', 6);
        charMapping.put('o', 6);

        keys = new HashSet<>();
        keys.add('p');
        keys.add('q');
        keys.add('r');
        keys.add('s');
        keyboard.put(7, keys);

        charMapping.put('p', 7);
        charMapping.put('q', 7);
        charMapping.put('r', 7);
        charMapping.put('s', 7);

        keys = new HashSet<>();
        keys.add('t');
        keys.add('u');
        keys.add('v');
        keyboard.put(8, keys);

        charMapping.put('t', 8);
        charMapping.put('u', 8);
        charMapping.put('v', 8);

        keys = new HashSet<>();
        keys.add('w');
        keys.add('x');
        keys.add('y');
        keys.add('z');
        keyboard.put(9, keys);

        charMapping.put('w', 9);
        charMapping.put('x', 9);
        charMapping.put('y', 9);
        charMapping.put('z', 9);
    }

    private String encodeWord(String word) {
        initMap();
        StringBuilder encoded = new StringBuilder("");

        for (int i = 0; i < word.length(); i++) {
            encoded.append(String.valueOf(charMapping.get(word.charAt(i))));
        }

        return encoded.toString();
    }


    public TrieT9 getTrieT9() {
        return trieT9;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = getResources().openRawResource(R.raw.words);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                try {
                    while ((line = br.readLine()) != null) {
                        trieT9.add(encodeWord(line), line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "run: t9 loaded");
            }
        };

        (new Thread(r)).start();


        defaultSuggestion = getString(R.string.no_suggestion);
        shareIntent.setType("text/plain");

        t9View = (TextView) findViewById(R.id.t_9);
        t9View.setActivated(t9Enabled);
        flipView = (ViewGroup) findViewById(R.id.hash);
        flipView.setOnClickListener(onFlipListener);

        if (t9Enabled) {
            currentHandler = new T9Handler(ContextCompat.getColor(this, R.color.colorAccent_40));
            ((T9Handler) currentHandler).setListener(keyPadListener);
        } else {
            currentHandler = new KeypadHandler(ContextCompat.getColor(this, R.color.colorAccent_40));
            ((KeypadHandler) currentHandler).setListener(keyPadListener);
        }

        currentTextCaseView = (TextView) findViewById(R.id.text_case);

        messageTextView = (EditText) findViewById(R.id.message_text);
        messageTextView.setTextIsSelectable(true);
        messageTextView.setCursorVisible(true);
        messageTextView.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE);
        messageTextView.setText("");
        showCursor();
        messageTextView.setCustomSelectionActionModeCallback(selectionCallback);
        messageTextView.addTextChangedListener(messageWatcher);

        suggestions0View = (TextView) findViewById(R.id.suggestions_0);
        suggestions1View = (TextView) findViewById(R.id.suggestions_1);
        suggestions2View = (TextView) findViewById(R.id.suggestions_2);
        suggestions3View = (TextView) findViewById(R.id.suggestions_3);
        suggestions0View.setOnClickListener(onSuggestionListener);
        suggestions1View.setOnClickListener(onSuggestionListener);
        suggestions2View.setOnClickListener(onSuggestionListener);
        suggestions3View.setOnClickListener(onSuggestionListener);

        suggestionsAdapter = new SuggestionsAdapter(this, R.layout.suggestion, this);

        oneView = (TextView) findViewById(R.id.one);
        oneView.setOnClickListener(keyClickListener);
        twoView = (TextView) findViewById(R.id.two);
        twoView.setOnClickListener(keyClickListener);
        threeView = (TextView) findViewById(R.id.three);
        threeView.setOnClickListener(keyClickListener);
        fourView = (TextView) findViewById(R.id.four);
        fourView.setOnClickListener(keyClickListener);
        fiveView = (TextView) findViewById(R.id.five);
        fiveView.setOnClickListener(keyClickListener);
        sixView = (TextView) findViewById(R.id.six);
        sixView.setOnClickListener(keyClickListener);
        sevenView = (TextView) findViewById(R.id.seven);
        sevenView.setOnClickListener(keyClickListener);
        eightView = (TextView) findViewById(R.id.eight);
        eightView.setOnClickListener(keyClickListener);
        nineView = (TextView) findViewById(R.id.nine);
        nineView.setOnClickListener(keyClickListener);
        zeroView = (TextView) findViewById(R.id.zero);
        zeroView.setOnClickListener(keyClickListener);

        starView = (TextView) findViewById(R.id.star);
        starView.setOnClickListener(changeCaseClickListener);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_text_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        return super.onCreateOptionsMenu(menu);
    }

    private void resetHandlerState(int message, Handler h) {
        h.removeMessages(HandlerConstants.START_TIMER_MESSAGE_CODE);
        h.removeMessages(HandlerConstants.KEY_MESSAGE_WHAT);
        h.sendEmptyMessage(message);
    }

    private void showCursor() {
        messageTextView.setSelection(messageTextView.getText().length());
    }

    @Override
    public void onItemClicked(String text) {
        ((T9Handler) currentHandler).replaceWordWith(messageTextView.getText().toString(), text);
        popupWindow.dismiss();
    }
}
