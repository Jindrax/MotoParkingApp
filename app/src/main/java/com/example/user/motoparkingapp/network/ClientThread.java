package com.example.user.motoparkingapp.network;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jindrax on 21/12/17.
 */

public class ClientThread extends Service {

    Thread hiloRed;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Servicio", "Servicio iniciado");
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                Client cliente = new Client();
                Kryo kryo = cliente.getKryo();
                kryo.register(CupoJSON.class);
                kryo.register(SolicitudCliente.class);
                kryo.register(RespuestaServidor.class);
                kryo.register(ArrayList.class);
                cliente.start();
                try {
                    cliente.connect(5000, "192.168.0.26", 8051);
                    cliente.addListener(new ClientListener());
                    Joint.setCliente(cliente);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
