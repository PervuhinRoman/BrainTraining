package com.example.braintraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.yandex.mobile.ads.banner.BannerAdView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Training extends AppCompatActivity {

    // взаимодействие с UI
    private BannerAdView mBannerAdView;
    private Button ans1;
    private Button ans2;
    private Button ans3;
    private TextView txtQuestion;
    private TextView title;
    private ImageView menuIcon;

    // для таймера
    private Timer timer;
    private TimerTask timerTask;
    private Double time = 0.0;

    // ================================ для генерации выражений ====================================
    private String question = "";             // выражение
    private int questionsCount;               // кол-во выражений

    private final ArrayList<String> userAnswers = new ArrayList<>();        // массив пользовательских ответов
    private ArrayList<String> questions = new ArrayList<>();                // массив выражений
    private ArrayList<Integer> rightAnswers = new ArrayList<>();            // массив правильных ответов
    private ArrayList<ArrayList<String>> answers = new ArrayList<>();       // ответы
    // =============================================================================================

    private int currentExp = 0;               // кол-во решённых выражений

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // поток для рекламы
        mBannerAdView = findViewById(R.id.banner_ad_view);
        Thread adsThread = new Thread(new setAd(mBannerAdView));
        adsThread.start();

        ans1 = findViewById(R.id.button1);
        ans2 = findViewById(R.id.button2);
        ans3 = findViewById(R.id.button3);
        txtQuestion = findViewById(R.id.question);
        title = findViewById(R.id.title_of_appBar);
        menuIcon = findViewById(R.id.menu_icon);

        // скрывает элемент гамбургера меню
        menuIcon.setVisibility(View.GONE);

        // создание таймера и его запуск
        timer = new Timer();
        startTimer();

        // кол-во выражений, которые необходимо решить, полученное из MainActivity
        Intent expressionsCountIntent = getIntent();
        questionsCount = expressionsCountIntent.getIntExtra("expressionsCount", 50);
        questions = expressionsCountIntent.getStringArrayListExtra("questions");
        rightAnswers = expressionsCountIntent.getIntegerArrayListExtra("rightAnswers");
        answers = (ArrayList<ArrayList<String>>) expressionsCountIntent.getSerializableExtra("answers");

        // установка первого выражения
        set(0);

        // общий listener для кнопок
        View.OnClickListener buttonsClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.button1:
                        onBtnClick(ans1);
                        break;
                    case R.id.button2:
                        onBtnClick(ans2);
                        break;
                    case R.id.button3:
                        onBtnClick(ans3);
                        break;
                }
            }
        };

        // обработка нажатий на варианты ответов
        ans1.setOnClickListener(buttonsClickListener);
        ans2.setOnClickListener(buttonsClickListener);
        ans3.setOnClickListener(buttonsClickListener);
    }

    // функция установки выражения и ответов на экран
    private void set(int i){
        ans1.setText(answers.get(i).get(0));
        ans2.setText(answers.get(i).get(1));
        ans3.setText(answers.get(i).get(2));

        question = questions.get(i);
        txtQuestion.setText(question);
    }

    // метод выполняющейся при нажатии на один из вариантов ответа
    private void onBtnClick(Button ans){
        // добавление пользовательского ответа в массив пользовательских ответов
        userAnswers.add(ans.getText().toString());
        currentExp++;
        
        // проверка кол-ва решённых выражений
        if(currentExp == questionsCount){
            // остановка таймера
            timerTask.cancel();

            txtQuestion.setText(R.string.finish);
            ans1.setVisibility(View.GONE);
            ans2.setVisibility(View.GONE);
            ans3.setVisibility(View.GONE);

            // создание intent-а для передачи данных между activity и открытия новых активностей
            Intent intent = new Intent(getApplicationContext(), Results.class);

            // передаём кол-во выражений в Results
            intent.putExtra("expressionsCount", questionsCount);

            // передаём время выполнения в Results
            intent.putExtra("time", time);

            // передаём массив выражений в Results
            intent.putExtra("questions", (Serializable) questions);

            // передаём массив пользовательских ответов в Results
            intent.putExtra("userAnswers", (Serializable) userAnswers);

            // передаём массив правильных ответов в Results
            intent.putExtra("rightAnswers", (Serializable) rightAnswers);

            // запускаем новую активность
            startActivity(intent);
            finish();
        } else {
            set(currentExp);
        }
    }

    // метод запуска таймера
    private void startTimer()
    {
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        time++;
                        title.setText(getTimerText());
                    }
                });
            }

        };
        timer.scheduleAtFixedRate(timerTask, 0 ,1000);
    }

    // получение времени
    private String getTimerText() {
        int rounded = (int) Math.round(time);
        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;

        return formatTime(seconds, minutes);
    }

    // форматирование вывода времени
    private String formatTime(int seconds, int minutes) {
        return String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }
}