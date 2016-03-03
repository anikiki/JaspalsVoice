package uk.co.jaspalsvoice.jv.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import uk.co.jaspalsvoice.jv.R;

/**
 * Created by Ana on 3/3/2016.
 */
public class TickBoxListAdapter extends RecyclerView.Adapter<TickBoxListAdapter.ViewHolder> {

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

    @Override
    public TickBoxListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tickbox_item_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TickBoxListAdapter.ViewHolder holder, int position) {
        holder.checkedTextView.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }
}
