package com.example.user.motoparkingapp;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.user.motoparkingapp.adapters.CupoAdapter;
import com.example.user.motoparkingapp.network.CupoJSON;
import com.example.user.motoparkingapp.network.SolicitudCliente;
import com.example.user.motoparkingapp.services.ClientService;
import com.example.user.motoparkingapp.services.CustomBinder;

import java.util.List;

public class Lobby extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ListView listEstado;
    private EditText plate, locker;
    private CustomBinder binder;
    private ServiceConnection serviceCon;

    private String getServerIp(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        return sharedPref.getString("server_ip", "192.168.0.2");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        System.setProperty("java.net.preferIPv4Stack" , "true");
        listEstado = findViewById(R.id.listState);
        listEstado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CupoJSON cupo =  (CupoJSON) parent.getItemAtPosition(position);
                binder.getService().requestProspect(new SolicitudCliente(1, cupo.getConsecutivo()));
            }
        });
        plate = findViewById(R.id.plateEdit);
        locker = findViewById(R.id.helmetEdit);
        serviceCon = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                binder = (CustomBinder) iBinder;
                binder.getService().startServer(Lobby.this, getServerIp());
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                binder = null;
            }
        };
        bindService(new Intent(getBaseContext(), ClientService.class), serviceCon, BIND_AUTO_CREATE);
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
        Intent i = new Intent(getBaseContext(), Egress.class);
        i.putExtra("cupo", cupo);
        i.putExtra("binder", binder);
        startActivity(i);
    }

    public void showHistoric(){

    }

    public void process(View v) {
        String placaTxt = plate.getText().toString().toUpperCase().trim();
        long cascos = 0;
        if (!locker.getText().toString().isEmpty()) {
            cascos = Long.valueOf(locker.getText().toString());
        }
        if (placaTxt.isEmpty()) {
            binder.getService().requestProspect(new SolicitudCliente(1, cascos));
        } else {
            SolicitudCliente solicitud = new SolicitudCliente();
            solicitud.setTipoSolicitud(0);
            solicitud.setPlaca(placaTxt);
            solicitud.setCascos(cascos);
            binder.getService().requestEntry(solicitud);
        }
        plate.setText("");
        locker.setText("");
        plate.requestFocus();
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
        binder.getService().restartServer(getServerIp());
        Log.i("Preferencias", "Se actualizan las preferencias");
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Esta seguro de cerrar la app?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        unbindService(serviceCon);
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();


    }

    @Override
    protected void onResume() {
        if(binder != null){
            binder.getService().isConnected();
        }
        super.onResume();
    }
}