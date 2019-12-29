package com.thiernoob.emporium.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.thiernoob.emporium.MainActivity;
import com.thiernoob.emporium.R;
import com.thiernoob.emporium.gameobjects.Item;
import com.thiernoob.emporium.gameobjects.Player;
import com.thiernoob.emporium.adapter.ItemAdapter;

import java.util.List;

public class ShopFragment extends Fragment {

    private Player player;
    private MainActivity activity;
    private List<Item> shop;

    // FAIRE UN COLLECTION ADAPTER
    private ItemAdapter adapter;

    // PEUT ETRE FAIRE UNE GRID
    private ListView lv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        player = Player.getPlayer();
        shop = activity.getShop();

        // FAIRE UN COLLECTION ITEM LAYOUT
        adapter = new ItemAdapter(this.getContext(),R.layout.offer_layout,shop);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_shop,container,false);
        lv = v.findViewById(R.id.collectionListView);
        lv.setAdapter(this.adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alertDialogFire(position);
            }

        });
        return v;
    }

    public void addItem(Item i ){
        adapter.add(i);
    }

    public void buy(int position){

        // ADD Bonus selon la zone dans laquelle est le player
        player.addGold(this.shop.get(position).getPrice());
        adapter.remove(this.shop.get(position));
        //adapter.notifyDataSetChanged();
    }

    public void alertDialogFire(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(),R.style.dialogMessage);

        TextView title = new TextView(this.getContext());
        title.setText(shop.get(position).getName());
        title.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        title.setGravity(Gravity.CENTER);
        title.setTextSize(25);
        builder.setCustomTitle(title);

        builder.setMessage("Do you want to move this item back to your collection ?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                moveToCollection(position);
            }
        });
        builder.setNeutralButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    public void moveToCollection(int position){
        Item i = shop.get(position);
        adapter.remove(shop.get(position));
        activity.addToCollection(i);
        Context context = lv.getContext();
        CharSequence text = "Move to collection";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
