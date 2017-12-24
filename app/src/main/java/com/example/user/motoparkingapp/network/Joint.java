package com.example.user.motoparkingapp.network;

import com.esotericsoftware.kryonet.Client;
import com.example.user.motoparkingapp.Lobby;

/**
 * Created by jindrax on 21/12/17.
 */

public class Joint {
    private static Lobby lobby;
    private static Client cliente;

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
}
