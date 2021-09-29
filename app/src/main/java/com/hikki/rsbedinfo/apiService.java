package com.hikki.rsbedinfo;

public interface apiService {
    String base_url = "https://rs-bed-covid-api.vercel.app";
    String url_Provinsi = base_url+"/api/get-provinces";
    String url_Kota = base_url+"/api/get-cities?provinceid=";
    String url_rs = base_url+"/api/get-hospitals?provinceid=";
    String url_detail = base_url+"/api/get-bed-detail?hospitalid={id}&type={type}";

}
