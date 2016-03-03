package uk.co.jaspalsvoice.jv;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ana on 1/17/2016.
 */
public class SuggestionsAdapter extends ArrayAdapter<String> {

    private List<String> objects;
    private Context context;
    private Listener listener;

    public SuggestionsAdapter(Context context, int resource, Listener listener) {
        super(context, resource);
        this.context = context;
        this.listener = listener;
    }

    public void setData(List<String> items) {
        this.objects = items;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            rowView = LayoutInflater.from(context).inflate(R.layout.suggestion, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) rowView.findViewById(R.id.suggestion);
            rowView.setTag(viewHolder);
        } else {
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("aaaaa", "onClick: " + objects.get(position));
                    listener.onItemClicked(objects.get(position));
                }
            });
        }
        ViewHolder holder = (ViewHolder) rowView.getTag();
        String s = objects.get(position);
        holder.text.setText(s);

        return rowView;
    }

    static class ViewHolder {
        TextView text;
    }

    @Override
    public String getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return objects == null ? 0 : objects.size();
    }

    public interface Listener {
        void onItemClicked(String text);
    }
}
