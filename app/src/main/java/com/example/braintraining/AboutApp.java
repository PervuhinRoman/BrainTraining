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
    HtmlTextView htmlText;
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
        htmlText = findViewById(R.id.html_text_about);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);

        // установка заголовка экрана
        title.setText(getResources().getString(R.string.title_of_aboutActivity));

        // работа с текстом описания
        htmlText.setHtml("<h4><span style='font-weight: 400;'><span style='text-decoration: underline;'><em>BrainTraining</em></span> - простое приложение, позволяющее поддерживать в тонусе мозг и расширять его возможности.</span></h4>\n" +
                "<h4><strong>Как?</strong></h4>\n" +
                "<h4><span style='text-decoration: underline;'><span style='font-weight: 400;'>Путем решения несложных примеров.</span></span></h4>\n" +
                "<h4><span style='font-weight: 400;'>Основой для создания приложения послужило исследование японского учёного <a title='Рюта Кавашима' href='https://www.labirint.ru/authors/121638/' target='_blank'>Рюты Кавашимы</a>, описанное в его книге &ldquo;Тренируй свой мозг&rdquo;. (Прочтите если интересно \uD83D\uDE09).</span></h4>\n" +
                "<h4><span style='font-weight: 400;'><strong>Главное:</strong> решение простых примеров крайне полезно!</span></h4>\n" +
                "<h4><span style='font-weight: 400;'>В книге автор доказывает это с помощью современных методов <a title='функциональной нейровизуализации' href='https://ru.wikipedia.org/wiki/%D0%A4%D1%83%D0%BD%D0%BA%D1%86%D0%B8%D0%BE%D0%BD%D0%B0%D0%BB%D1%8C%D0%BD%D0%B0%D1%8F_%D0%BD%D0%B5%D0%B9%D1%80%D0%BE%D0%B2%D0%B8%D0%B7%D1%83%D0%B0%D0%BB%D0%B8%D0%B7%D0%B0%D1%86%D0%B8%D1%8F' target='_blank'>функциональной нейровизуализации</a>, из которых следует, что общение с людьми, творческий ручной труд, и, конечно, чтение, письмо и счёт естественным образом активизируют работу префронтальной коры головного мозга, управляющую <a title='Высшая нервная деятельность' href='https://foxford.ru/wiki/biologiya/vysshaya-nervnaya-deyatelnost-cheloveka' target='_blank'>высшей нервной деятельностью</a>. Это подтверждается снимками, полученными при помощи оптической топографии.</span></h4>\n" +
                "<h4><span style='font-weight: 400;'>Ниже приведены некоторые снимки (чем больше красного тем больше трудится мозг)</span></h4>\n" +
                "<h4><span style='font-weight: 400;'><img src='https://raw.githubusercontent.com/PervuhinRoman/Images/master/exp.jpg' alt='' width='100%' /></span></h4>\n" +
                "<h4><span style='font-weight: 400;'>Как видите активнее всего мозг работает при&hellip; решении простых арифметических примеров на время. Вот так!</span></h4>\n" +
                "<h4><span style='font-weight: 400;'>Таким образом от улучшения памяти, повышения концентрации и работоспособности вас отделяет несколько кликов. <strong>Чего вы ждёте?</strong> \uD83D\uDE80\uD83D\uDD25</span></h4>\n" +
                "<h4>&nbsp;</h4>", new HtmlHttpImageGetter(htmlText));

        // открытие ссылок
        htmlText.setOnClickATagListener(new OnClickATagListener() {
            @Override
            public boolean onClick(View widget, String spannedText, @Nullable String href) {
                Toast.makeText(getApplicationContext(), href, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

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