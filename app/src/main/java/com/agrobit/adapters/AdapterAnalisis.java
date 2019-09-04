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
import com.agrobit.classes.ModelAnalisis;
import com.agrobit.classes.ModelHome;

import java.util.List;

public class AdapterAnalisis extends PagerAdapter {

    private List<ModelAnalisis> models;
    private LayoutInflater layoutInflater;
    private Context context;

    public AdapterAnalisis(List<ModelAnalisis> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.item_analisis,container,false);

        ImageView imageView;
        TextView name,area,tec,fecha,hora;

        imageView=view.findViewById(R.id.ana_image);
        name=view.findViewById(R.id.ana_name);
        area=view.findViewById(R.id.ana_area);
        tec=view.findViewById(R.id.ana_cond);
        fecha=view.findViewById(R.id.ana_fecha);
        hora=view.findViewById(R.id.ana_hora);

        imageView.setImageResource(models.get(position).getImage());
        name.setText(models.get(position).getName());
        area.setText(models.get(position).getArea()+" has");
        tec.setText("Técnico "+models.get(position).getTec());
        fecha.setText(models.get(position).getFecha());
        hora.setText(models.get(position).getHora()+" hs");


        container.addView(view,0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
