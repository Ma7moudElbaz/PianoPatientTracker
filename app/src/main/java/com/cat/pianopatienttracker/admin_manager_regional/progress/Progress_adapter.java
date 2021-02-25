package com.cat.pianopatienttracker.admin_manager_regional.progress;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;

import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.admin_manager_regional.progress.progress_flm.ProgressFlmActivity;

import java.util.ArrayList;
import java.util.List;

public class Progress_adapter extends RecyclerView.Adapter<Progress_adapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private List<Progress_item> items;

    private Context mContext;
    private int countryId,brandId;

    public Progress_adapter(Context context, ArrayList<Progress_item> items,int countryId,int brandId) {

        this.mContext = context;
        this.items = items;
        this.countryId = countryId;
        this.brandId = brandId;
    }

    @NonNull
    @Override
    public Progress_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false);
        Progress_adapter.ViewHolder holder = new Progress_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Progress_adapter.ViewHolder holder, final int position) {
        //to log which item is failed
        Log.d(TAG, "onBindViewHolder: called.");

        String targetNoString = items.get(position).getActualTarget() + " of " + items.get(position).getTotalTarget();
        String targetPercentString = items.get(position).getTargetPercent() + "%" ;
        String marketNoString = items.get(position).getActualMarket() + " of " + items.get(position).getTotalMarket();
        String marketPercentString = items.get(position).getMarketPercent() + "%" ;

        holder.name.setText(items.get(position).getName());
        holder.repsNo.setText(String.valueOf(items.get(position).getRepsNo()));
        holder.address.setText(items.get(position).getAddress());
        holder.targetNo.setText(targetNoString);
        holder.targetPercent.setText(targetPercentString);
        holder.targetProgress.setProgress((int) items.get(position).getTargetPercent());
        holder.marketNo.setText(marketNoString);
        holder.marketPercent.setText(marketPercentString);
        holder.marketProgress.setProgress((int) items.get(position).getMarketPercent());

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ProgressFlmActivity.class);
                i.putExtra("flmId",items.get(position).getId());
                i.putExtra("countryId",countryId);
                i.putExtra("brandId",brandId);
                i.putExtra("flmName",items.get(position).getName());
                mContext.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView name,repsNo,address, targetNo, targetPercent, marketNo, marketPercent;
        ContentLoadingProgressBar targetProgress, marketProgress;
        LinearLayout parent_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            repsNo = itemView.findViewById(R.id.reps_no);
            address = itemView.findViewById(R.id.address);
            targetNo = itemView.findViewById(R.id.targetNo);
            targetPercent = itemView.findViewById(R.id.targetPercent);
            marketNo = itemView.findViewById(R.id.marketNo);
            marketPercent = itemView.findViewById(R.id.marketPercent);
            targetProgress = itemView.findViewById(R.id.targetProgress);
            marketProgress = itemView.findViewById(R.id.marketProgress);
            parent_layout = itemView.findViewById(R.id.parent_layout);
        }
    }
}