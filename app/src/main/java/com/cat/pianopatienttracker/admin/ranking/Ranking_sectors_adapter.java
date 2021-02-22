package com.cat.pianopatienttracker.admin.ranking;

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

public class Ranking_sectors_adapter extends RecyclerView.Adapter<Ranking_sectors_adapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private List<Ranking_sectors_item> items;

    private Context mContext;

    public Ranking_sectors_adapter(Context context, ArrayList<Ranking_sectors_item> items) {

        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Ranking_sectors_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking_hc_sectors, parent, false);
        Ranking_sectors_adapter.ViewHolder holder = new Ranking_sectors_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Ranking_sectors_adapter.ViewHolder holder, final int position) {
        //to log which item is failed
        Log.d(TAG, "onBindViewHolder: called.");

        holder.name.setText(items.get(position).getName());
        holder.doctorsNo.setText(String.valueOf(items.get(position).getDoctorsNo()));
        holder.hospitalsNo.setText(String.valueOf(items.get(position).getHospitalsNo()));
        holder.rank.setText(String.valueOf(position + 1));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView name, doctorsNo,hospitalsNo, rank;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            doctorsNo = itemView.findViewById(R.id.doctorsNo);
            hospitalsNo = itemView.findViewById(R.id.hospitalsNo);
            rank = itemView.findViewById(R.id.rank);
        }
    }
}