package com.cat.pianopatienttracker.admin_manager_regional.ranking;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.admin_manager_regional.ranking.ranking_details.RankingDetails;

import java.util.ArrayList;
import java.util.List;

public class Ranking_reps_adapter extends RecyclerView.Adapter<Ranking_reps_adapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";


    private Context mContext;
    private List<Ranking_reps_item> items;
    private String accessToken;
    private int selectedCountryId,selectedBrandId;

    public Ranking_reps_adapter(Context mContext, List<Ranking_reps_item> items, String accessToken, int selectedCountryId, int selectedBrandId) {
        this.mContext = mContext;
        this.items = items;
        this.accessToken = accessToken;
        this.selectedCountryId = selectedCountryId;
        this.selectedBrandId = selectedBrandId;
    }

    @NonNull
    @Override
    public Ranking_reps_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking_rep, parent, false);
        Ranking_reps_adapter.ViewHolder holder = new Ranking_reps_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Ranking_reps_adapter.ViewHolder holder, final int position) {
        //to log which item is failed
        Log.d(TAG, "onBindViewHolder: called.");

        holder.name.setText(items.get(position).getName());
        holder.patientsNo.setText(String.valueOf(items.get(position).getPatientsNo()));
        holder.rank.setText(String.valueOf(position + 1));
        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, RankingDetails.class);
                i.putExtra("accessToken",accessToken);
                i.putExtra("countryId",selectedCountryId);
                i.putExtra("brandId",selectedBrandId);
                i.putExtra("name",items.get(position).getName());
                i.putExtra("detailsType","reps");
                i.putExtra("itemId",items.get(position).getId());
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView name, patientsNo, rank;
        RelativeLayout parent_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            patientsNo = itemView.findViewById(R.id.patientsNo);
            rank = itemView.findViewById(R.id.rank);
            parent_layout = itemView.findViewById(R.id.parent_layout);
        }
    }
}