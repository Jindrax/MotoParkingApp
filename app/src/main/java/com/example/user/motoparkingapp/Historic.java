package com.example.user.motoparkingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.example.user.motoparkingapp.adapters.HistoricAdapter;
import com.example.user.motoparkingapp.network.Joint;

public class Historic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);
        ListView historicList = findViewById(R.id.historicList);
        historicList.setAdapter(new HistoricAdapter(getBaseContext(), R.layout.historic_item, Joint.getHistorico()));
    }

    public void close(View v){
        finish();
    }
}
