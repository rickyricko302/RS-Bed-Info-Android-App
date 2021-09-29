package com.hikki.rsbedinfo;

import android.content.Context;
import androidx.core.app.ActivityCompat;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class message {
    private SweetAlertDialog sw;
    private Context c;
    public message(Context c){
        this.c = c;
    }


    public void error(String title, String msg){
        sw = new SweetAlertDialog(c, SweetAlertDialog.ERROR_TYPE);
        sw.setTitleText(title);
        sw.setContentText(msg);
        sw.show();
    }

    public void hide(){
        sw.hide();
    }
}
