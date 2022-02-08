package com.example.braintraining;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class Training extends AppCompatActivity {

    private static final String LOG_TAG = "ArraysState";

    Button ans1;
    Button ans2;
    Button ans3;
    TextView txtQuestion;

    String question = "";             // выражение
    int rightAnswer = 0;              // правильный ответ

    List<Integer> userAnswers = new ArrayList<>();              // массив пользовательских ответов
    List<Integer> rightAnswers = new ArrayList<>();             // массив правильных ответов

    int expressionsCount;                                        // кол-во выражений
    List<String> expressions = new ArrayList<>();                // массив выражений

    String[] actions = {"+", "-"};    // список действий

    int number;                       // элемент цифры для выражения
    String action;                    // элемент действия для выражения

    int currentExp = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        ans1 = (Button) findViewById(R.id.button1);
        ans2 = (Button) findViewById(R.id.button2);
        ans3 = (Button) findViewById(R.id.button3);
        txtQuestion = (TextView) findViewById(R.id.question);

        // получение ресурсов цветов
        Resources resources = getResources();
        int okColor = resources.getColor(R.color.okColor,  null);
        int noColor = resources.getColor(R.color.noColor,  null);

        // кол-во выражений, которые необходимо создать, полученное из MainActivity
        Intent expressionsCountIntent = getIntent();
        expressionsCount = expressionsCountIntent.getIntExtra("expressionsCount", 51);
        Log.d(LOG_TAG, Integer.toString(expressionsCount));

        // заполнение массива выражений и массива правильных решений
        for(int i = 0; i < expressionsCount; i++){
            onButtonClick();
        }

        // отладочный вывод сгенерированных выражений и правильных ответов
        Log.d(LOG_TAG, '\n' + "RightAnswers: " + rightAnswers + '\n' + "Expressions: " + expressions + " length: " + expressions.size());
        
        // обработка нажатия на вариант ответа #1
        ans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userAnswers.add(Integer.parseInt(ans1.getText().toString()));
                Log.d(LOG_TAG, '\n' + "RightAnswers: " + rightAnswers + '\n' + "UserAnswers: " + userAnswers);
                onButtonClick();
            }
        });

        // обработка нажатия на вариант ответа #2
        ans2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userAnswers.add(Integer.parseInt(ans2.getText().toString()));
                Log.d(LOG_TAG, '\n' + "RightAnswers: " + rightAnswers + '\n' + "UserAnswers: " + userAnswers);
                onButtonClick();
            }
        });

        // обработка нажатия на вариант ответа #3
        ans3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userAnswers.add(Integer.parseInt(ans3.getText().toString()));
                Log.d(LOG_TAG, '\n' + "RightAnswers: " + rightAnswers + '\n' + "UserAnswers: " + userAnswers);
                onButtonClick();
            }
        });
    }

    // основной метод
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
        // TODO: реализовать систему, исключающую возможность совпадения рандомного неправильного ответа с правильным
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
}

// TODO: установить ограгичение по кол-ву выражений