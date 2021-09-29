package com.hikki.rsbedinfo;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
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
import com.google.android.material.tabs.TabLayout;
import com.hikki.rsbedinfo.adapter.bedAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.hikki.rsbedinfo.model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.hikki.rsbedinfo.adapter.nonCovidAdapter;
public class bedActivity extends AppCompatActivity {
    nonCovidAdapter nad;
    bedAdapter bad;

    ShimmerFrameLayout shimmer;
    TabLayout tb;
    TextView nama;
    String provid,kotaid;
    List<hospital> data;
    List<non_Covid> data_nonCovid;
    RecyclerView recycler;
    ImageView bedend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bed);
        tb = findViewById(R.id.tablayout);
        recycler = findViewById(R.id.recyclerBed);
        Intent in = getIntent();
        provid = in.getStringExtra("provid");
        kotaid = in.getStringExtra("kotaid");
        nama =findViewById(R.id.bednama);
        shimmer = findViewById(R.id.shimmerbed);
        bedend= findViewById(R.id.bednend);
        nama.setText(in.getStringExtra("nama"));
        LinearLayoutManager lm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(lm);
        AndroidNetworking.initialize(getApplicationContext());
        getCovid("1");

        bedend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tb.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        if(data != null){
                            recycler.setAdapter(bad);
                        }
                        else{
                            getCovid("1");
                        }
                        break;
                    case 1:
                        if(data_nonCovid != null){
                            recycler.setAdapter(nad);
                        }
                        else{
                            getCovid("2");
                        }
                        break;
                }
               // Toast.makeText(bedActivity.this, String.valueOf(tab.getPosition()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    public void getCovid(String type){
        if(!shimmer.isShimmerStarted()){
            recycler.setVisibility(View.GONE);
            shimmer.setVisibility(View.VISIBLE);
            shimmer.startShimmer();
        }
        AndroidNetworking.get(apiService.url_rs+provid+"&cityid="+kotaid+"&type="+type)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray ja = response.getJSONArray("hospitals");
                            shimmer.stopShimmer();
                            shimmer.setVisibility(View.GONE);
                            File f = new File(getFilesDir().getAbsolutePath()+"/io");
                            if(!f.exists()){
                                f.mkdir();
                                new SweetAlertDialog(bedActivity.this,SweetAlertDialog.NORMAL_TYPE)
                                        .setTitleText("Info App")
                                        .setContentText("Geser kotak kekiri untuk opsi lainnya")
                                        .show();
                            }
                            if(type.equals("1")) {
                                data = new ArrayList<>();
                                for (int i = 0; i < ja.length(); i++) {
                                    String id = ja.getJSONObject(i).getString("id");
                                    String name = ja.getJSONObject(i).getString("name");
                                    String address = ja.getJSONObject(i).getString("address");
                                    String phone = ja.getJSONObject(i).getString("phone");
                                    String queue = ja.getJSONObject(i).getString("queue");
                                    String bed_availability = ja.getJSONObject(i).getString("bed_availability");
                                    String info = ja.getJSONObject(i).getString("info");
                                    data.add(new hospital(id, name, address, phone, queue, bed_availability, info));
                                }

                                bad = new bedAdapter(getApplicationContext(), data, bedActivity.this);
                                recycler.setVisibility(View.VISIBLE);
                                if(data.size() >0) {
                                    recycler.setAdapter(bad);
                                }
                                else{
                                    new SweetAlertDialog(bedActivity.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Maaf data tidak ditemukan :(")
                                            .setContentText("Silahkan cari lokasi lain")
                                            .show();
                                }
                            }
                            else{
                                data_nonCovid = new ArrayList<>();
                                for(int i = 0;i<ja.length();i++){
                                    String id = ja.getJSONObject(i).getString("id");
                                    String name = ja.getJSONObject(i).getString("name");
                                    String address = ja.getJSONObject(i).getString("address");
                                    String phone = ja.getJSONObject(i).getString("phone");
                                    List<bed_nonCovid> bed = new ArrayList<>();
                                    JSONArray jo = ja.getJSONObject(i).getJSONArray("available_beds");
                                    int jumlah = 0;
                                    String update = "";
                                    for(int j =0;j<jo.length();j++){
                                        try{
                                            update = jo.getJSONObject(j).getString("info");
                                            jumlah += Integer.parseInt(jo.getJSONObject(j).getString("available"));
                                        }catch(Exception e){
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    data_nonCovid.add(new non_Covid(id,name,address,phone,jumlah,update));
                                }
                                nad = new nonCovidAdapter(getApplicationContext(),data_nonCovid,bedActivity.this);
                                recycler.setVisibility(View.VISIBLE);
                                if(data_nonCovid.size() > 0) {
                                    recycler.setAdapter(nad);
                                }else{
                                    new SweetAlertDialog(bedActivity.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Maaf data tidak ditemukan :(")
                                            .setContentText("Silahkan cari lokasi lain")
                                            .show();
                                }

                            }
                        } catch (JSONException e) {
                            Toast.makeText(bedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        new SweetAlertDialog(bedActivity.this,SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Opps error")
                        .setContentText(anError.getMessage())
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        finish();
                                    }
                                }).show();
                    }
                });

    }
}