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
 * Created by User on 24/12/2017.
 */

public class HistoricAdapter extends ArrayAdapter<CupoJSON> {
    public HistoricAdapter(@NonNull Context context, int resource, @NonNull List<CupoJSON> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.historic_item, null);
        }
        CupoJSON cupo = getItem(position);
        TextView cobro = convertView.findViewById(R.id.historic_cobro), plate = convertView.findViewById(R.id.historic_plate), locker = convertView.findViewById(R.id.historic_locker);
        if(cupo.getCobro()>0){
            cobro.setText(String.valueOf(cupo.getCobro()));
            cobro.setVisibility(View.VISIBLE);
        }
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
