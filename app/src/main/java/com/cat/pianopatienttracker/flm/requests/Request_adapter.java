package com.cat.pianopatienttracker.flm.requests;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.rep.home.patients.Patient_item;

import java.util.ArrayList;
import java.util.List;

public class Request_adapter extends RecyclerView.Adapter<Request_adapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private List<Request_item> items;

    private Context mContext;

    public Request_adapter(Context context, ArrayList<Request_item> items) {

        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Request_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        Request_adapter.ViewHolder holder = new Request_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Request_adapter.ViewHolder holder, final int position) {
        //to log which item is failed
        Log.d(TAG, "onBindViewHolder: called.");

        if (items.get(position).getProcess().equals("switched")){
            holder.switchLabel.setVisibility(View.VISIBLE);
            holder.dropLabel.setVisibility(View.GONE);
        }else if (items.get(position).getProcess().equals("dropped")){
            holder.switchLabel.setVisibility(View.GONE);
            holder.dropLabel.setVisibility(View.VISIBLE);
        }

        holder.hospital.setText(items.get(position).getHospital());
        holder.sector.setText(items.get(position).getSector());
        holder.doctor.setText(items.get(position).getDoctor());
        holder.dose.setText(items.get(position).getDose());
        holder.byName.setText("by "+items.get(position).getByName());


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView hospital,sector,doctor,dose,byName,confirm_btn;
        CardView switchLabel,dropLabel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hospital = itemView.findViewById(R.id.hospital);
            sector = itemView.findViewById(R.id.sector);
            doctor = itemView.findViewById(R.id.doctor);
            dose = itemView.findViewById(R.id.dose);
            byName = itemView.findViewById(R.id.byName);
            confirm_btn = itemView.findViewById(R.id.confirm_btn);
            switchLabel = itemView.findViewById(R.id.switch_label);
            dropLabel = itemView.findViewById(R.id.drop_label);
        }
    }
}