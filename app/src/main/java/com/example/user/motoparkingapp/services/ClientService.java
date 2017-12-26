package com.example.user.motoparkingapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.example.user.motoparkingapp.Egress;
import com.example.user.motoparkingapp.Lobby;
import com.example.user.motoparkingapp.network.ClientListener;
import com.example.user.motoparkingapp.network.CupoJSON;
import com.example.user.motoparkingapp.network.RespuestaServidor;
import com.example.user.motoparkingapp.network.SolicitudCliente;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 25/12/2017.
 */

public class ClientService extends Service{

    private Client cliente;
    private Lobby lobby;
    private Egress activeEgress;
    private CustomBinder thisBinder;
    private List<CupoJSON> historic;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        historic = new ArrayList<>();
        thisBinder = new CustomBinder(this);
        return thisBinder;
    }

    public void requestEgress(final SolicitudCliente request, Egress requestingEgress){
        activeEgress = requestingEgress;
        new AsyncTask(){

            @Override
            protected Object doInBackground(Object[] objects) {
                cliente.sendTCP(request);
                return null;
            }
        }.execute();
    }

    public void requestEntry(final SolicitudCliente request){
        new AsyncTask(){

            @Override
            protected Object doInBackground(Object[] objects) {
                cliente.sendTCP(request);
                return null;
            }
        }.execute();
    }

    public void requestProspect(final SolicitudCliente request){
        new AsyncTask(){

            @Override
            protected Object doInBackground(Object[] objects) {
                cliente.sendTCP(request);
                return null;
            }
        }.execute();
    }

    public void entryCallback(CupoJSON response){
        historic.add(0, response);
        lobby.showEntry(response);
    }

    public void egressCallback(CupoJSON response){
        historic.add(0, response);
        activeEgress.egressedCallback(response);
    }

    public void prospectCallback(CupoJSON response){
        lobby.showEgress(response);
    }

    public void updateState(List<CupoJSON> state){
        lobby.updateState(state);
    }

    public void startServer(Lobby lobby, final String ip){
        this.lobby = lobby;
        cliente = new Client();
        Kryo kryo = cliente.getKryo();
        kryo.register(CupoJSON.class);
        kryo.register(SolicitudCliente.class);
        kryo.register(RespuestaServidor.class);
        kryo.register(ArrayList.class);
        cliente.addListener(new ClientListener(thisBinder));
        cliente.start();
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    cliente.connect(5000, ip, 8051);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public void restartServer(final String ip){
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    cliente.close();
                    cliente.connect(5000, ip, 8051);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public void toastError(){
        Toast.makeText(getBaseContext(), "A ocurrido un error al procesar la peticion", Toast.LENGTH_SHORT).show();
    }

    public boolean isConnected(){
       if(cliente.isConnected()){
           return true;
       }else{
           try {
               cliente.reconnect();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
       return false;
    }

    @Override
    public void onDestroy() {
        cliente.close();
        super.onDestroy();
    }


}
