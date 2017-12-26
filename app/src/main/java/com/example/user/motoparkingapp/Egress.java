package com.example.user.motoparkingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.motoparkingapp.network.CupoJSON;
import com.example.user.motoparkingapp.network.SolicitudCliente;
import com.example.user.motoparkingapp.services.CustomBinder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Egress extends AppCompatActivity {

    private CupoJSON cupo;
    private TextView cons, plate, locker, payment, ingress, hour, minute;
    private CustomBinder binder;

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
        binder = (CustomBinder) getIntent().getSerializableExtra("binder");
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

    public String formaterHora(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        return format.format(date);
    }

    public void cancel(View v){
        finish();
    }

    public void retirar(View v){
        binder.getService().requestEgress(new SolicitudCliente(2, cupo.getConsecutivo(), false), this);
    }

    public void imprimir(View v){
        binder.getService().requestEgress(new SolicitudCliente(2, cupo.getConsecutivo(), true), this);
    }

    public void egressedCallback(final CupoJSON response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cons.setText(String.valueOf(response.getConsecutivo()));
                plate.setText(response.getPlaca());
                String lockerString = response.getLocker();
                if(lockerString != null && !lockerString.isEmpty()){
                    locker.setText(lockerString);
                }else{
                    locker.setText("Sin Cascos");
                }
                payment.setText(String.valueOf(response.getCobro()));
                ingress.setText(formaterHora(new Date(response.getStart())));
                hour.setText(String.valueOf(response.getHoras()));
                minute.setText(String.valueOf(response.getMinutos()));
                Button print = findViewById(R.id.egressPrintBtn);
                Button retire = findViewById(R.id.egressRetireBtn);
                Button cancel = findViewById(R.id.egressCancelBtn);
                print.setVisibility(View.GONE);
                retire.setVisibility(View.GONE);
                cancel.setText("OK");
            }
        });
    }
}
