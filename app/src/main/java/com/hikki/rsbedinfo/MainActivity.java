package com.hikki.rsbedinfo;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
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
import com.hikki.rsbedinfo.model.*;
import com.hikki.rsbedinfo.adapter.*;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    message msg;
    List<provinces> provinsi,kota,provclone,kotaclone;
    TextView tapp;
    RecyclerView recyclerProp;
    provinsiAdapter pad;
    NestedScrollView scroll;
    ConstraintLayout cons;
    kotaAdapter kotad;
    ShimmerFrameLayout shimmer;
    SearchView sv;
    Button prov;
    Boolean kotas;
    String g,provid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cons = findViewById(R.id.cons);
        tapp = findViewById(R.id.tapp);
        shimmer = findViewById(R.id.shimmer1);
        prov = findViewById(R.id.text_Provinsi);
        scroll = findViewById(R.id.scroll);
        sv = findViewById(R.id.sv);
        recyclerProp = findViewById(R.id.recycler_Provinsi);
        msg = new message(this);
        tapp.setPaintFlags(tapp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        LinearLayoutManager lm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerProp.setLayoutManager(lm);
        getProvince();
        provclone = new ArrayList<>();
        kotaclone = new ArrayList<>();

        prov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prov.setVisibility(View.GONE);
                recyclerProp.setVisibility(View.GONE);
                shimmer.setVisibility(View.VISIBLE);
                shimmer.startShimmer();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        kotas=false;
                        sv.setQuery("",false);
                        sv.setQueryHint("Filter provinsi");
                        sv.clearFocus();
                        cons.requestFocus();
                        shimmer.stopShimmer();
                        shimmer.setVisibility(View.GONE);
                        recyclerProp.setVisibility(View.VISIBLE);
                        recyclerProp.setAdapter(pad);
                    }
                },3000);

            }
        });
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals(null) || newText.equals("")){
                    if(kotas){
                        recyclerProp.setAdapter(kotad);
                    }
                    else{
                        recyclerProp.setAdapter(pad);
                    }
                }
                else{
                    if(kotas){
                        kotaclone.clear();
                        int ta=0;
                        for(provinces yu : kota){
                            if(yu.getNama().contains(newText)){
                                g=newText;
                                kotaclone.add(kota.get(ta));
                            }
                            ta++;
                        }
                        kotaAdapter kotadc = new kotaAdapter(getApplicationContext(),kotaclone);
                        recyclerProp.setAdapter(kotadc);
                        kotadc.setClick(new kotaAdapter.click() {
                            @Override
                            public void onItemClick(View v, int Position) {
                                Intent i = new Intent(MainActivity.this,bedActivity.class);
                                i.putExtra("provid",provid);
                                i.putExtra("kotaid",kotaclone.get(Position).getId());
                                i.putExtra("nama",kotaclone.get(Position).getNama());
                                startActivity(i);
                           //     Toast.makeText(MainActivity.this, String.valueOf(provid)+" "+kotaclone.get(Position).getId(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else{
                        provclone.clear();
                        int ta=0;
                        for(provinces yu : provinsi){
                            if(yu.getNama().contains(newText)){
                                g=newText;
                                provclone.add(provinsi.get(ta));
                            }
                            ta++;
                        }
                        provinsiAdapter padc = new provinsiAdapter(getApplicationContext(),provclone);
                        recyclerProp.setAdapter(padc);
                        padc.setClick(new provinsiAdapter.click() {
                            @Override
                            public void onItemClick(View v, int Position) {
                                recyclerProp.setVisibility(View.GONE);
                                prov.setVisibility(View.VISIBLE);
                                prov.setText(provclone.get(Position).getNama());
                                scroll.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        scroll.smoothScrollTo(0,0);
                                    }
                                });

                                provid = provclone.get(Position).getId();
                                getKota(provid);
                               // Toast.makeText(getApplicationContext(),provid,Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
                return false;
            }
        });
        tapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,aboutActivity.class);
                startActivity(i);
            }
        });
    }
    public void getProvince(){
        kotas = false;
        sv.setQueryHint("Filter provinsi");
        if(!shimmer.isShimmerStarted()){
            shimmer.setVisibility(View.VISIBLE);
            shimmer.startShimmer();
        }
        AndroidNetworking.get(apiService.url_Provinsi)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            provinsi = new ArrayList<>();
                            JSONArray ja = response.getJSONArray("provinces");
                            for(int i=0;i<ja.length();i++){
                                provinsi.add(new provinces(ja.getJSONObject(i).getString("id"),ja.getJSONObject(i).getString("name")));
                            }
                            shimmer.stopShimmer();
                            shimmer.setVisibility(View.GONE);
                            recyclerProp.setVisibility(View.VISIBLE);
                            pad = new provinsiAdapter(getApplicationContext(),provinsi);
                            recyclerProp.setAdapter(pad);
                            pad.setClick(new provinsiAdapter.click() {
                                @Override
                                public void onItemClick(View v, int Position) {
                                    recyclerProp.setVisibility(View.GONE);
                                    prov.setVisibility(View.VISIBLE);
                                    prov.setText(provinsi.get(Position).getNama());
                                    scroll.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            scroll.smoothScrollTo(0,0);
                                        }
                                    });
                                    provid = provinsi.get(Position).getId();
                                    getKota(provid);
                                   // Toast.makeText(getApplicationContext(),provid,Toast.LENGTH_LONG).show();
                                }
                            });
                           // Toast.makeText(MainActivity.this, String.valueOf(provinsi.size()), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            msg.error("Opps error",e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        if(anError.getMessage().contains("dress")){

                            Toast.makeText(MainActivity.this, "selesai, status tidak ada koneksi", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                });
    }
    public void getKota(String id){
        kotas = true;
        sv.setQuery("",false);
        sv.setQueryHint("Filter Kota / Kabupaten");
        sv.clearFocus();
        cons.requestFocus();
        if(!shimmer.isShimmerStarted()){
            shimmer.setVisibility(View.VISIBLE);
            shimmer.startShimmer();
        }
        AndroidNetworking.get(apiService.url_Kota+id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            kota = new ArrayList<>();
                            JSONArray ja = response.getJSONArray("cities");
                            for(int i =0;i<ja.length();i++){
                                kota.add(new provinces(ja.getJSONObject(i).getString("id"),ja.getJSONObject(i).getString("name")));
                            }
                            shimmer.stopShimmer();
                            shimmer.setVisibility(View.GONE);
                            kotad = new kotaAdapter(getApplicationContext(),kota);
                            recyclerProp.setVisibility(View.VISIBLE);
                            recyclerProp.setAdapter(kotad);
                            kotad.setClick(new kotaAdapter.click() {
                                @Override
                                public void onItemClick(View v, int Position) {
                                    Intent i = new Intent(MainActivity.this,bedActivity.class);
                                    i.putExtra("provid",provid);
                                    i.putExtra("kotaid",kota.get(Position).getId());
                                    i.putExtra("nama",kota.get(Position).getNama());
                                    startActivity(i);
                                   // Toast.makeText(MainActivity.this, String.valueOf(provid)+" "+kota.get(Position).getId(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } catch (JSONException e) {
                            msg.error("Error",e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        shimmer.stopShimmer();
                        shimmer.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Koneksi error", Toast.LENGTH_SHORT).show();
                        prov.setVisibility(View.GONE);
                        recyclerProp.setAdapter(pad);
                        recyclerProp.setVisibility(View.VISIBLE);
                    }
                });
    }
}