package com.thiernoob.emporium.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.thiernoob.emporium.MainActivity;
import com.thiernoob.emporium.R;
import com.thiernoob.emporium.adapter.MapAdapter;
import com.thiernoob.emporium.gameobjects.Kingdom;
import com.thiernoob.emporium.gameobjects.Player;

import java.util.List;

public class MapFragment extends Fragment {

    private Player player;
    private MainActivity activity;
    private List<Kingdom> map;

    private MapAdapter adapter;

    private GridView gv;
    private TextView location;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        player = Player.getPlayer();
        map = activity.getMap();

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

        return v;
    }
}
