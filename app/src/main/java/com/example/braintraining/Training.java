package com.example.braintraining;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class Training extends AppCompatActivity {

    private static final String LOG_TAG = "ArraysState";

    // взаимодействие с UI
    Button ans1;
    Button ans2;
    Button ans3;
    TextView txtQuestion;
    TextView title;
    ImageView menuIcon;

    // для таймера
    Timer timer;
    TimerTask timerTask;
    Double time = 0.0;

    // ================================ для генерации выражений ====================================
    String question = "";             // выражение
    int questionsCount;               // кол-во выражений

    ArrayList<String> userAnswers = new ArrayList<>();              // массив пользовательских ответов
    ArrayList<String> questions = new ArrayList<>();                // массив выражений
    ArrayList<Integer> rightAnswers = new ArrayList<>();             // массив правильных ответов
    ArrayList<ArrayList<String>> answers = new ArrayList<>();       // ответы
    // =============================================================================================

    int number;                       // элемент цифры для выражения
    int currentExp = 0;               // кол-во решённых выражений

    //@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
        Log.d(LOG_TAG, "Expressions count from MainActivity: " + questionsCount);
        Log.d(LOG_TAG, "Answers: " + answers);

        set(0);

        // общий listener для кнопок
        View.OnClickListener buttonsClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.button1:
                        onBtnClick(ans1);
                        Log.d(LOG_TAG, "curr: " + currentExp);
                        break;
                    case R.id.button2:
                        onBtnClick(ans2);
                        Log.d(LOG_TAG, "curr: " + currentExp);
                        break;
                    case R.id.button3:
                        onBtnClick(ans3);
                        Log.d(LOG_TAG, "curr: " + currentExp);
                        break;
                }
            }
        };

        // обработка нажатий на варианты ответов
        ans1.setOnClickListener(buttonsClickListener);
        ans2.setOnClickListener(buttonsClickListener);
        ans3.setOnClickListener(buttonsClickListener);
    }

    void set(int i){
        ans1.setText(answers.get(i).get(0));
        ans2.setText(answers.get(i).get(1));
        ans3.setText(answers.get(i).get(2));

        question = questions.get(i);
        txtQuestion.setText(question);
    }

    // метод выполняющейся при нажатии на один из вариантов ответа
    void onBtnClick(Button ans){
        // добавление пользовательского ответа в массив пользовательских ответов
        userAnswers.add(ans.getText().toString());
        currentExp++;

        Log.d(LOG_TAG, '\n' + "Question: " + question + '\n' + "RightAnswers: " + rightAnswers + '\n' + "UserAnswers: " + userAnswers + '\n' + questions);

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
    String getTimerText() {
        int rounded = (int) Math.round(time);
        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;

        return formatTime(seconds, minutes);
    }

    // форматирование вывода времени
    String formatTime(int seconds, int minutes) {
        return String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }
}