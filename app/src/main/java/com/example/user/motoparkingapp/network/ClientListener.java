package com.example.user.motoparkingapp.network;

import android.widget.Toast;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.example.user.motoparkingapp.Lobby;

/**
 * Created by jindrax on 21/12/17.
 */

public class ClientListener extends Listener{

    public ClientListener() {
    }

    @Override
    public void connected(Connection connection) {
        super.connected(connection);
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
    }

    @Override
    public void received(Connection connection, Object o) {
        if(!(o instanceof FrameworkMessage.KeepAlive)){
            Lobby lobby = null;
            while(lobby == null){
                lobby = Joint.getLobby();
            }
            RespuestaServidor fromServer = (RespuestaServidor)o;
            switch ((int)fromServer.getTipoRespuesta()){
                case -1:
                    Toast.makeText(lobby, "A ocurrido un error al procesar la peticion", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    lobby.updateState(fromServer.getCupos());
                    break;
                case 1:
                    lobby.showEntry(fromServer.getCupos().get(0));
                    break;
                case 2:

                    break;
                case 3:

                    break;
            }
        }
    }
}
