package com.kickbackapps.ghostcall.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kickbackapps.ghostcall.ApiObjects.HistoryObject;
import com.kickbackapps.ghostcall.R;

import java.util.List;

/**
 * Created by Ynott on 7/8/15.
 */
public class HistoryAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<HistoryObject> historyObjectList;
    Context context;

    public HistoryAdapter(Context context, List<HistoryObject> historyObjectList) {
        this.context = context;
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

    public List<HistoryObject> getData() { return historyObjectList; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        final ViewHolder holder;
        if(convertView == null) {
            view = layoutInflater.inflate(R.layout.history_row, parent, false);
            holder = new ViewHolder();
            holder.historyNumber = (TextView) view.findViewById(R.id.historyNumber);
            holder.historyDescription = (TextView) view.findViewById(R.id.historyDescription);
            holder.historyDate = (TextView) view.findViewById(R.id.historyDate);
            holder.historyTime = (TextView) view.findViewById(R.id.historyTime);
            holder.historyStatus = (ImageView) view.findViewById(R.id.historyStatusImage);
            holder.loadingPanel = (RelativeLayout) view.findViewById(R.id.loadingPanel);
            holder.progressBar = (ProgressBar) view.findViewById(R.id.spinProgressBar);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        final HistoryObject historyObject = historyObjectList.get(position);
        holder.historyNumber.setText(historyObject.getHistoryNumber());
        holder.historyDescription.setText(historyObject.getHistoryDescription());
        holder.historyDate.setText(historyObject.getHistoryDate());
        holder.historyTime.setText(historyObject.getHistoryTime());
        holder.progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        if (historyObject.getHistoryType() != null) {
            if(historyObject.getHistoryType().equals("call")) {
                if (historyObject.getHistoryDescription().equals("out")) {
                    holder.historyDescription.setText("outgoing call");
                    holder.historyStatus.setImageResource(R.drawable.call_outgoing);
                } else if (historyObject.getHistoryDescription().equals("in")) {
                    holder.historyDescription.setText("incoming call");
                    holder.historyStatus.setImageResource(R.drawable.call_incoming);
                    holder.historyNumber.setText(historyObject.getHistoryOutNumber());
                }
            } else if (historyObject.getHistoryType().equals("message")) {
                if (historyObject.getHistoryRecord().equals("received")) {
                    holder.historyStatus.setImageResource(R.drawable.sms_history);
                } else {
                    holder.historyStatus.setImageResource(R.drawable.replied);
                }
            } else if (historyObject.getHistoryType().equals("voicemail")) {
                holder.historyStatus.setImageResource(R.drawable.audio_play);
            }
        }
        else {
            holder.historyStatus.setImageResource(R.drawable.call_outgoing);
        }

        if (historyObject.getHistoryState().equals("playing")) {
            holder.historyStatus.setImageResource(R.drawable.audio_stop);
            holder.loadingPanel.setVisibility(View.GONE);
            holder.historyStatus.setVisibility(View.VISIBLE);
        } else if (historyObject.getHistoryState().equals("loading")) {
              holder.historyStatus.setVisibility(View.GONE);
            holder.loadingPanel.setVisibility(View.VISIBLE);
        } else if (historyObject.getHistoryState().equals("not_playing")) {
            holder.historyStatus.setImageResource(R.drawable.audio_play);
            holder.historyStatus.setVisibility(View.VISIBLE);
            holder.loadingPanel.setVisibility(View.GONE);
        }

        return view;
    }

    private class ViewHolder {
        public TextView historyNumber, historyDescription, historyDate, historyTime;
        public ImageView historyStatus;
        public RelativeLayout loadingPanel;
        public ProgressBar progressBar;
    }
}
