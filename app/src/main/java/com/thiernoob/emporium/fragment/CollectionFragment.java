package com.thiernoob.emporium.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.thiernoob.emporium.MainActivity;
import com.thiernoob.emporium.R;
import com.thiernoob.emporium.gameobjects.Item;
import com.thiernoob.emporium.gameobjects.Player;
import com.thiernoob.emporium.adapter.ItemAdapter;

import java.util.List;

public class CollectionFragment extends Fragment {

    private Player player;
    private MainActivity activity;
    private List<Item> collection;

    private ItemAdapter adapter;

    private ListView lv;
    private TextView worth;

    private Button sellingButton;
    private EditText sellingPrice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        player = Player.getPlayer();
        collection = activity.getCollection();

        adapter = new ItemAdapter(this.getContext(),R.layout.offer_layout,collection);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_collection,container,false);
        worth = v.findViewById(R.id.collectionWorth);
        int cpt = 0;
        for(Item i : collection){
            cpt += i.getOriginalPrice();
        }
        worth.setText(cpt+" $");
        lv = v.findViewById(R.id.collectionListView);
        lv.setAdapter(this.adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sellingDialogFire(position);
            }

        });
        return v;
    }

    public void sellingDialogFire(final int position){

        final Dialog sellingDial = new Dialog(this.getContext());
        sellingDial.setContentView(R.layout.vending_layout);
        ((TextView)sellingDial.findViewById(R.id.item_name)).setText(collection.get(position).getName());
        ImageView icon = sellingDial.findViewById(R.id.item_icon);
        setIcons(icon,position);


        ((TextView)sellingDial.findViewById(R.id.item_description)).setText(collection.get(position).getDescription()+"\n Item Buyed at : "+collection.get(position).getOriginalPrice());

        sellingPrice = (sellingDial.findViewById(R.id.selling_price));
        sellingButton = (sellingDial.findViewById(R.id.selling_button));


        sellingPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                sellingButton.setEnabled(false);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    sellingButton.setEnabled(true);
                }
                else{
                    sellingButton.setEnabled(false);
                }
            }
        });
        sellingButton.setEnabled(false);
        sellingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vending(position);
                sellingDial.dismiss();
                Context context = lv.getContext();
                CharSequence text = "Added to Shop";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        sellingDial.show();
        Window window = sellingDial.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void vending(int position ){
        String np = sellingPrice.getText().toString();
        int newPrice=Integer.parseInt(np);

        Item i = collection.get(position);
        // Calculing the time that the item should passes in the shop based on its rarety and original price

        int factor = 1;
        if(newPrice/i.getPrice() > 4){
            factor = 10;
        }
        else if(newPrice/i.getPrice() > 2){
            factor = 4;
        }
        else if(newPrice/i.getPrice() > 1){
            factor = 2;
        }
        else{
            factor = 1;
        }

        switch (i.getRarity()){
            case COMMON:
                i.setTimeInShop(factor * 7);
                break;
            case RARE:
                i.setTimeInShop(factor * 10);
                break;
            case EPIC:
                i.setTimeInShop(factor * 13);
                break;
            case LEGENDARY:
                i.setTimeInShop(factor * 20);
                break;
        }

        i.setPrice(newPrice);
        adapter.remove(collection.get(position));
        activity.addToShop(i);
        int cpt = 0;
        for(Item a : collection){
            cpt += a.getOriginalPrice();
        }
        worth.setText(cpt+" $");
    }

    private void setIcons(ImageView icon, int position){
        switch (collection.get(position).getType()){
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
    }




}
