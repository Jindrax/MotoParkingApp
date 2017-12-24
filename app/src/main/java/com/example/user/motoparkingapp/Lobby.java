package com.example.user.motoparkingapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.user.motoparkingapp.adapters.CupoAdapter;
import com.example.user.motoparkingapp.network.ClientThread;
import com.example.user.motoparkingapp.network.CupoJSON;
import com.example.user.motoparkingapp.network.Joint;
import com.example.user.motoparkingapp.network.SolicitudCliente;

import java.util.ArrayList;
import java.util.List;

public class Lobby extends AppCompatActivity {

    public List<CupoJSON> historic;
    private ListView listEstado;
    private EditText plate, locker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        Joint.setLobby(this);
        startService(new Intent(this, ClientThread.class));
        listEstado = (ListView) findViewById(R.id.listState);
        listEstado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CupoJSON cupo =  (CupoJSON) parent.getItemAtPosition(position);
                new AsyncTask<CupoJSON, Void, Void>(){
                    @Override
                    protected Void doInBackground(CupoJSON... cupoJSONS) {
                        Joint.getCliente().sendTCP(cupoJSONS[0].getConsecutivo());
                        return null;
                    }
                }.execute(cupo);
            }
        });
        plate = (EditText) findViewById(R.id.plateEdit);
        locker = (EditText) findViewById(R.id.helmetEdit);
        historic = new ArrayList<>();
    }

    public void updateState(final List<CupoJSON> estado){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listEstado.setAdapter(new CupoAdapter(getBaseContext(), R.layout.cupo_item, estado));
            }
        });
    }

    public void showEntry(CupoJSON cupo){
        startActivity(new Intent(getBaseContext(), Entry.class).putExtra("cupo", cupo));
    }

    public void showEgress(CupoJSON cupo){
        startActivityForResult(new Intent(getBaseContext(), Egress.class).putExtra("cupo", cupo), 2);
    }

    public void showHistoric(){

    }

    public void process(View v){
        final SolicitudCliente solicitud = new SolicitudCliente();
        solicitud.setTipoSolicitud(0);
        solicitud.setPlaca(plate.getText().toString().toUpperCase().trim());
        solicitud.setCascos(Long.valueOf(locker.getText().toString()));
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                Joint.getCliente().sendTCP(solicitud);
                return null;
            }
        }.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}