package com.kickbackapps.ghostcall.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kickbackapps.ghostcall.objects.GhostPackage;
import com.kickbackapps.ghostcall.R;

import java.util.List;


public class PackageAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<GhostPackage> ghostPackage;


    public PackageAdapter(Context context, List<GhostPackage> ghostPackage) {

        layoutInflater = LayoutInflater.from(context);
        this.ghostPackage = ghostPackage;
    }

    @Override
    public int getCount() {

        return ghostPackage.size();
    }

    @Override
    public Object getItem(int position) {

        return ghostPackage.get(position);
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
            view = layoutInflater.inflate(R.layout.package_row, parent, false);
            holder = new ViewHolder();
            holder.packageName = (TextView) view.findViewById(R.id.packageName);
            holder.packageTime = (TextView) view.findViewById(R.id.packageTime);
            holder.packagePrice = (TextView) view.findViewById(R.id.packagePrice);
            holder.packageCredit = (TextView) view.findViewById(R.id.packageCredits);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        GhostPackage gPackage = ghostPackage.get(position);
        holder.packageName.setText(gPackage.getPackageName());
        if (gPackage.getPackageType().equals("new")) {
            holder.packageTime.setText("Expires in " + gPackage.getPackageTime() + " days");
            holder.packageCredit.setText(" - " + gPackage.getPackageCredits() + " Minutes Free");
        } else if (gPackage.getPackageType().equals("extend")) {
            holder.packageTime.setText("Extends for " + gPackage.getPackageTime() + " days");
            holder.packageCredit.setText(" - " + gPackage.getPackageCredits() + " Minutes Free");
        } else if (gPackage.getPackageType().equals("credits")) {
            holder.packageTime.setText(gPackage.getPackageTime());
            holder.packageCredit.setText("");
        }
        holder.packagePrice.setText("$" + gPackage.getPackagePrice());


        return view;
    }

    private class ViewHolder {
        public TextView packageName, packagePrice, packageTime, packageCredit;
    }
}
