package com.example.braintraining;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
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

    // для генерации выражений
    String question = "";             // выражение
    int questionsCount;               // кол-во выражений
    int rightAnswer = 0;              // правильный ответ

    String[] actions = {"+", "-"};    // список действий
    String action;                    // элемент действия для выражения

    ArrayList<String> userAnswers = new ArrayList<>();              // массив пользовательских ответов
    ArrayList<String> rightAnswers = new ArrayList<>();             // массив правильных ответов
    ArrayList<String> questions = new ArrayList<>();                 // массив выражений

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
        Log.d(LOG_TAG, "Expressions count from MainActivity: " + questionsCount);

        // создание "нулевого" выражения
        generation0();

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

    // основной метод генерации
    @SuppressLint("SetTextI18n")
    public void generation0(){
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
        questions.add(question);
        txtQuestion.setText(question);
        question = "";

        // добавляем вычисленный правильный ответ в массив правильных ответов
        rightAnswers.add(Integer.toString(rightAnswer));

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

    // метод выполняющейся при нажатии на один из вариантов ответа
    void onBtnClick(Button ans){
        // добавление пользовательского ответа в массив пользовательских ответов
        userAnswers.add(ans.getText().toString());
        Log.d(LOG_TAG, '\n' + "RightAnswers: " + rightAnswers + '\n' + "UserAnswers: " + userAnswers + '\n' + questions);

        // проверка кол-ва решённых выражений
        if(currentExp == questionsCount - 1){
            // остановка таймера
            timerTask.cancel();

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
        }

        // генерация следующего выражения
        generation0();

        // увеличение кол-ва решённых выражений
        currentExp++;
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