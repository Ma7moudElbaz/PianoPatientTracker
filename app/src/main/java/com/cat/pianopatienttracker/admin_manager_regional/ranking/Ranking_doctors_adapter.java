package com.cat.pianopatienttracker.admin_manager_regional.ranking;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cat.pianopatienttracker.R;

import java.util.ArrayList;
import java.util.List;

public class Ranking_doctors_adapter extends RecyclerView.Adapter<Ranking_doctors_adapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private List<Ranking_doctors_item> items;

    private Context mContext;

    public Ranking_doctors_adapter(Context context, ArrayList<Ranking_doctors_item> items) {

        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Ranking_doctors_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking_doctors, parent, false);
        Ranking_doctors_adapter.ViewHolder holder = new Ranking_doctors_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Ranking_doctors_adapter.ViewHolder holder, final int position) {
        //to log which item is failed
        Log.d(TAG, "onBindViewHolder: called.");

        holder.name.setText(items.get(position).getName());
        holder.hospital.setText(items.get(position).getHospital());
        holder.patientsNo.setText(String.valueOf(items.get(position).getPatientsNo()));
        holder.address.setText(items.get(position).getAddress());
        holder.rank.setText(String.valueOf(position + 1));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView name, hospital,patientsNo,address, rank;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            hospital = itemView.findViewById(R.id.hospital);
            patientsNo = itemView.findViewById(R.id.patientsNo);
            address = itemView.findViewById(R.id.address);
            rank = itemView.findViewById(R.id.rank);
        }
    }
}