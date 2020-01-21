package com.thiernoob.emporium.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.renderscript.Script;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.thiernoob.emporium.MainActivity;
import com.thiernoob.emporium.R;
import com.thiernoob.emporium.adapter.MapAdapter;
import com.thiernoob.emporium.adapter.Offer;
import com.thiernoob.emporium.gameobjects.Item;
import com.thiernoob.emporium.gameobjects.Kingdom;
import com.thiernoob.emporium.gameobjects.Player;
import com.thiernoob.emporium.gameobjects.enums.Categories;
import com.thiernoob.emporium.gameobjects.enums.Location;
import com.thiernoob.emporium.gameobjects.enums.Rarity;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MapFragment extends Fragment {

    private int MIN_TRAVEL_TIME = 4;
    private int MAX_TRAVEL_TIME = 10;

    private Player player;
    private MainActivity activity;

    private List<Kingdom> map;
    private MapAdapter adapter;

    private GridView gv;
    private TextView location;

    ScheduledThreadPoolExecutor sch;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        player = Player.getPlayer();
        map = activity.getMap();
        sch = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
        adapter = new MapAdapter(this.getContext(),R.layout.map_layout,map);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map,container,false);
        this.location = v.findViewById(R.id.currentLocation);

        this.location.setText(player.getLocation().toString());
        gv = v.findViewById(R.id.grid_map);
        gv.setAdapter(this.adapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Player.getPlayer().getLocation() == Location.TRAVELING){
                    Context context = gv.getContext();
                    CharSequence text = "Hey... you are already traveling... wait";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }else{
                    traveling(position);
                }
            }

        });

        return v;
    }

    public void traveling(final int position){

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(),R.style.dialogMessage);

        TextView title = new TextView(this.getContext());
        title.setText(R.string.travel);
        title.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        title.setGravity(Gravity.CENTER);
        title.setTextSize(25);
        builder.setCustomTitle(title);

        String message = Kingdom.kingdomBonus(map.get(position).getKingdom())+"\n\n"+"Do you want to travel from "+Player.getPlayer().getLocation()+" to "+map.get(position).getKingdom().toString()+" ?";
        builder.setMessage(message);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                travel(map.get(position).getKingdom());
            }
        });
        builder.setNeutralButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    // travel task, manage the time between each destination
    public void travel(final Location position){
        Random r = new Random();
        Runnable tripEnd = new Runnable() {
            @Override
            public void run() {
                Player.getPlayer().setLocation(position);
                location.setText(Player.getPlayer().getLocation().toString());
            }
        };
        Player.getPlayer().setLastLocation(position);
        Player.getPlayer().setLocation(Location.TRAVELING);
        location.setText(Location.TRAVELING.toString());
        sch.schedule(tripEnd,MIN_TRAVEL_TIME + r.nextInt(MAX_TRAVEL_TIME), TimeUnit.SECONDS);
    }

}
