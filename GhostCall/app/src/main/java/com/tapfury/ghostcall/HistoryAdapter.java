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
public class HistoryAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<HistoryObject> historyObjectList;

    public HistoryAdapter(Context context, List<HistoryObject> historyObjectList) {
        layoutInflater = LayoutInflater.from(context);
        this.historyObjectList = historyObjectList;
    }

    @Override
    public int getCount() {
        return historyObjectList.size();
    }

    @Override
    public Object getItem(int position) {
        return historyObjectList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder holder;
        if(convertView == null) {
            view = layoutInflater.inflate(R.layout.history_row, parent, false);
            holder = new ViewHolder();
            holder.historyNumber = (TextView) view.findViewById(R.id.historyNumber);
            holder.historyDescription = (TextView) view.findViewById(R.id.historyDescription);
            holder.historyDate = (TextView) view.findViewById(R.id.historyDate);
            holder.historyTime = (TextView) view.findViewById(R.id.historyTime);
            holder.historyStatus = (ImageView) view.findViewById(R.id.historyStatusImage);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        HistoryObject historyObject = historyObjectList.get(position);
        holder.historyNumber.setText(historyObject.getHistoryNumber());
        holder.historyDescription.setText(historyObject.getHistoryDescription());
        holder.historyDate.setText(historyObject.getHistoryDate());
        holder.historyTime.setText(historyObject.getHistoryTime());

        if (historyObject.getHistoryType() != null) {
            if(historyObject.getHistoryType().equals("call")) {
                if (historyObject.getHistoryDescription().equals("out")) {
                    holder.historyStatus.setImageResource(R.drawable.call_outgoing);
                } else if (historyObject.getHistoryDescription().equals("in")) {
                    holder.historyStatus.setImageResource(R.drawable.call_incoming);
                }
            } else if (historyObject.getHistoryType().equals("message")) {
                holder.historyStatus.setImageResource(R.drawable.sms_history);
            } else if (historyObject.getHistoryType().equals("voicemail")) {
                holder.historyStatus.setImageResource(R.drawable.audio_play);
            }
        }
        else {
            holder.historyStatus.setImageResource(R.drawable.call_outgoing);
        }

        return view;
    }

    private class ViewHolder {
        public TextView historyNumber, historyDescription, historyDate, historyTime;
        public ImageView historyStatus;
    }
}
