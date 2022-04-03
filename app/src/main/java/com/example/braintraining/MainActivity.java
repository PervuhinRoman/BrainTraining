package com.example.braintraining;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    ImageView btnStart;
    SeekBar expressionsCount;
    SeekBar numbersCount;
    EditText txtExpressionsCount;
    EditText txtNumbersCount;
    TextView title;
    ImageView menuIcon;
    DrawerLayout navigationDrawerLayout;
    NavigationView navigationView;

    int defaultExpressionsCount = 25;
    int defaultNumbersCount = 3;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.btn_start);
        expressionsCount = findViewById(R.id.expressions_count_seekBar);
        numbersCount = findViewById(R.id.numbers_count_seekBar);
        txtExpressionsCount = findViewById(R.id.expressions_count);
        txtNumbersCount = findViewById(R.id.numbers_count);
        title = findViewById(R.id.title_of_appBar);

        navigationDrawerLayout = findViewById(R.id.navigation_drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);

        // установка заголовка экрана
        title.setText(getResources().getString(R.string.title_of_mainActivity));

        // установка дефолтного значения кол-ва выражений
        expressionsCount.setProgress(defaultExpressionsCount);
        txtExpressionsCount.setText(Integer.toString(defaultExpressionsCount));

        // установка дефолтного значения кол-ва чисел
        numbersCount.setProgress(defaultNumbersCount);
        txtNumbersCount.setText(Integer.toString(defaultNumbersCount));

        // открытие меню по нжатию на гамбургер меню
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                navigationDrawerLayout.openDrawer(Gravity.START);
            }
        });

        // обработка навигации
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.about:
                        Intent newIntent = new Intent(getApplicationContext(), AboutApp.class);
                        startActivity(newIntent);
                        break;
                    case R.id.start_training:
                        newIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(newIntent);
                        break;
                }
                return true;
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(getApplicationContext(), Training.class);
                    intent.putExtra("expressionsCount", Integer.parseInt(txtExpressionsCount.getText().toString()));
                    intent.putExtra("numbersCount", Integer.parseInt(txtNumbersCount.getText().toString()));
                    startActivity(intent);
                } catch (NumberFormatException e){
                    // обработка исключения: пустая строка кол-ва выражений
                    Toast nfeToast = Toast.makeText(getApplicationContext(), getResources().getText(R.string.enter_the_expressions_count), Toast.LENGTH_SHORT);
                    nfeToast.setGravity(Gravity.CENTER, 0, 0);
                    nfeToast.show();
                }
            }
        });

        expressionsCount.setMax(50);
        expressionsCount.setMin(1);
        expressionsCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                txtExpressionsCount.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        numbersCount.setMax(10);
        numbersCount.setMin(2);
        numbersCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                txtNumbersCount.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }
}