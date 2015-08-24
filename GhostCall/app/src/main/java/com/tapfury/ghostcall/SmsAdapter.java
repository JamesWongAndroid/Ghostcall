package com.tapfury.ghostcall;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ynott on 8/24/15.
 */
public class SmsAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<SmsObject> smsObjectList;
    Context context;

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        final ViewHolder holder;

        if (convertView == null) {
            if (smsObjectList.get(position).getMessageDirection().equals("to")) {
                view = layoutInflater.inflate(R.layout.sms_out_layout, parent, false);
            } else {
                view = layoutInflater.inflate(R.layout.sms_in_layout, parent, false);
            }

            holder = new ViewHolder();
            holder.avatarImage = (ImageView) view.findViewById(R.id.avatarView);
            holder.messageText = (TextView) view.findViewById(R.id.message_view);
            holder.messageDate = (TextView) view.findViewById(R.id.date_view);
            holder.messageBlock = (LinearLayout) view.findViewById(R.id.message_block);

            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        final SmsObject smsObject = smsObjectList.get(position);
        holder.messageText.setText(smsObject.getMessageText());
        holder.messageDate.setText(smsObject.getMessageDate());

        if (smsObject.getMessageDirection().equals("to")) {
            holder.messageText.getBackground().setColorFilter(context.getResources().getColor(R.color.titleblue), PorterDuff.Mode.SRC_ATOP);
            holder.messageText.setTextColor(context.getResources().getColor(R.color.white));
            ((RelativeLayout.LayoutParams) holder.messageBlock.getLayoutParams()).setMargins(0, 0, 0, 0);
            holder.avatarImage.setVisibility(View.GONE);
        }

        return view;
    }

    private class ViewHolder {
        public TextView messageText, messageDate;
        public ImageView avatarImage;
        public LinearLayout messageBlock;
    }
}
