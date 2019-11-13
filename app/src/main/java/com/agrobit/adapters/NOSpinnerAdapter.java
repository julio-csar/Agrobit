package com.agrobit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.agrobit.R;
import com.agrobit.classes.NOSpinnerItem;

import java.util.ArrayList;

public class NOSpinnerAdapter extends ArrayAdapter<NOSpinnerItem> {
    public NOSpinnerAdapter(Context context,ArrayList<NOSpinnerItem> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.no_spinner_layout,parent,false);
        }
        NOSpinnerItem item=(NOSpinnerItem)getItem(position);
        ImageView imageView=convertView.findViewById(R.id.iv_spinner_item);
        TextView textView=convertView.findViewById(R.id.tv_spinner_item);

        if(item!=null){
            imageView.setImageResource(item.getImage());
            textView.setText(item.getItemText());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.no_dropdown_item,parent,false);
        }
        NOSpinnerItem item=(NOSpinnerItem)getItem(position);
        ImageView imageView=convertView.findViewById(R.id.iv_no_item);
        TextView textView=convertView.findViewById(R.id.tv_no_item);

        if(item!=null){
            imageView.setImageResource(item.getImage());
            textView.setText(item.getItemText());
        }
        return convertView;
    }
}
