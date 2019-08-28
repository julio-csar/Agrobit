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
        }else{
            view=layoutInflater.inflate(R.layout.item_tareas,container,false);
        }

        container.addView(view,0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
