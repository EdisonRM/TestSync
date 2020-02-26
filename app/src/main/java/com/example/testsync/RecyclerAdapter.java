package com.example.testsync;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{

    private ArrayList<Contact> lstContact = new ArrayList<>();

    public RecyclerAdapter(ArrayList<Contact> lstContact)
    {
        this.lstContact = lstContact;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.Name.setText(lstContact.get(position).getcNombre());
        int sync_status = lstContact.get(position).getnSicronizado();

        if (sync_status == DbContract.SYNC_STATUS_OK)
        {
            holder.Sync_Status.setImageResource(R.drawable.ok);
        }
        else
        {
            holder.Sync_Status.setImageResource(R.drawable.sync);
        }
    }

    @Override
    public int getItemCount() {
        return this.lstContact.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView Sync_Status;
        TextView Name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Sync_Status = (ImageView)itemView.findViewById(R.id.imvIcono);
            Name = (TextView)itemView.findViewById(R.id.txtNombre);

        }
    }
}
