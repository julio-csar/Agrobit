package com.agrobit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.agrobit.R;
import com.agrobit.classes.ModelHome;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdapterTasks extends PagerAdapter {

    private List<Object> models;
    private LayoutInflater layoutInflater;
    private Context context;

    public AdapterTasks(List<Object> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=LayoutInflater.from(context);
        View view;
        if(position==0){
            view=layoutInflater.inflate(R.layout.item_resum_tareas,container,false);
            Date current= Calendar.getInstance().getTime();
            ((TextView)view.findViewById(R.id.mes)).setText(mes(current.getMonth()));
        }else{
            view=layoutInflater.inflate(R.layout.item_tareas,container,false);
        }

        container.addView(view,0);

        return view;
    }
    public String mes(int i){
        switch (i+1){
            case 1:
                return "Enero";
            case 2:
                return "Febrero";
            case 3:
                return "Marzo";
            case 4:
                return "Abril";
            case 5:
                return "Mayo";
            case 6:
                return "Junio";
            case 7:
                return "Julio";
            case 8:
                return "Agosto";
            case 9:
                return "Septiembre";
            case 10:
                return "Octubre";
            case 11:
                return "Noviembre";
            default:
                return "Diciembre";
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
