package com.kickbackapps.ghostcall.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kickbackapps.ghostcall.objects.BackgroundEffects.BackgroundObject;
import com.kickbackapps.ghostcall.R;

import java.util.List;

/**
 * Created by Ynott on 8/7/15.
 */
public class BackgroundAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<BackgroundObject> backgroundObjectList;
    private Context context;

    public BackgroundAdapter(Context context, List<BackgroundObject> backgroundObjectList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.backgroundObjectList = backgroundObjectList;
    }

    @Override
    public int getCount() {
        return backgroundObjectList.size();
    }

    @Override
    public Object getItem(int position) {
        return backgroundObjectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.bg_item, parent, false);
            holder = new ViewHolder();
            holder.backgroundImage = (ImageView) view.findViewById(R.id.bgImageGrid);
            holder.backgroundName = (TextView) view.findViewById(R.id.background_name);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        BackgroundObject backgroundObject = backgroundObjectList.get(position);
        holder.backgroundName.setText(backgroundObject.getBackgroundName());
        String lowerCaseBackgroundName = backgroundObject.getBackgroundName().toLowerCase();
        int resID = context.getResources().getIdentifier(lowerCaseBackgroundName , "drawable", context.getPackageName());
        holder.backgroundImage.setImageResource(resID);
        if (backgroundObject.getBackgroundName().equals("Static")) {
            holder.backgroundImage.setImageResource(R.drawable.statics);
        }

        if (backgroundObject.getBackgroundState() != null) {
            if (backgroundObject.getBackgroundState().equals("selected")) {
                holder.backgroundImage.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
            } else {
                holder.backgroundImage.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            }
        }


        return view;
    }

    private class ViewHolder {
        public TextView backgroundName;
        public ImageView backgroundImage;
    }
}
