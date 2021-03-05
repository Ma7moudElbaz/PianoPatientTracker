package com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.ranking;

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

import com.novartis.global.gbl.onlinepatienttracker.R;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.ranking.ranking_details.HospitalDetailsActivity;

import java.util.List;

public class Ranking_hospitals_adapter extends RecyclerView.Adapter<Ranking_hospitals_adapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private Context mContext;
    private List<Ranking_hospitals_item> items;
    private String accessToken;
    private int selectedCountryId,selectedBrandId;


    public Ranking_hospitals_adapter(Context mContext, List<Ranking_hospitals_item> items, String accessToken, int selectedCountryId, int selectedBrandId) {
        this.mContext = mContext;
        this.items = items;
        this.accessToken = accessToken;
        this.selectedCountryId = selectedCountryId;
        this.selectedBrandId = selectedBrandId;
    }

    @NonNull
    @Override
    public Ranking_hospitals_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking_hospital, parent, false);
        Ranking_hospitals_adapter.ViewHolder holder = new Ranking_hospitals_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Ranking_hospitals_adapter.ViewHolder holder, final int position) {
        //to log which item is failed
        Log.d(TAG, "onBindViewHolder: called.");

        holder.name.setText(items.get(position).getName());
        holder.speciality.setText(items.get(position).getSector());
        holder.address.setText(items.get(position).getAddress());
        holder.doctorsNo.setText(String.valueOf(items.get(position).getDoctorsNo()));
        holder.patientsNo.setText(String.valueOf(items.get(position).getPatientsNo()));
        holder.rank.setText(String.valueOf(position+1));

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, HospitalDetailsActivity.class);
                i.putExtra("accessToken",accessToken);
                i.putExtra("countryId",selectedCountryId);
                i.putExtra("brandId",selectedBrandId);
                i.putExtra("name",items.get(position).getName());
                i.putExtra("detailsType","sectors");
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


        TextView name,speciality,address,rank,doctorsNo,patientsNo;
        RelativeLayout parent_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            speciality = itemView.findViewById(R.id.speciality);
            address = itemView.findViewById(R.id.address);
            doctorsNo = itemView.findViewById(R.id.doctorsNo);
            patientsNo = itemView.findViewById(R.id.patientsNo);
            rank = itemView.findViewById(R.id.rank);
            parent_layout = itemView.findViewById(R.id.parent_layout);
        }
    }
}