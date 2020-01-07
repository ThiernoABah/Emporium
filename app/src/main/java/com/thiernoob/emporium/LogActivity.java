package com.thiernoob.emporium;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class LogActivity extends AppCompatActivity {

    private Button startButt;
    private EditText pseudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log);

        String filename = "save.json";
        File file = new File(this.getFilesDir(), filename);

        if (file.exists()) {
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
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
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


    }

    public void logIn() {
        Intent intent = new Intent(this, MainActivity.class);
        String p = pseudo.getText().toString();
        intent.putExtra("PSEUDO", p);
        startActivity(intent);
    }
}
