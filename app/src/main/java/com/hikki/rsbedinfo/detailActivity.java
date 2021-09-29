package com.hikki.rsbedinfo;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.hikki.rsbedinfo.model.detail_Covid;

import java.util.ArrayList;
import java.util.List;
import com.hikki.rsbedinfo.adapter.detailAdapter;
public class detailActivity extends AppCompatActivity {

    RecyclerView recycler;
    ShimmerFrameLayout shimmer;
    TextView judul, type;
    List<detail_Covid> data;
    detailAdapter adapter;
    String id,types;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        judul = findViewById(R.id.detailNama);
        type = findViewById(R.id.detailType);
        recycler = findViewById(R.id.detailRecycler);
        shimmer = findViewById(R.id.detailShimmer);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        types = intent.getStringExtra("type");
        judul.setText(intent.getStringExtra("judul"));
        LinearLayoutManager lm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(lm);
        if(types.equals("1")){
            //type covid
            type.setText("Type Covid-19");
        }
        else{
            type.setText("Type Non Covid-19");
        }
        getData();
    }

    public void getData(){
        if(!shimmer.isShimmerStarted()){
            shimmer.startShimmer();
            shimmer.setVisibility(View.VISIBLE);
        }
        AndroidNetworking.get(apiService.url_detail)
                .addPathParameter("id",id)
                .addPathParameter("type",types)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jo = response.getJSONObject("data");
                            JSONArray ja = jo.getJSONArray("bedDetail");
                            data = new ArrayList<>();
                            for(int i=0;i<ja.length();i++){
                                String time = ja.getJSONObject(i).getString("time");

                                data.add(new detail_Covid(time,  ja.getJSONObject(i).getJSONObject("stats").getString("title"),
                                        ja.getJSONObject(i).getJSONObject("stats").getString("bed_available"),
                                        ja.getJSONObject(i).getJSONObject("stats").getString("bed_empty"),
                                        ja.getJSONObject(i).getJSONObject("stats").getString("queue")));
                            }
                          //  Toast.makeText(detailActivity.this, String.valueOf(data.size()), Toast.LENGTH_SHORT).show();
                            adapter = new detailAdapter(getApplicationContext(),data);
                            shimmer.stopShimmer();
                            shimmer.setVisibility(View.GONE);
                            recycler.setVisibility(View.VISIBLE);
                            recycler.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            new SweetAlertDialog(detailActivity.this,SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText(e.getMessage())
                                    .show();
                            }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if(anError.getMessage().contains("dress")){
                            new SweetAlertDialog(detailActivity.this,SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Opps!")
                                    .setContentText("Koneksi error, Coba ulangi lagi")
                                    .show();
                        }
                    }
                });
    }
}