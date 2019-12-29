package com.thiernoob.emporium.adapter;

import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.content.Context;

import androidx.annotation.NonNull;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thiernoob.emporium.R;
import com.thiernoob.emporium.gameobjects.Item;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {

    private List<Item> objects;

    private final int layoutRessource;

    public ItemAdapter(@NonNull Context context, int ressource, @NonNull List<Item> objects){
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

        final TextView name = convertView.findViewById(R.id.offerName);
        final TextView price = convertView.findViewById(R.id.offerPrice);
        final ImageView icon = convertView.findViewById(R.id.offerIcon);

        name.setText(objects.get(position).getName());
        int v = objects.get(position).getPrice();
        price.setText(v+"$");

        switch (objects.get(position).getType()){
            case CONSUMABLE:
                icon.setImageResource(R.drawable.ic_poison);
                break;
            case WEAPON:
                icon.setImageResource(R.drawable.ic_sword);
                break;
            case SPELL:
                icon.setImageResource(R.drawable.ic_magic_wand);
                break;
            case SHIELD:
                icon.setImageResource(R.drawable.ic_shield);
                break;
            case INGREDIENT:
                icon.setImageResource(R.drawable.ic_mortar);
                break;
        }

        switch (objects.get(position).getRarity()){
            case COMMON:
                convertView.setBackgroundColor(convertView.getResources().getColor(R.color.communColor));
                break;
            case RARE:
                convertView.setBackgroundColor(convertView.getResources().getColor(R.color.rareColor));
                break;
            case EPIC:
                convertView.setBackgroundColor(convertView.getResources().getColor(R.color.epicColor));
                break;
            case LEGENDARY:
                convertView.setBackgroundColor(convertView.getResources().getColor(R.color.legendaryColor));
                break;
        }

        return convertView;
    }
}
