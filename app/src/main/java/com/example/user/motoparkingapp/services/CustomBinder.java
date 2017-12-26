package com.example.user.motoparkingapp.services;

import android.content.ServiceConnection;
import android.os.Binder;

import java.io.Serializable;

/**
 * Created by User on 25/12/2017.
 */

public class CustomBinder extends Binder implements Serializable{

    ClientService master;

    public CustomBinder(ClientService master) {
        this.master = master;
    }

    public ClientService getService(){
        return master;
    }
}
