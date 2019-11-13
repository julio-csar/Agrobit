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

public class AdapterHome extends PagerAdapter {

    private List<ModelHome> models;
    private LayoutInflater layoutInflater;
    private Context context;

    public AdapterHome(List<ModelHome> models, Context context) {
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
        View view;
            view = layoutInflater.inflate(R.layout.item_orchard, container, false);
            ImageView imageView,status;
            TextView name,desc,hora;

            imageView=view.findViewById(R.id.orchar_image);
            status=view.findViewById(R.id.status);
            name=view.findViewById(R.id.orchar_name);
            desc=view.findViewById(R.id.orchar_desc);
            hora=view.findViewById(R.id.hora);

            imageView.setImageResource(models.get(position).getImage());
            switch (models.get(position).getStatus()){
                case 1:
                    status.setImageResource(R.drawable.ic_f1);
                    break;
                case 2:
                    status.setImageResource(R.drawable.ic_f2);
                    break;
                case 3:
                    status.setImageResource(R.drawable.ic_f3);
                    break;
                case 4:
                    status.setImageResource(R.drawable.ic_f4);
                    break;
                case 5:
                    status.setImageResource(R.drawable.ic_f5);
                    break;
                case 6:
                    status.setImageResource(R.drawable.ic_f6);
                    break;
            }
            name.setText(models.get(position).getName());
            desc.setText(models.get(position).getDesc());
            hora.setText(models.get(position).getHora());
        container.addView(view,0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
