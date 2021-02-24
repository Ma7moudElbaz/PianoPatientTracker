package com.cat.pianopatienttracker.regional_product.dashboard.regional;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;

import com.cat.pianopatienttracker.R;

import java.util.ArrayList;
import java.util.List;

public class ProductTarget_adapter extends RecyclerView.Adapter<ProductTarget_adapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private List<ProductTarget_item> items;

    private Context mContext;

    public ProductTarget_adapter(Context context, ArrayList<ProductTarget_item> items) {

        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ProductTarget_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_target, parent, false);
        ProductTarget_adapter.ViewHolder holder = new ProductTarget_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductTarget_adapter.ViewHolder holder, final int position) {
        //to log which item is failed
        Log.d(TAG, "onBindViewHolder: called.");

        String targetNoString = items.get(position).getActualTarget() + " of " + items.get(position).getTotalTarget();
        String targetPercentString = items.get(position).getTargetPercent() + "%" ;
        String marketNoString = items.get(position).getActualMarket() + " of " + items.get(position).getTotalMarket();
        String marketPercentString = items.get(position).getMarketPercent() + "%" ;

        holder.name.setText(items.get(position).getName());
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


        TextView name, targetNo, targetPercent, marketNo, marketPercent;
        ContentLoadingProgressBar targetProgress, marketProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            targetNo = itemView.findViewById(R.id.targetNo);
            targetPercent = itemView.findViewById(R.id.targetPercent);
            marketNo = itemView.findViewById(R.id.marketNo);
            marketPercent = itemView.findViewById(R.id.marketPercent);
            targetProgress = itemView.findViewById(R.id.targetProgress);
            marketProgress = itemView.findViewById(R.id.marketProgress);
        }
    }
}