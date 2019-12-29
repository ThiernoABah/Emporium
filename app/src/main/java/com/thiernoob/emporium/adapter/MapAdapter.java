package com.thiernoob.emporium.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.thiernoob.emporium.R;
import com.thiernoob.emporium.gameobjects.Kingdom;

import java.util.List;


public class MapAdapter extends ArrayAdapter<Kingdom> {

    private List<Kingdom> objects;
    private final int layoutRessource;

    public MapAdapter(@NonNull Context context, int ressource, @NonNull List<Kingdom> objects){
        super(context,ressource,objects);
        this.layoutRessource = ressource;
        this.objects = objects;
    }


    @NonNull
    @Override
    public View getView(final int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(this.layoutRessource,null,false);
        }

        final TextView name = convertView.findViewById(R.id.kingdomName);
        name.setText(objects.get(position).getKingdom().toString());

        return convertView;
    }

}
