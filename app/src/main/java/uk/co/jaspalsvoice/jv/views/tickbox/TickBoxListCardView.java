package uk.co.jaspalsvoice.jv.views.tickbox;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import uk.co.jaspalsvoice.jv.R;

/**
 * Created by Ana on 2/21/2016.
 */
public class TickBoxListCardView extends CardView {
    private String[] data;
    private String title;
    private boolean editMode;

    private FrameLayout overlayView;
    private TextView titleView;
    private RecyclerView recyclerView;
    private ViewGroup buttonsView;
    private Button cancelBtn;
    private Button saveBtn;

    public TickBoxListCardView(Context context) {
        super(context);
        init(context);
    }

    public TickBoxListCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TickBoxListCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TickBoxListCardView, defStyleAttr, 0);
        try {
            CharSequence[] entries = a.getTextArray(R.styleable.TickBoxListCardView_entries);
            if (entries != null) {
                data = new String[entries.length];
                for (int i = 0; i < entries.length; i++) {
                    data[i] = entries[i].toString();
                }
            }
        } finally {
            a.recycle();
        }
        init(context);
    }

    private void init(final Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.tickbox_list_card_view, this);

        overlayView = (FrameLayout) root.findViewById(R.id.frame_overlay);
        titleView = (TextView) root.findViewById(R.id.title);
        recyclerView = (RecyclerView) root.findViewById(R.id.list);
        buttonsView = (ViewGroup) root.findViewById(R.id.buttons);
        cancelBtn = (Button) root.findViewById(R.id.cancel);
        saveBtn = (Button) root.findViewById(R.id.save);

        recyclerView.setAdapter(new TickBoxListAdapter(data));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setMinimumHeight(computeMinHeight());

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
                showNonEditMode();
            }
        });

        saveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public boolean isEditMode() {
        return editMode;
    }

    private void showNonEditMode() {
        overlayView.setVisibility(VISIBLE);
        recyclerView.setVisibility(VISIBLE);
        buttonsView.setVisibility(GONE);
        titleView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_action_edit), null);
    }

    private void showEditMode() {
        overlayView.setVisibility(GONE);
        titleView.setCompoundDrawables(null, null, null, null);
        recyclerView.setVisibility(VISIBLE);
        buttonsView.setVisibility(VISIBLE);
    }

    private int computeMinHeight() {
        return data.length * getResources().getDimensionPixelSize(R.dimen.list_card_item_min_height);
    }
}
