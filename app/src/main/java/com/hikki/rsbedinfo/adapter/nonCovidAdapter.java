package com.hikki.rsbedinfo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.hikki.rsbedinfo.R;
import com.hikki.rsbedinfo.detailActivity;
import com.hikki.rsbedinfo.model.*;
public class nonCovidAdapter extends RecyclerView.Adapter<nonCovidAdapter.ViewHolder> {
    private List<non_Covid> data;
    private Context c,ca;
    public nonCovidAdapter(Context c, List data, Context ca){
        this.c=c;
        this.ca=ca;
        this.data=data;
    }
    @NonNull
    @Override
    public nonCovidAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.item_noncovid,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull nonCovidAdapter.ViewHolder holder, int position) {
        holder.nama.setText(String.valueOf(position+1)+". "+ data.get(position).getName());
        holder.jalan.setText(data.get(position).getAddress());
        holder.update.setText(data.get(position).getUpdate());
        holder.tersedia.setText("Tersedia \n"+String.valueOf(data.get(position).getJumlah()));
        if(data.get(position).getJumlah() > 0){
            holder.status.setText("Status\nAda");
            holder.status.setTextColor(Color.parseColor("#0B8807"));
            holder.status.setBackground(c.getDrawable(R.drawable.adaitem));
        }
        else{
            holder.status.setText("Status\nPenuh");
            holder.status.setTextColor(Color.RED);
            holder.status.setBackground(c.getDrawable(R.drawable.penuhitem));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nama,jalan,tersedia,status,update;
        Button call,map,detail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.nonName);
            jalan = itemView.findViewById(R.id.nonJln);
            tersedia = itemView.findViewById(R.id.nonbedTersedia);
            status = itemView.findViewById(R.id.nonbedStatus);
            update = itemView.findViewById(R.id.nonbedUpdate);

            call = itemView.findViewById(R.id.Nonbedcall);
            map = itemView.findViewById(R.id.Nonbedmaps);
            detail = itemView.findViewById(R.id.Nonbeddeatil);
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_DIAL);
                    i.setData(Uri.parse("tel:"+data.get(getAdapterPosition()).getPhone()));
                    ca.startActivity(i);
                }
            });

            map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri guri = Uri.parse("geo:0,0?q="+data.get(getAdapterPosition()).getName()+", "+data.get(getAdapterPosition()).getAddress());
                    Intent i = new Intent(Intent.ACTION_VIEW,guri);
                    i.setPackage("com.google.android.apps.maps");
                    ca.startActivity(i);
                }
            });
            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ca, detailActivity.class);
                    i.putExtra("id",data.get(getAdapterPosition()).getId());
                    i.putExtra("type","2");
                    i.putExtra("judul",data.get(getAdapterPosition()).getName());
                    ca.startActivity(i);
                }
            });
        }
    }
}
