package com.example.braintraining;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.yandex.mobile.ads.banner.AdSize;
import com.yandex.mobile.ads.banner.BannerAdEventListener;
import com.yandex.mobile.ads.banner.BannerAdView;
import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.ImpressionData;
import com.yandex.mobile.ads.common.InitializationListener;
import com.yandex.mobile.ads.common.MobileAds;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ArraysState";
    private static final String YANDEX_MOBILE_ADS_TAG = "YandexAds";

    private static final String AdUnitId = "adf-279013/966631";
    private BannerAdView mBannerAdView;

    private ImageView btnStart;
    private ImageView btnPlay;
    private SeekBar expressionsCount;
    private SeekBar skBarNumbersCount;
    private SeekBar skBarAnswersRange;
    private EditText txtExpressionsCount;
    private EditText txtNumbersCount;
    private EditText txtAnswersRange;
    private TextView title;
    private ImageView menuIcon;
    private DrawerLayout navigationDrawerLayout;
    private NavigationView navigationView;

    private final int defaultExpressionsCount = 25;
    private final int defaultNumbersCount = 3;
    private final int defaultAnswersRange = 10;

    // ================================ для генерации выражений ====================================
    private String question = "";
    private int rightAnswer;

    private int questionsCount = 25;            // пользовательский ввод | кол-во выражений
    private int numbersCount = 3;               // пользовательский ввод | кол-во чисел в выражении
    private final int numbersRange = 9;
    private final int answersCount = 3;         // пользовательский ввод | кол-во ответов
    private int answersRange = 10;              // пользовательский ввод | диапазон ответов

    private final ArrayList<String> alreadyGeneratedNumbers = new ArrayList<>(); // для генерации
    private final ArrayList<Integer> alreadyGeneratedAnswers = new ArrayList<>(); // для генерации

    private final String[] actions = {"+", "-"}; // для генерации
    private String prevAction;                   // для генерации

    private final ArrayList<ArrayList<String>> answers = new ArrayList<>();   // ответы

    private final ArrayList<Integer> rightAnswers = new ArrayList<>();             // массив правильных ответов
    private final ArrayList<String> expressions = new ArrayList<>();               // массив выражений
    // =============================================================================================

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MobileAds.initialize(this, new InitializationListener() {
            @Override
            public void onInitializationCompleted() {
                Log.d(YANDEX_MOBILE_ADS_TAG, "SDK initialized");
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        AdSize size = new AdSize(displayMetrics.widthPixels, 100);

        mBannerAdView = findViewById(R.id.banner_ad_view);
        mBannerAdView.setAdUnitId(AdUnitId);
        mBannerAdView.setAdSize(AdSize.BANNER_320x50);

        final AdRequest adRequest = new AdRequest.Builder().build();

        mBannerAdView.setBannerAdEventListener(new BannerAdEventListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdFailedToLoad(@NonNull AdRequestError adRequestError) {

            }

            @Override
            public void onAdClicked() {

            }

            @Override
            public void onLeftApplication() {

            }

            @Override
            public void onReturnedToApplication() {

            }

            @Override
            public void onImpression(@Nullable ImpressionData impressionData) {

            }
        });

        // Загрузка объявления.
        mBannerAdView.loadAd(adRequest);

        btnStart = findViewById(R.id.btn_start);
        btnPlay = findViewById(R.id.btn_play);
        expressionsCount = findViewById(R.id.expressions_count_seekBar);
        skBarNumbersCount = findViewById(R.id.numbers_count_seekBar);
        skBarAnswersRange = findViewById(R.id.answer_range_seekBar);
        txtAnswersRange = findViewById(R.id.answers_range);
        txtExpressionsCount = findViewById(R.id.expressions_count);
        txtNumbersCount = findViewById(R.id.numbers_count);
        title = findViewById(R.id.title_of_appBar);

        // кнопки изначально
        btnStart.setVisibility(View.VISIBLE);
        btnPlay.setVisibility(View.GONE);

        // для обраьотки навигации
        navigationDrawerLayout = findViewById(R.id.navigation_drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);

        // установка заголовка экрана
        title.setText(getResources().getString(R.string.title_of_mainActivity));

        // установка дефолтного значения кол-ва выражений
        expressionsCount.setProgress(defaultExpressionsCount);
        txtExpressionsCount.setText(Integer.toString(defaultExpressionsCount));

        // установка дефолтного значения кол-ва чисел
        skBarNumbersCount.setProgress(defaultNumbersCount);
        txtNumbersCount.setText(Integer.toString(defaultNumbersCount));

        // установка дефолтного значения диапазона чисел
        skBarAnswersRange.setProgress(defaultAnswersRange);
        txtAnswersRange.setText(Integer.toString(defaultAnswersRange));

        // =================================== навигация ===========================================
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
                        finish();
                        break;
                    case R.id.start_training:
                        newIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(newIntent);
                        break;
                }
                return true;
            }
        });
        // =========================================================================================

        // кнопка генерации
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < questionsCount; i++) {
                    mGen();
                }
                btnStart.setVisibility(View.GONE);
                btnPlay.setVisibility(View.VISIBLE);
                Log.d(LOG_TAG, "expressions: " + expressions);
                Log.d(LOG_TAG, "Answers: " + answers);
            }
        });

        // кнопка старта теста
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Training.class);
                intent.putExtra("expressionsCount", (Serializable) questionsCount);
                intent.putExtra("questions", (Serializable) expressions);
                intent.putExtra("answers", (Serializable) answers);
                intent.putExtra("rightAnswers", (Serializable) rightAnswers);

                btnStart.setVisibility(View.VISIBLE);
                btnPlay.setVisibility(View.GONE);

                startActivity(intent);
                finish();
            }
        });

        // кол-во выражений
        expressionsCount.setMax(50);
        expressionsCount.setMin(2);
        expressionsCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                txtExpressionsCount.setText(Integer.toString(progress));
                questionsCount = progress;
                Log.d(LOG_TAG, "questionsCount: " + questionsCount);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // кол-во чисел в выражении
        skBarNumbersCount.setMax(4);
        skBarNumbersCount.setMin(2);
        skBarNumbersCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                txtNumbersCount.setText(Integer.toString(progress));
                numbersCount = progress;
                Log.d(LOG_TAG, "numbersCount: " + numbersCount);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // диапазон ответов
        skBarAnswersRange.setMax(100);
        skBarAnswersRange.setMin(5);
        skBarAnswersRange.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                txtAnswersRange.setText(Integer.toString(progress));
                answersRange = progress;
                Log.d(LOG_TAG, "answersRange: " + answersRange);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    // ====================================== генерация ========================================
    // генерация чисел для выражения
    private void numberGeneration(int  numbersCount, int numbersRange){
        String number = Integer.toString((int)(Math.random() * numbersRange + 1));

        for(int i = 0; i < numbersCount; i++) {

            // проверка
            while (alreadyGeneratedNumbers.size() > 0 && alreadyGeneratedNumbers.contains(number)) {
                // генерация
                number = Integer.toString((int) (Math.random() * numbersRange + 1));
            }
            alreadyGeneratedNumbers.add(number);
        }
    }

    // генерация действия
    private String actionGeneration(String[] actions){
        String action = actions[(int) (Math.random() * 2)];

        while(action.equals(prevAction)) {
            action = actions[(int) (Math.random() * 2)];
        }

        prevAction = action;

        return action;
    }

    // генерация случайных ответов
    private String answerGeneration(int rightAnswer, int answersRange){
        int number = (int)(Math.random() * (answersRange + 1) - (answersRange + 1));

        // проверка
        while(alreadyGeneratedAnswers.size() > 0 && (alreadyGeneratedAnswers.contains(number) || number == rightAnswer)){
            // генерация
            number = (int)(Math.random() * (answersRange + 1) - (answersRange + 1));
        }
        alreadyGeneratedAnswers.add(number);

        return Integer.toString(number);
    }

    private void mGen(){
        numberGeneration(numbersCount, numbersRange);
        ArrayList<String> itemAnswers = new ArrayList<>(3);
        String number;
        String action;

        // генерация первого числа
        number = alreadyGeneratedNumbers.get(0);
        question += number;
        question+=" ";

        // начинаем считать правильный ответ
        rightAnswer = Integer.parseInt(number);

        for(int i = 1; i < alreadyGeneratedNumbers.size(); i++){
            Log.d(LOG_TAG, "loop" + i);
            // генерируем действие и добавляем в выражение
            action = actionGeneration(actions);
            question += action;
            question+=" ";

            // генерация числа и добавление в выражение
            number = alreadyGeneratedNumbers.get(i);
            question += number;
            if(i == alreadyGeneratedNumbers.size() - 1){
                question+="";
            } else {
                question+=" ";
            }

            // считаем правильный ответ
            switch (action) {
                case "+":
                    rightAnswer += Integer.parseInt(number);
                    break;
                case "-":
                    rightAnswer -= Integer.parseInt(number);
                    break;
            }
        }
        Log.d(LOG_TAG, "question: " + question);

        // генерация случайных ответов
        int rightAnsInd = (int)(Math.random() * (answersCount) + 0);

        for(int i = 0; i < answersCount; i++){
            Log.d(LOG_TAG, "LOOP" + i);
            if(i == rightAnsInd){
                itemAnswers.add(Integer.toString(rightAnswer));
            } else {
                itemAnswers.add(answerGeneration(rightAnswer, answersRange));
            }
        }

        // добавляем текущие сненерированные ответы к общему стеку
        answers.add(itemAnswers);

        rightAnswers.add(rightAnswer);
        // обнуляем правильный ответ
        rightAnswer = 0;

        expressions.add(question);
        // обнуляем выражение
        question = "";

        // отчищаем массив рандомных ответов и т.д.
        alreadyGeneratedNumbers.clear();
        alreadyGeneratedAnswers.clear();
        Log.d(LOG_TAG, "Method finished");
    }
    // =========================================================================================
}