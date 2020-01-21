package com.thiernoob.emporium.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.thiernoob.emporium.MainActivity;
import com.thiernoob.emporium.R;
import com.thiernoob.emporium.gameobjects.Player;

public class ProfileFragment extends Fragment {

    private Player player;
    private MainActivity activity;

    private Button reseter;
    private Button saver;

    private TextView pseudo;
    private TextView location;
    private TextView karma;
    private TextView gold;
    private TextView aligement;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        player = Player.getPlayer();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        ((TextView) v.findViewById(R.id.profilePseudo)).setText("Pseudo : " + player.getPseudo());
        ((TextView) v.findViewById(R.id.profileLocation)).setText("Location : " + player.getLocation().toString());
        ((TextView) v.findViewById(R.id.profileAlign)).setText("Aligment : " + player.getAlignment().toString());
        ((TextView) v.findViewById(R.id.profileKarma)).setText("Karma : " + player.getKarma());
        ((TextView) v.findViewById(R.id.profileGold)).setText("Gold : " + player.getGold() + " $");

        reseter = v.findViewById(R.id.resetButton);
        saver = v.findViewById(R.id.saveButton);

        reseter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.reset();
            }
        });
        saver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.save();
            }
        });
        return v;
    }

    public void refresh(){
        ((TextView) this.getView().findViewById(R.id.profilePseudo)).setText("Pseudo : " + player.getPseudo());
        ((TextView) this.getView().findViewById(R.id.profileLocation)).setText("Location : " + player.getLocation().toString());
        ((TextView) this.getView().findViewById(R.id.profileAlign)).setText("Aligment : " + player.getAlignment().toString());
        ((TextView) this.getView().findViewById(R.id.profileKarma)).setText("Karma : " + player.getKarma());
        ((TextView) this.getView().findViewById(R.id.profileGold)).setText("Gold : " + player.getGold() + " $");
    }

}
