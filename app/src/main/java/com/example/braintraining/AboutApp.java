package com.example.braintraining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.IOException;

public class AboutApp extends AppCompatActivity {

    public static final String LOG_TAG = "THR";

    TextView title;
    HtmlTextView htmlView;
    ImageView menuIcon;
    DrawerLayout navigationDrawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        title = findViewById(R.id.title_of_appBar);
        // htmlView = findViewById(R.id.html_view);

        navigationDrawerLayout = findViewById(R.id.navigation_drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);

        // установка заголовка экрана
        title.setText(getResources().getString(R.string.title_of_aboutActivity));

        // парсинг текста в фоновом потоке
        /*Thread parsing = new Thread(new Parsing());
        parsing.start();
        Log.d(LOG_TAG, "Поток запущен");*/

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
    }

    // парсинг текста откуда-то
    /*class Parsing implements Runnable {
        @Override
        public void run() {
            // парсинг
            Document doc = null;
            try {
                doc = (Document) Jsoup.connect("https://github.com/PervuhinRoman/BrainTraining/blob/master/README.md").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements readme = doc.select("#readme");
            htmlView.post(new Runnable() {
                @Override
                public void run() {
                    htmlView.setHtml(readme.toString(), new HtmlHttpImageGetter(htmlView));
                }
            });

            Log.d(LOG_TAG,"Поток завершён");
        }
    }*/
}