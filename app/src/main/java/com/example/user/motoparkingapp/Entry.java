package com.example.user.motoparkingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.user.motoparkingapp.network.CupoJSON;

public class Entry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        TextView plate = findViewById(R.id.entry_plate), locker = findViewById(R.id.entry_locker);
        Intent intent = getIntent();
        CupoJSON cupo = (CupoJSON) intent.getSerializableExtra("cupo");
        switch (cupo.getCodigoError()){
            case 0:
                plate.setText(cupo.getPlaca());
                String lockerString = cupo.getLocker();
                if(lockerString != null && !lockerString.isEmpty()){
                    locker.setText(lockerString);
                }else{
                    locker.setText("Sin Cascos");
                }
                break;
            case -1:
                plate.setText("Error");
                break;
            case 1:
                plate.setText("Datos invalidos");
                break;
        }

    }
}
