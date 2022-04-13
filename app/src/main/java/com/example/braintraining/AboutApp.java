package com.example.braintraining;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.sufficientlysecure.htmltextview.HtmlFormatter;
import org.sufficientlysecure.htmltextview.HtmlFormatterBuilder;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;
import org.sufficientlysecure.htmltextview.OnClickATagListener;

import java.io.IOException;

public class AboutApp extends AppCompatActivity {

    public static final String LOG_TAG = "THR";

    TextView title;
    ImageView btnNext;
    WebView webView;
    ImageView menuIcon;
    DrawerLayout navigationDrawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        title = findViewById(R.id.title_of_appBar);

        navigationDrawerLayout = findViewById(R.id.navigation_drawer_layout);
        webView = findViewById(R.id.web_view);
        btnNext = findViewById(R.id.btn_next);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);

        // установка заголовка экрана
        title.setText(getResources().getString(R.string.title_of_aboutActivity));

        webView.loadUrl("file:///android_asset/about.html");

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

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
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