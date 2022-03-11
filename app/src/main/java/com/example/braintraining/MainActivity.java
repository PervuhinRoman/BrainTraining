package com.example.braintraining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    Button btnApply;
    SeekBar expressionsCount;
    TextView txtExpressionsCount;
    TextView tittle;
    ImageView menuIcon;
    DrawerLayout navLayout;
    NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnApply = findViewById(R.id.btnApply);
        expressionsCount = findViewById(R.id.seekBar);
        txtExpressionsCount = findViewById(R.id.txtExpressionsCount);
        tittle = findViewById(R.id.tittle);

        navLayout = findViewById(R.id.nav_layout);
        navView = findViewById(R.id.navigation);
        menuIcon = findViewById(R.id.menu);

        tittle.setText("Home");

        // открытие меню по нжатию на гамбургер меню
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                navLayout.openDrawer(Gravity.START);
            }
        });

        // обработка навигации
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.about){
                    Intent newIntent = new Intent(getApplicationContext(), AboutApp.class);
                    startActivity(newIntent);
                }
                return true;
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(getApplicationContext(), Training.class);
                    intent.putExtra("expressionsCount", Integer.parseInt(txtExpressionsCount.getText().toString()));
                    startActivity(intent);
                } catch (NumberFormatException e){
                    // обработка исключения: пустая строка кол-ва выражений
                    Toast nfeToast = Toast.makeText(getApplicationContext(), "Enter the expressions count", Toast.LENGTH_SHORT);
                    nfeToast.setGravity(Gravity.CENTER, 0, 0);
                    nfeToast.show();
                }
            }
        });

        expressionsCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                txtExpressionsCount.setText(Integer.toString(progress / 2));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }
}