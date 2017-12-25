package com.example.user.motoparkingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.motoparkingapp.network.CupoJSON;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Egress extends AppCompatActivity {

    private CupoJSON cupo;
    private TextView cons, plate, locker, payment, ingress, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egress);
        cons = findViewById(R.id.egress_cons);
        plate = findViewById(R.id.egress_plate);
        locker = findViewById(R.id.egress_locker);
        payment = findViewById(R.id.egress_pay);
        ingress = findViewById(R.id.egress_start);
        hour = findViewById(R.id.egress_hour);
        minute = findViewById(R.id.egress_minute);
        cupo = (CupoJSON) getIntent().getSerializableExtra("cupo");
        cons.setText(String.valueOf(cupo.getConsecutivo()));
        plate.setText(cupo.getPlaca());
        String lockerString = cupo.getLocker();
        if(lockerString != null && !lockerString.isEmpty()){
            locker.setText(lockerString);
        }else{
            locker.setText("Sin Cascos");
        }
        payment.setText(String.valueOf(cupo.getCobro()));
        ingress.setText(formaterHora(new Date(cupo.getStart())));
        hour.setText(String.valueOf(cupo.getHoras()));
        minute.setText(String.valueOf(cupo.getMinutos()));
    }

    public static String formaterHora(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        return format.format(date);
    }

}
