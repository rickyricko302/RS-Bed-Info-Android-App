package com.hikki.rsbedinfo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.hikki.rsbedinfo.R;
import com.hikki.rsbedinfo.model.*;
import java.util.List;

public class provinsiAdapter extends RecyclerView.Adapter<provinsiAdapter.ViewHolder> {
    private Context c;
    private List<provinces> data;
    private int lastPosition = -1;
    private click cl;
    public provinsiAdapter(Context c, List data){
        this.c=c;
        this.data=data;
    }
    @NonNull
    @Override
    public provinsiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.item_prov,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull provinsiAdapter.ViewHolder holder, int position) {
        provinces prop = data.get(position);
        holder.provinsi.setText(String.valueOf(position+1)+". "+prop.getNama());

        if(position %2 !=0){
            holder.card.setCardBackgroundColor(Color.parseColor("#ffe1dd"));
        }
       // setAnimation(holder.card,position);
    }
    private void setAnimation(View vt, int pos){
        if(pos > lastPosition){
            Animation anim = AnimationUtils.loadAnimation(c, android.R.anim.slide_in_left);
            vt.startAnimation(anim);
            lastPosition = pos;
        }
        else{
            Animation anim = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
            vt.startAnimation(anim);
        }
    }
    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView provinsi;
        private MaterialCardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            provinsi = itemView.findViewById(R.id.textProvinsi);
            card = itemView.findViewById(R.id.mycard);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(cl != null) cl.onItemClick(view,getAdapterPosition());
                }
            });
        }
    }
    public provinces getItem(int id){
        return data.get(id);
    }
    public void setClick(click cl){
        this.cl=cl;
    }
    public interface click{
        void onItemClick(View v, int Position);
    }
}
