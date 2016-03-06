package uk.co.jaspalsvoice.jv.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.EditText;

import uk.co.jaspalsvoice.jv.R;

/**
 * Created by Ana on 3/3/2016.
 */
public class TickBoxListAdapter extends RecyclerView.Adapter<TickBoxListAdapter.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_OTHER = 1;

    private String[] data;

    public TickBoxListAdapter(String[] data) {
        this.data = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CheckedTextView checkedTextView;

        public ViewHolder(View view) {
            super(view);
            checkedTextView = (CheckedTextView) view.findViewById(R.id.item);
            checkedTextView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ((CheckedTextView) v).toggle();
                }
            });
        }
    }

    public static class OtherViewHolder extends ViewHolder {
        public EditText otherEditTextView;

        public OtherViewHolder(View view) {
            super(view);
            otherEditTextView = (EditText) view.findViewById(R.id.other);
            checkedTextView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckedTextView checkedView = (CheckedTextView) v;
                    checkedView.toggle();
                    otherEditTextView.setVisibility(checkedView.isChecked() ? View.VISIBLE : View.INVISIBLE);
                }
            });
        }
    }

    @Override
    public TickBoxListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_OTHER:
                View otherView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tickbox_other_item_row, parent, false);
                return new OtherViewHolder(otherView);
            case TYPE_ITEM:
            default:
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tickbox_item_row, parent, false);
                return new ViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(TickBoxListAdapter.ViewHolder holder, int position) {
        holder.checkedTextView.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position == getItemCount() - 1 ? TYPE_OTHER : TYPE_ITEM;
    }
}
