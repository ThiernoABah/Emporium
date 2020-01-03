package com.thiernoob.emporium.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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
import com.thiernoob.emporium.adapter.Offer;
import com.thiernoob.emporium.adapter.OfferAdapter;
import com.thiernoob.emporium.gameobjects.Item;
import com.thiernoob.emporium.gameobjects.Player;
import com.thiernoob.emporium.gameobjects.enums.Categories;
import com.thiernoob.emporium.gameobjects.enums.Rarity;

import java.util.List;
import java.util.Random;

public class ClientFragment extends Fragment {

    private MainActivity activity;
    private Player player;
    private List<Offer> listOffer;
    private OfferAdapter adapter;
    private ListView lv;
    private TextView remaining;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        player = Player.getPlayer();
        listOffer = activity.getOffers();
        adapter = new OfferAdapter(this.getContext(),R.layout.offer_layout,listOffer);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_client,container,false);
        remaining = v.findViewById(R.id.remainingGold);
        remaining.setText(player.getGold()+" $");
        lv = v.findViewById(R.id.offerListView);
        lv.setAdapter(this.adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alertDialogFire(position);
            }

        });
        return v;
    }

    public void alertDialogFire(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(),R.style.dialogMessage);

        TextView title = new TextView(this.getContext());
        title.setText(listOffer.get(position).getItem().getName());
        title.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        title.setGravity(Gravity.CENTER);
        title.setTextSize(25);

        builder.setCustomTitle(title);

        builder.setMessage(listOffer.get(position).getItem().getDescription()+"\n\nCost : "+listOffer.get(position).getItem().getPrice()+"$");

        builder.setPositiveButton(R.string.buy, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                buy(position);
            }
        });
        builder.setNeutralButton(R.string.discard, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                adapter.remove(listOffer.get(position));
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    public void addOffer(Offer offer){
            listOffer.add(offer);
            adapter.notifyDataSetChanged();
    }

    public void buy(int position){
        Item i = listOffer.get(position).getItem();
        int karma = listOffer.get(position).getKarma();
        if (player.getGold() >= i.getPrice()) {
            player.giveGold(i.getPrice());
            remaining.setText(player.getGold()+" $");
            player.updateKarma(karma);
            adapter.remove(listOffer.get(position));
            activity.addToCollection(i);
        } else {
            Context context = lv.getContext();
            CharSequence text = "To poor for this...";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
}