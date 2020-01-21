package com.thiernoob.emporium;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.thiernoob.emporium.gameobjects.Player;

import java.io.File;

public class LogActivity extends AppCompatActivity {

    private final String SAVE_FILENAME = "save.json";
    private final String DEFAULT_PSEUDO = "Kaladin";

    private Button startButt;
    private EditText pseudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log);

        File file = new File(this.getFilesDir(), SAVE_FILENAME);


        if (file.exists()) {
            this.defaultLogIn();
        } else {
            this.firstConnection();
        }

    }

    public void firstConnection() {

        pseudo = findViewById(R.id.pseudo);
        startButt = findViewById(R.id.startButt);
        pseudo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    startButt.setEnabled(true);
                } else {
                    startButt.setEnabled(false);
                }
            }
        });
        startButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });
    }

    public void logIn() {
        Intent intent = new Intent(this, MainActivity.class);
        String p = pseudo.getText().toString();
        intent.putExtra("PSEUDO", p);
        startActivityForResult(intent, 1);
    }

    public void defaultLogIn() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("PSEUDO", DEFAULT_PSEUDO);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.startButt = null;
        this.pseudo = null;
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_CANCELED) {
                Player.getPlayer().destroy();
                this.firstConnection();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.startButt = null;
        this.pseudo = null;
        Player.getPlayer().destroy();
        this.firstConnection();
    }
}
