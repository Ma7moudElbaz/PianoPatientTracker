package com.cat.pianopatienttracker.admin_manager_regional.dashboard.regional;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cat.pianopatienttracker.R;

import java.util.ArrayList;
import java.util.List;

public class DashboardRegional_adapter extends RecyclerView.Adapter<DashboardRegional_adapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private List<DashboardRegional_item> items;

    private Context mContext;

    public DashboardRegional_adapter(Context context, ArrayList<DashboardRegional_item> items) {

        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public DashboardRegional_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard_regional, parent, false);
        DashboardRegional_adapter.ViewHolder holder = new DashboardRegional_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardRegional_adapter.ViewHolder holder, final int position) {
        //to log which item is failed
        Log.d(TAG, "onBindViewHolder: called.");

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        ProductTarget_adapter productTarget_adapter = new ProductTarget_adapter(mContext, items.get(position).getProductTarget_items());
        holder.brand_target_recycler.setLayoutManager(layoutManager);
        holder.brand_target_recycler.setAdapter(productTarget_adapter);

        holder.name.setText(items.get(position).getName());

        String imageName = "flag_"+items.get(position).getIso().toLowerCase();
        int imgDrawable = mContext.getResources().getIdentifier(imageName, "drawable", mContext.getPackageName());
        Glide.with(mContext).load(imgDrawable).into(holder.image);


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView name;
        RecyclerView brand_target_recycler;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            brand_target_recycler = itemView.findViewById(R.id.brand_target_recycler);
        }
    }
}