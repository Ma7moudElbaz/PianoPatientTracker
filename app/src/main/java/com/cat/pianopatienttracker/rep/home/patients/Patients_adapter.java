package com.cat.pianopatienttracker.rep.home.patients;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;

import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.rep.home.RepHome_item;

import java.util.ArrayList;
import java.util.List;

public class Patients_adapter extends RecyclerView.Adapter<Patients_adapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private List<Patient_item> items;

    private Context mContext;

    public Patients_adapter(Context context, ArrayList<Patient_item> items) {

        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Patients_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient, parent, false);
        Patients_adapter.ViewHolder holder = new Patients_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Patients_adapter.ViewHolder holder, final int position) {
        //to log which item is failed
        Log.d(TAG, "onBindViewHolder: called.");

        holder.hospital.setText(items.get(position).getHospital());
        holder.sector.setText(items.get(position).getSector());
        holder.doctor.setText(items.get(position).getDoctor());
        holder.dose.setText(items.get(position).getDose());

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mContext, holder.more);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.patient_action, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(
                                mContext,
                                "You Clicked : " + item.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView hospital,sector,doctor,dose;
        ImageView more;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hospital = itemView.findViewById(R.id.hospital);
            sector = itemView.findViewById(R.id.sector);
            doctor = itemView.findViewById(R.id.doctor);
            dose = itemView.findViewById(R.id.dose);
            more = itemView.findViewById(R.id.more);
        }
    }
}