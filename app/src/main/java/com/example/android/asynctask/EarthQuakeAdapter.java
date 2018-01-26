package com.example.android.asynctask;


import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class EarthQuakeAdapter extends ArrayAdapter<EarthQuake>{
    private static final String SEPERATOR=" of ";
    Context mContext;
    public EarthQuakeAdapter(@NonNull Context context, @NonNull List<EarthQuake> earthQuakes) {
        super(context, 0, earthQuakes);
        mContext=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=LayoutInflater.from(mContext).inflate(R.layout.earthquake_list_item,parent,false);
            holder.magnituteTxt=(TextView)convertView.findViewById(R.id.magnitute);
            holder.locationPrimaryTxt=(TextView)convertView.findViewById(R.id.primary_location);
            holder.locationOffsetTxt=(TextView)convertView.findViewById(R.id.location_offset);
            holder.dateTxt=(TextView)convertView.findViewById(R.id.date);
            holder.timeTxt=(TextView)convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        }
        else{
             holder=(ViewHolder) convertView.getTag();
        }
            EarthQuake currentEarthQuake=getItem(position);

            double magnitute=currentEarthQuake.getMagnitute();
            holder.magnituteTxt.setText(formateMagnitute(magnitute));
            GradientDrawable magCircleColor=(GradientDrawable) holder.magnituteTxt.getBackground();
            magCircleColor.setColor(getMagnituteColor(magnitute));

        String orgionalLocation=currentEarthQuake.getLocation();
        String  offsetLocation;
        String primaryLocation;
        if(orgionalLocation.contains(SEPERATOR)){
            String parts[]=orgionalLocation.split(SEPERATOR);
            offsetLocation=parts[0]+SEPERATOR;
            primaryLocation=parts[1];
        }
        else{
            offsetLocation=mContext.getString(R.string.near);
            primaryLocation=orgionalLocation;
        }

        holder.locationOffsetTxt.setText(offsetLocation);
        holder.locationPrimaryTxt.setText(primaryLocation);

        long timeInMilliSec=currentEarthQuake.getTimeInMillisecond();
        holder.dateTxt.setText(getDate(timeInMilliSec));
        holder.timeTxt.setText(getTime(timeInMilliSec));

        return convertView;
    }

    private static class ViewHolder{
        TextView magnituteTxt;
        TextView locationPrimaryTxt;
        TextView locationOffsetTxt;
        TextView dateTxt;
        TextView timeTxt;
    }

    private String formateMagnitute(double mag){
        DecimalFormat formater=new DecimalFormat("0.0");
        return formater.format(mag);
    }

    private int getMagnituteColor(double mag){
        int magFloor=(int)Math.floor(mag);
        int colorResourceId;
        switch (magFloor){
            case 0:
            case 1:
                colorResourceId=R.color.magnitude1;
                break;
            case 2:
                colorResourceId=R.color.magnitude2;
                break;
            case 3:
                colorResourceId=R.color.magnitude3;
                break;
            case 4:
                colorResourceId=R.color.magnitude4;
                break;
            case 5:
                colorResourceId=R.color.magnitude5;
                break;
            case 6:
                colorResourceId=R.color.magnitude6;
                break;
            case 7:
                colorResourceId=R.color.magnitude7;
                break;
            case 8:
                colorResourceId=R.color.magnitude8;
                break;
            case 9:
                colorResourceId=R.color.magnitude9;
                break;
            default:
                colorResourceId=R.color.magnitude10plus;
        }
        return ContextCompat.getColor(mContext,colorResourceId);
    }

    private String getDate(long timeInMilliSecond){
        SimpleDateFormat formater=new SimpleDateFormat("LLL dd, yyyy");
        return formater.format(timeInMilliSecond);
    }

    private String getTime(Long timeInMilleSecond){
        SimpleDateFormat formater=new SimpleDateFormat("hh:mm a");
        return formater.format(timeInMilleSecond);
    }

}
