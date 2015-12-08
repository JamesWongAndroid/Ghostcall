package com.kickbackapps.ghostcall.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kickbackapps.ghostcall.R;
import com.kickbackapps.ghostcall.objects.SmsObject;

import java.util.List;

/**
 * Created by Ynott on 8/24/15.
 */
public class SmsAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<SmsObject> smsObjectList;
    Context context;
    public static final int IN_LAYOUT = 0;
    public static final int OUT_LAYOUT = 1;

    public SmsAdapter(Context context, List<SmsObject> smsObjectList) {
        this.context = context;
        this.smsObjectList = smsObjectList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return smsObjectList.size();
    }

    @Override
    public Object getItem(int position) {
        return smsObjectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<SmsObject> getData() {
        return smsObjectList;
    }

    @Override
    public int getItemViewType(int position) {
        if (smsObjectList.get(position).getMessageDirection().equals("out")) {
            return OUT_LAYOUT;
        } else {
            return IN_LAYOUT;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        int type = getItemViewType(position);

        if (convertView == null) {
//            if (smsObjectList.get(position).getMessageDirection().equals("out")) {
//                view = layoutInflater.inflate(R.layout.sms_out_layout, parent, false);
//            } else {
//                view = layoutInflater.inflate(R.layout.sms_in_layout, parent, false);
//            }
            if (type == OUT_LAYOUT) {
                convertView = layoutInflater.inflate(R.layout.sms_out_layout, parent, false);
            } else {
                convertView = layoutInflater.inflate(R.layout.sms_in_layout, parent, false);
            }

            holder = new ViewHolder();
            holder.avatarImage = (TextView) convertView.findViewById(R.id.avatarView);
            holder.messageText = (TextView) convertView.findViewById(R.id.message_view);
            holder.messageDate = (TextView) convertView.findViewById(R.id.date_view);
            holder.messageBlock = (LinearLayout) convertView.findViewById(R.id.message_block);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final SmsObject smsObject = smsObjectList.get(position);

//            final SmsObject smsObjectPrevious = smsObjectList.get(position - 1);
//            DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM dd, yyyy hh:mm a");
//            DateTime dateTimePrevious = formatter.parseDateTime(smsObjectPrevious.getMessageDate());
//            DateTime dateTimeCurrent = formatter.parseDateTime(smsObject.getMessageDate());
//            Minutes minute = Minutes.minutesBetween(dateTimePrevious, dateTimeCurrent);
//            int difference = minute.getMinutes();
            holder.messageDate.setText(smsObject.getMessageDate());

        holder.messageText.setText(smsObject.getMessageText());

        if (smsObject.getMessageDirection().equals("out")) {
            holder.messageText.getBackground().setColorFilter(context.getResources().getColor(R.color.titleblue), PorterDuff.Mode.SRC_ATOP);
            holder.messageText.setTextColor(context.getResources().getColor(R.color.white));
        } else if (smsObject.getMessageDirection().equals("in")) {
            holder.messageText.getBackground().setColorFilter(context.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            holder.messageText.setTextColor(context.getResources().getColor(R.color.black));
        }

        return convertView;
    }

    private class ViewHolder {
        public TextView messageText, messageDate;
        public TextView avatarImage;
        public LinearLayout messageBlock;
    }
}
