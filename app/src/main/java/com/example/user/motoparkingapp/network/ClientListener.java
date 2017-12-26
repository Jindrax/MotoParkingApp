package com.example.user.motoparkingapp.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.example.user.motoparkingapp.services.CustomBinder;

/**
 * Created by jindrax on 21/12/17.
 */

public class ClientListener extends Listener{

    CustomBinder binder;

    public ClientListener(CustomBinder binder) {
        this.binder = binder;
    }

    @Override
    public void connected(Connection connection) {
        super.connected(connection);
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
        connection.close();
    }

    @Override
    public void received(Connection connection, Object o) {
        if(!(o instanceof FrameworkMessage.KeepAlive)){
            RespuestaServidor fromServer = (RespuestaServidor)o;
            switch ((int)fromServer.getTipoRespuesta()){
                case -1:
                    binder.getService().toastError();
                    break;
                case 0:
                    binder.getService().updateState(fromServer.getCupos());
                    break;
                case 1:
                    binder.getService().entryCallback(fromServer.getCupos().get(0));
                    break;
                case 2:
                    binder.getService().prospectCallback(fromServer.getCupos().get(0));
                    break;
                case 3:
                    binder.getService().egressCallback(fromServer.getCupos().get(0));
                    break;
            }
        }
    }
}
