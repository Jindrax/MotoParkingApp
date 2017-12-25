package com.example.user.motoparkingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.example.user.motoparkingapp.adapters.CupoAdapter;
import com.example.user.motoparkingapp.network.ClientListener;
import com.example.user.motoparkingapp.network.ClientThread;
import com.example.user.motoparkingapp.network.CupoJSON;
import com.example.user.motoparkingapp.network.Joint;
import com.example.user.motoparkingapp.network.RespuestaServidor;
import com.example.user.motoparkingapp.network.SolicitudCliente;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Lobby extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public List<CupoJSON> historic;
    private ListView listEstado;
    private EditText plate, locker;
    private static AsyncTask hiloNet;
    private final Client cliente = new Client();

    private String getServerIp(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        return sharedPref.getString("server_ip", "192.168.0.2");
    }

    private void conectToServer(final String ip){
        if(hiloNet != null){
            hiloNet.cancel(true);
        }
        hiloNet = new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    cliente.connect(5000, ip, 8051);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        Joint.setLobby(this);
        Joint.setHistorico(new ArrayList<CupoJSON>());
        //startService(new Intent(this, ClientThread.class));
        Kryo kryo = cliente.getKryo();
        kryo.register(CupoJSON.class);
        kryo.register(SolicitudCliente.class);
        kryo.register(RespuestaServidor.class);
        kryo.register(ArrayList.class);
        cliente.addListener(new ClientListener());
        Joint.setCliente(cliente);
        cliente.start();
        System.setProperty("java.net.preferIPv4Stack" , "true");
        conectToServer(getServerIp());
        listEstado = findViewById(R.id.listState);
        listEstado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final CupoJSON cupo =  (CupoJSON) parent.getItemAtPosition(position);
                new AsyncTask<CupoJSON, Void, Void>(){
                    @Override
                    protected Void doInBackground(CupoJSON... cupoJSONS) {
                        Joint.getCliente().sendTCP(new SolicitudCliente(1, cupo.getConsecutivo()));
                        return null;
                    }
                }.execute(cupo);
            }
        });
        plate = findViewById(R.id.plateEdit);
        locker = findViewById(R.id.helmetEdit);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ip_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuIP:
                startActivity(new Intent(getBaseContext(), Settings.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        conectToServer(getServerIp());
        Log.i("Preferencias", "Se actualizan las preferencias");
    }

    @Override
    protected void onResume() {
        if(!cliente.isConnected()){
            conectToServer(getServerIp());
        }
        super.onResume();
    }
}