package com.cat.pianopatienttracker.admin_manager_regional.ranking.ranking_details;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.admin_manager_regional.ranking.Ranking_hospitals_item;

import java.util.ArrayList;
import java.util.List;

public class RankingDetails_hospitals_adapter extends RecyclerView.Adapter<RankingDetails_hospitals_adapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private List<RankingDetails_hospitals_item> items;

    private Context mContext;

    public RankingDetails_hospitals_adapter(Context context, ArrayList<RankingDetails_hospitals_item> items) {

        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RankingDetails_hospitals_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking_hospital, parent, false);
        RankingDetails_hospitals_adapter.ViewHolder holder = new RankingDetails_hospitals_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RankingDetails_hospitals_adapter.ViewHolder holder, final int position) {
        //to log which item is failed
        Log.d(TAG, "onBindViewHolder: called.");

        holder.name.setText(items.get(position).getName());
        holder.speciality.setText(items.get(position).getSector());
        holder.address.setText(items.get(position).getAddress());
        holder.doctorsNo.setText(String.valueOf(items.get(position).getDoctorsNo()));
        holder.patientsNo.setText(String.valueOf(items.get(position).getPatientsNo()));
        holder.rank.setText(String.valueOf(position+1));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView name,speciality,address,rank,doctorsNo,patientsNo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            speciality = itemView.findViewById(R.id.speciality);
            address = itemView.findViewById(R.id.address);
            doctorsNo = itemView.findViewById(R.id.doctorsNo);
            patientsNo = itemView.findViewById(R.id.patientsNo);
            rank = itemView.findViewById(R.id.rank);
        }
    }
}