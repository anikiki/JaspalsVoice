package uk.co.jaspalsvoice.jv;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import uk.co.jaspalsvoice.jv.task.FetchWordsTask;
import uk.co.jaspalsvoice.jv.task.InitWordsTask;
import uk.co.jaspalsvoice.jv.task.InsertWordTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private String defaultSuggestion;
    private TextView currentTextCaseView;
    private EditText messageTextView;
    private TextView suggestions0View;
    private TextView suggestions1View;
    private TextView suggestions2View;
    private TextView suggestions3View;
    private KeypadHandler handler;
    private ShareActionProvider shareActionProvider;
    private Intent shareIntent = new Intent(Intent.ACTION_SEND);

    private FetchWordsTask.OnResultsListener onResultsListener = new FetchWordsTask.OnResultsListener() {
        @Override
        public void onUpdateUi(List<String> text) {
            suggestions0View.setText(text.size() >= 1 ? text.get(0) : defaultSuggestion);
            suggestions1View.setText(text.size() >= 2 ? text.get(1) : defaultSuggestion);
            suggestions2View.setText(text.size() >= 3 ? text.get(2) : defaultSuggestion);
            suggestions3View.setText(text.size() >= 4 ? text.get(3) : defaultSuggestion);
        }
    };

    private View.OnClickListener onSuggestionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String currentText = messageTextView.getText().toString();
            String suggestion = ((TextView) v).getText().toString();
            // Don't do any replacement if we have the default suggestion.
            if (!defaultSuggestion.equals(suggestion)) {
                handler.replaceWordWith(currentText, suggestion);
            }
        }
    };

    private View.OnClickListener keyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            handler.removeMessages(KeypadHandler.START_TIMER_MESSAGE_CODE);

            Message keyMessage = Message.obtain();
            keyMessage.what = KeypadHandler.KEY_MESSAGE_WHAT;
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
            handler.sendEmptyMessage(KeypadHandler.SET_TEXT_CASE);
        }
    };

    private View.OnClickListener clearScreenClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            resetHandlerState(KeypadHandler.CLEAR_MESSAGE);
            messageTextView.setText(null);
            showCursor();
        }
    };

    private View.OnClickListener deleteLetterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            resetHandlerState(KeypadHandler.CLEAR_LETTER);
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

    private KeypadHandler.Listener keyPadListener = new KeypadHandler.Listener() {
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
            new FetchWordsTask(getApplicationContext(), onResultsListener).execute(text);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defaultSuggestion = getString(R.string.no_suggestion);
        shareIntent.setType("text/plain");

        handler = new KeypadHandler(ContextCompat.getColor(this, R.color.colorAccent_40));
        handler.setListener(keyPadListener);

        currentTextCaseView = (TextView) findViewById(R.id.text_case);

        messageTextView = (EditText) findViewById(R.id.message_text);
        messageTextView.setTextIsSelectable(true);
        messageTextView.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        messageTextView.setCursorVisible(true);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_text_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        return super.onCreateOptionsMenu(menu);
    }

    private void resetHandlerState(int message) {
        handler.removeMessages(KeypadHandler.START_TIMER_MESSAGE_CODE);
        handler.removeMessages(KeypadHandler.KEY_MESSAGE_WHAT);
        handler.sendEmptyMessage(message);
    }

    private void showCursor() {
        messageTextView.setSelection(messageTextView.getText().length());
    }

}
