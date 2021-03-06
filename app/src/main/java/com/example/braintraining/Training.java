package com.example.braintraining;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Training extends AppCompatActivity {

    private static final String LOG_TAG = "ArraysState";

    Button ans1;
    Button ans2;
    Button ans3;
    TextView txtQuestion;
    TextView tittle;

    Timer timer;
    TimerTask timerTask;
    Double time = 0.0;

    String question = "";             // выражение
    int rightAnswer = 0;              // правильный ответ

    List<Integer> userAnswers = new ArrayList<>();              // массив пользовательских ответов
    List<Integer> rightAnswers = new ArrayList<>();             // массив правильных ответов

    int expressionsCount;                                        // кол-во выражений
    List<String> expressions = new ArrayList<>();                // массив выражений

    String[] actions = {"+", "-"};    // список действий

    int number;                       // элемент цифры для выражения
    String action;                    // элемент действия для выражения

    int currentExp = 0;               // кол-во решённых выражений

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        ans1 = findViewById(R.id.button1);
        ans2 = findViewById(R.id.button2);
        ans3 = findViewById(R.id.button3);
        txtQuestion = (TextView) findViewById(R.id.question);
        tittle = (TextView) findViewById(R.id.tittle);

        // создание таймера и его запуск
        timer = new Timer();
        startTimer();

        // кол-во выражений, которые необходимо решить, полученное из MainActivity
        Intent expressionsCountIntent = getIntent();
        expressionsCount = expressionsCountIntent.getIntExtra("expressionsCount", 50);
        Log.d(LOG_TAG, "Expressions count from MainActivity: " + expressionsCount);

        // создание "нулевого" выражения
        onButtonClick();

        // общий listener для кнопок
        View.OnClickListener buttonsClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.button1:
                        optimize(ans1);
                        break;
                    case R.id.button2:
                        optimize(ans2);
                        break;
                    case R.id.button3:
                        optimize(ans3);
                        break;
                }
            }
        };

        // обработка нажатий на варианты ответов
        ans1.setOnClickListener(buttonsClickListener);
        ans2.setOnClickListener(buttonsClickListener);
        ans3.setOnClickListener(buttonsClickListener);
    }

    // основной метод генерации
    @SuppressLint("SetTextI18n")
    public void onButtonClick(){
        // генерация и добавления первой случайной цифры в диапозоне: 1 - 9
        number = (int)(Math.random() * 9 + 1);
        question += Integer.toString(number);
        question+=" ";

        // начало счёта праильного ответа
        rightAnswer += number;

        // первое действие
        action = actions[(int)(Math.random()*2)];
        question += action;
        question+=" ";

        // генерация и добавления второй случайной цифры в диапозоне: 1 - 9
        number = (int)(Math.random() * 9 + 1);
        question += Integer.toString(number);
        question+=" ";

        // добавления вычислений к праильному ответу | второе действие должно отличаться от первого
        if(action == "-"){
            rightAnswer -= number;
            action = "+";
            question += action;
        } else if(action == "+"){
            rightAnswer += number;
            action = "-";
            question += action;
        }
        question+=" ";

        // генерация и добавления третей случайной цифры в диапозоне: 1 - 9
        number = (int)(Math.random() * 9 + 1);
        question += Integer.toString(number);

        if(action == "-"){
            rightAnswer -= number;
        } else if(action == "+"){
            rightAnswer += number;
        }

        // готовое выражение
        expressions.add(question);
        txtQuestion.setText(question);
        question = "";

        // добавляем вычисленный правильный ответ в массив правильных ответов
        rightAnswers.add(rightAnswer);

        // случайным образом присваиваем праильный ответ одной из конопок выбора ответа

        number = (int)(Math.random() * 3 + 1);
        switch (number){
            case 1:
                // присваивание праильного значения выбранной случайным образом кнопке
                ans1.setText(Integer.toString(rightAnswer));

                // генерация неверных ответов для оставшихся кнопок
                number = (int)(Math.random() * 20 - 20);
                ans2.setText(Integer.toString(number));
                number = (int)(Math.random() * 20 - 20);
                ans3.setText(Integer.toString(number));
                break;
            case 2:
                // присваивание праильного значения выбранной случайным образом кнопке
                ans2.setText(Integer.toString(rightAnswer));

                // генерация неверных ответов для оставшихся кнопок
                number = (int)(Math.random() * 20 - 20);
                ans1.setText(Integer.toString(number));
                number = (int)(Math.random() * 20 - 20);
                ans3.setText(Integer.toString(number));
                break;
            case 3:
                // присваивание праильного значения выбранной случайным образом кнопке
                ans3.setText(Integer.toString(rightAnswer));

                // генерация неверных ответов для оставшихся кнопок
                number = (int)(Math.random() * 20 - 20);
                ans1.setText(Integer.toString(number));
                number = (int)(Math.random() * 20 - 20);
                ans2.setText(Integer.toString(number));
                break;
        }

        // обнуляем переменную правильного ответа
        rightAnswer = 0;
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
                        tittle.setText(getTimerText());
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

    // метод выполняющейся при нажатии на один из вариантов ответа
    void optimize(Button ans){
        // добавление пользовательского ответа в массив пользовательских ответов
        userAnswers.add(Integer.parseInt(ans.getText().toString()));
        Log.d(LOG_TAG, '\n' + "RightAnswers: " + rightAnswers + '\n' + "UserAnswers: " + userAnswers);

        // проверка кол-ва решённых выражений
        if(currentExp == expressionsCount - 1){
            // остановка таймера
            timerTask.cancel();

            // создание intent-а для передачи данных между activity и открытия новых активностей
            Intent intent = new Intent(getApplicationContext(), Results.class);

            // передаём кол-во выражений в Results
            intent.putExtra("expressionsCount", expressionsCount);

            // передаём время выполнения в Results
            intent.putExtra("time", time);

            // передаём массив пользовательских ответов в Results
            intent.putExtra("userAnswers", (Serializable) userAnswers);

            // передаём массив правильных ответов в Results
            intent.putExtra("rightAnswers", (Serializable) rightAnswers);

            // запускаем новую активность
            startActivity(intent);
        }

        // генерация следующего выражения
        onButtonClick();

        // увеличение кол-ва решённых выражений
        currentExp++;
    }
}