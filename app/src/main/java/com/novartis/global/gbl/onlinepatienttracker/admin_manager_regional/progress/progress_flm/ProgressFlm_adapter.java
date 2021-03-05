package com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.progress.progress_flm;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;

import com.novartis.global.gbl.onlinepatienttracker.R;

import java.util.ArrayList;
import java.util.List;

public class ProgressFlm_adapter extends RecyclerView.Adapter<ProgressFlm_adapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private List<ProgressFlm_item> items;

    private Context mContext;

    public ProgressFlm_adapter(Context context, ArrayList<ProgressFlm_item> items) {

        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ProgressFlm_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress_flm, parent, false);
        ProgressFlm_adapter.ViewHolder holder = new ProgressFlm_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressFlm_adapter.ViewHolder holder, final int position) {
        //to log which item is failed
        Log.d(TAG, "onBindViewHolder: called.");

        String targetNoString = items.get(position).getActualTarget() + " of " + items.get(position).getTotalTarget();
        String targetPercentString = items.get(position).getTargetPercent() + "%" ;
        String marketNoString = items.get(position).getActualMarket() + " of " + items.get(position).getTotalMarket();
        String marketPercentString = items.get(position).getMarketPercent() + "%" ;

        holder.name.setText(items.get(position).getName());
        holder.address.setText(items.get(position).getAddress());
        holder.targetNo.setText(targetNoString);
        holder.targetPercent.setText(targetPercentString);
        holder.targetProgress.setProgress((int) items.get(position).getTargetPercent());
        holder.marketNo.setText(marketNoString);
        holder.marketPercent.setText(marketPercentString);
        holder.marketProgress.setProgress((int) items.get(position).getMarketPercent());


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView name,address, targetNo, targetPercent, marketNo, marketPercent;
        ContentLoadingProgressBar targetProgress, marketProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            targetNo = itemView.findViewById(R.id.targetNo);
            targetPercent = itemView.findViewById(R.id.targetPercent);
            marketNo = itemView.findViewById(R.id.marketNo);
            marketPercent = itemView.findViewById(R.id.marketPercent);
            targetProgress = itemView.findViewById(R.id.targetProgress);
            marketProgress = itemView.findViewById(R.id.marketProgress);
        }
    }
}