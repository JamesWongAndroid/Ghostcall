package com.tapfury.ghostcall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ynott on 7/8/15.
 */
public class GhostNumbersAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<GhostNumbers> ghostNumbersList;

    public GhostNumbersAdapter(Context context, List<GhostNumbers> ghostNumbersList) {

        layoutInflater = LayoutInflater.from(context);
        this.ghostNumbersList = ghostNumbersList;
    }

    @Override
    public int getCount() {

        return ghostNumbersList.size();
    }

    @Override
    public Object getItem(int position) {

        return ghostNumbersList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder holder;
        if(convertView == null) {
            view = layoutInflater.inflate(R.layout.ghost_number_row, parent, false);
            holder = new ViewHolder();
            holder.ghostName = (TextView) view.findViewById(R.id.ghost_name);
            holder.ghostNumber = (TextView) view.findViewById(R.id.ghost_number);
            holder.smsButton = (ImageView) view.findViewById(R.id.smsButton);
            holder.callButton = (ImageView) view.findViewById(R.id.callButton);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        GhostNumbers ghostNumbers = ghostNumbersList.get(position);
        holder.ghostName.setText(ghostNumbers.getGhostTitle());
        holder.ghostNumber.setText(ghostNumbers.getGhostNumber());

        return view;
    }

    private class ViewHolder {
        public TextView ghostName, ghostNumber;
        public ImageView smsButton, callButton;
    }

}
