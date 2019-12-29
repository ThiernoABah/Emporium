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


import java.util.List;

public class OfferAdapter extends ArrayAdapter<Offer> {

    private List<Offer> offers;

    private final int layoutRessource;

    public OfferAdapter(@NonNull Context context, int ressource, @NonNull List<Offer> objects){
        super(context,ressource,objects);
        this.layoutRessource = ressource;
        this.offers = objects;
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

        name.setText(offers.get(position).getItem().getName());
        int v = offers.get(position).getItem().getPrice();
        price.setText(v+"$");

        switch (offers.get(position).getItem().getType()){
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

        switch (offers.get(position).getItem().getRarity()){
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
