package uk.co.jaspalsvoice.jv.views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import uk.co.jaspalsvoice.jv.R;

/**
 * Created by Ana on 2/21/2016.
 */
public class EditableCardView extends CardView {
    private String title;
    private String text;
    private boolean editMode;

    private TextView titleView;
    private TextView textView;
    private EditText editView;
    private ViewGroup buttonsView;
    private Button cancelBtn;
    private Button saveBtn;

    public EditableCardView(Context context) {
        super(context);
        init(context);
    }

    public EditableCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EditableCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.editable_card_view, this);

        titleView = (TextView) root.findViewById(R.id.title);
        textView = (TextView) root.findViewById(R.id.text);
        editView = (EditText) root.findViewById(R.id.edit);
        buttonsView = (ViewGroup) root.findViewById(R.id.buttons);
        cancelBtn = (Button) root.findViewById(R.id.cancel);
        saveBtn = (Button) root.findViewById(R.id.save);

        titleView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editMode = !editMode;

                if (editMode) {
                    showEditMode();
                } else {
                    showNonEditMode();
                }
            }
        });

        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setEdit(text);
                showNonEditMode();
            }
        });

        saveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setText(editView.getText().toString());
                showNonEditMode();
            }
        });
    }

    public void setTitle(String title) {
        this.title = title;
        titleView.setText(title);
    }

    public String getTitle() {
        return title;
    }

    public void setText(String text) {
        this.text = text;
        textView.setText(text);
    }

    public String getText() {
        return text;
    }

    public void setEdit(String text) {
        editView.setText(text);
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public boolean isEditMode() {
        return editMode;
    }

    private void showNonEditMode() {
        editView.setVisibility(GONE);
        buttonsView.setVisibility(GONE);
        textView.setVisibility(VISIBLE);
        titleView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_action_edit), null);
    }

    private void showEditMode() {
        titleView.setCompoundDrawables(null, null, null, null);
        textView.setVisibility(GONE);
        editView.setVisibility(VISIBLE);
        buttonsView.setVisibility(VISIBLE);
    }
}
