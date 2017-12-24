package com.example.user.motoparkingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.user.motoparkingapp.R;
import com.example.user.motoparkingapp.network.CupoJSON;

import java.util.List;

/**
 * Created by jindrax on 21/12/17.
 */

public class CupoAdapter extends ArrayAdapter<CupoJSON>{

    public CupoAdapter(@NonNull Context context, int resource, @NonNull List<CupoJSON> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.cupo_item, null);
        }
        CupoJSON cupo = getItem(position);
        TextView cons = (TextView) convertView.findViewById(R.id.cupo_cons), plate = (TextView) convertView.findViewById(R.id.cupo_plate), locker = (TextView) convertView.findViewById(R.id.cupo_locker);
        cons.setText(String.valueOf(cupo.getConsecutivo()));
        plate.setText(cupo.getPlaca());
        String lockerString = cupo.getLocker();
        if(lockerString != null && !lockerString.isEmpty()){
            locker.setText(lockerString);
        }else{
            locker.setText("Sin Cascos");
        }
        return convertView;
    }
}