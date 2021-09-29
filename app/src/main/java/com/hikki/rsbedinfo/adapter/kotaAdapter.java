package com.hikki.rsbedinfo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.google.android.material.card.MaterialCardView;
import com.hikki.rsbedinfo.R;
import com.hikki.rsbedinfo.model.provinces;
public class kotaAdapter extends RecyclerView.Adapter<kotaAdapter.ViewHolder> {
    private List<provinces> data;
    private Context c;
    private click cl;
    public kotaAdapter(Context c, List data){
        this.c=c;
        this.data=data;
    }
    @NonNull
    @Override
    public kotaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.item_prov,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull kotaAdapter.ViewHolder holder, int position) {
        provinces prop = data.get(position);
        holder.text.setText(String.valueOf(position+1)+". "+prop.getNama());

        if(position %2 !=0){
            holder.card.setCardBackgroundColor(Color.parseColor("#ffe1dd"));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        MaterialCardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.textProvinsi);
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
    public void setClick(kotaAdapter.click cl){
        this.cl=cl;
    }
    public interface click{
        void onItemClick(View v, int Position);
    }
}
