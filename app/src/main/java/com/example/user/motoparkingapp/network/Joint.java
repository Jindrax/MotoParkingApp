package com.example.user.motoparkingapp.network;

import android.content.Context;

import com.esotericsoftware.kryonet.Client;
import com.example.user.motoparkingapp.Lobby;

import java.util.List;

/**
 * Created by jindrax on 21/12/17.
 */

public class Joint {
    private static Lobby lobby;
    private static Client cliente;
    private static List<CupoJSON> historico;
    private static Context contextoActivo;

    public static Lobby getLobby() {
        return lobby;
    }

    public static void setLobby(Lobby lobby) {
        Joint.lobby = lobby;
    }

    public static Client getCliente() {
        return cliente;
    }

    public static void setCliente(Client cliente) {
        Joint.cliente = cliente;
    }

    public static List<CupoJSON> getHistorico() {
        return historico;
    }

    public static void setHistorico(List<CupoJSON> historico) {
        Joint.historico = historico;
    }

    public static void addHistoric(CupoJSON cupo){
        historico.add(0,cupo);
    }

    public static Context getContextoActivo() {
        return contextoActivo;
    }

    public static void setContextoActivo(Context contextoActivo) {
        Joint.contextoActivo = contextoActivo;
    }
}
