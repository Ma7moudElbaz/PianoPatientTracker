package com.cat.pianopatienttracker.rep.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.admin_manager_regional.dashboard.regional.ProductTarget_item;
import com.cat.pianopatienttracker.admin_manager_regional.shared.spinners.Country_item;
import com.cat.pianopatienttracker.rep.home.patients.PatientsActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RepHome_adapter extends RecyclerView.Adapter<RepHome_adapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    List<Country_item> country_items = new ArrayList<>();
    private List<RepHome_item> items;
    private String accessToken;

    private Context mContext;

    public RepHome_adapter(Context context, ArrayList<RepHome_item> items, String accessToken,
                           List<Country_item> country_items) {

        this.mContext = context;
        this.items = items;
        this.accessToken = accessToken;
        this.country_items = country_items;
    }

    @NonNull
    @Override
    public RepHome_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rep_home, parent, false);
        RepHome_adapter.ViewHolder holder = new RepHome_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RepHome_adapter.ViewHolder holder, final int position) {
        //to log which item is failed
        Log.d(TAG, "onBindViewHolder: called.");

        String targetNoString = items.get(position).getActualTarget() + " of " + items.get(position).getTotalTarget();
        String targetPercentString = items.get(position).getTargetPercent() + "%";
        String marketNoString = items.get(position).getActualMarket() + " of " + items.get(position).getTotalMarket();
        String marketPercentString = items.get(position).getMarketPercent() + "%";

        holder.name.setText(items.get(position).getName());
        holder.targetNo.setText(targetNoString);
        holder.targetPercent.setText(targetPercentString);
        holder.targetProgress.setProgress((int) items.get(position).getTargetPercent());
        holder.marketNo.setText(marketNoString);
        holder.marketPercent.setText(marketPercentString);
        holder.marketProgress.setProgress((int) items.get(position).getMarketPercent());

        String imageName = items.get(position).getName().toLowerCase() + "_logo";
        int imgDrawable = mContext.getResources().getIdentifier(imageName, "drawable", mContext.getPackageName());
        Glide.with(mContext).load(imgDrawable).into(holder.image);

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, PatientsActivity.class);
                i.putExtra("accessToken", accessToken);
                i.putExtra("brandId", items.get(position).getId());
                i.putExtra("brandName", items.get(position).getName());
                i.putExtra("countries", (Serializable) country_items);
                mContext.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView name, targetNo, targetPercent, marketNo, marketPercent;
        ContentLoadingProgressBar targetProgress, marketProgress;
        LinearLayout parent_layout;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            targetNo = itemView.findViewById(R.id.targetNo);
            targetPercent = itemView.findViewById(R.id.targetPercent);
            marketNo = itemView.findViewById(R.id.marketNo);
            marketPercent = itemView.findViewById(R.id.marketPercent);
            targetProgress = itemView.findViewById(R.id.targetProgress);
            marketProgress = itemView.findViewById(R.id.marketProgress);
            parent_layout = itemView.findViewById(R.id.parent_layout);
            image = itemView.findViewById(R.id.image);
        }
    }
}