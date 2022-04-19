package com.example.braintraining;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ArraysState";

    ImageView btnStart;
    ImageView btnPlay;
    SeekBar expressionsCount;
    SeekBar skBarNumbersCount;
    EditText txtExpressionsCount;
    EditText txtNumbersCount;
    TextView title;
    ImageView menuIcon;
    DrawerLayout navigationDrawerLayout;
    NavigationView navigationView;

    int defaultExpressionsCount = 25;
    int defaultNumbersCount = 3;

    // ================================ для генерации выражений ====================================
    String question = "";
    int rightAnswer;

    int questionsCount;            // пользовательский ввод | кол-во выражений
    int numbersCount;              // пользовательский ввод | кол-во чисел в выражении
    final int numbersRange = 10;
    final int answersCount = 3;    // пользовательский ввод | кол-во ответов
    int answersRange;              // пользовательский ввод | диапазон ответов

    ArrayList<String> alreadyGeneratedNumbers = new ArrayList<>(); // для генерации
    ArrayList<Integer> alreadyGeneratedAnswers = new ArrayList<>(); // для генерации

    String[] actions = {"+", "-"}; // для генерации
    String prevAction;             // для генерации

    ArrayList<ArrayList<String>> answers = new ArrayList<>();   // ответы

    ArrayList<Integer> rightAnswers = new ArrayList<>();             // массив правильных ответов
    ArrayList<String> expressions = new ArrayList<>();               // массив выражений
    // =============================================================================================

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.btn_start);
        btnPlay = findViewById(R.id.btn_play);
        expressionsCount = findViewById(R.id.expressions_count_seekBar);
        skBarNumbersCount = findViewById(R.id.numbers_count_seekBar);
        txtExpressionsCount = findViewById(R.id.expressions_count);
        txtNumbersCount = findViewById(R.id.numbers_count);
        title = findViewById(R.id.title_of_appBar);

        // кнопки изначально
        //btnStart.setVisibility(View.VISIBLE);
        btnPlay.setVisibility(View.GONE);

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

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    for(int i = 0; i < questionsCount; i++) {
                        mGen();
                    }
                    Log.d(LOG_TAG, "exp: " + expressions);
                    btnPlay.setVisibility(View.VISIBLE);
                } catch (NumberFormatException e){
                    // обработка исключения: пустая строка кол-ва выражений
                    Toast nfeToast = Toast.makeText(getApplicationContext(), getResources().getText(R.string.enter_the_expressions_count), Toast.LENGTH_SHORT);
                    nfeToast.setGravity(Gravity.CENTER, 0, 0);
                    nfeToast.show();
                }
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Training.class);
                intent.putExtra("expressionsCount", (Serializable) questionsCount);
                intent.putExtra("questions", (Serializable) expressions);
                intent.putExtra("answers", (Serializable) answers);
                intent.putExtra("rightAnswers", (Serializable) rightAnswers);
                startActivity(intent);
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

        skBarNumbersCount.setMax(10);
        skBarNumbersCount.setMin(2);
        skBarNumbersCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
    // ====================================== генерация ========================================
    // генерация чисел для выражения
    void numberGeneration(int  numbersCount, int numbersRange){
        String number = Integer.toString((int)(Math.random() * (numbersRange - 1) + 1));

        for(int i = 0; i < numbersCount; i++) {

            // проверка
            while (alreadyGeneratedNumbers.size() > 0 && alreadyGeneratedNumbers.contains(number)) {
                // генерация
                number = Integer.toString((int) (Math.random() * (numbersRange + 1) + 1));
            }
            alreadyGeneratedNumbers.add(number);
        }
    }

    // генерация действия
    String actionGeneration(String[] actions){
        String action = actions[(int) (Math.random() * 2)];

        while(action.equals(prevAction)) {
            action = actions[(int) (Math.random() * 2)];
        }

        prevAction = action;

        return action;
    }

    // генерация случайных ответов
    String answerGeneration(int rightAnswer, int answersRange){
        int number = (int)(Math.random() * (answersRange + 1) - (answersRange + 1));

        // проверка
        while(alreadyGeneratedAnswers.size() > 0 && (alreadyGeneratedAnswers.contains(number) || number == rightAnswer)){
            // генерация
            number = (int)(Math.random() * (answersRange + 1) - (answersRange + 1));
        }
        alreadyGeneratedAnswers.add(number);

        return Integer.toString(number);
    }

    void mGen(){
        numberGeneration(numbersCount, numbersRange);
        ArrayList<String> itemAnswers = new ArrayList<>();
        String number;
        String action;

        // генерация первого числа
        number = alreadyGeneratedNumbers.get(0);
        question += number;

        // начинаем считать правильный ответ
        rightAnswer = Integer.parseInt(number);

        for(int i = 1; i < alreadyGeneratedNumbers.size(); i++){
            // генерируем действие и добавляем в выражение
            action = actionGeneration(actions);
            question += action;

            // генерация числа и добавление в выражение
            number = alreadyGeneratedNumbers.get(i);
            question += number;

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

        // генерация случайных ответов
        int rightAnsInd = (int)(Math.random() * (answersCount) + 0);

        for(int i = 0; i < answersCount; i++){
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
    }
    // =========================================================================================
}