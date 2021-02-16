package com.cat.pianopatienttracker.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.admin.dashboard.Brand_item;

import java.util.ArrayList;

public class BrandsSpinnerAdapter extends BaseAdapter {
    Context context;
    ArrayList<Brand_item> items;
    LayoutInflater inflater;

    public BrandsSpinnerAdapter(Context applicationContext, ArrayList<Brand_item> items) {
        this.context = applicationContext;
        this.items = items;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_product_spinner, null);
        ImageView image =  view.findViewById(R.id.imageView);
        TextView name = view.findViewById(R.id.textView);

        name.setText(items.get(i).getName());
//        icon.setImageResource(items[i].getName);
        return view;
    }
}
