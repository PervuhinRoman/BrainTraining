package com.example.braintraining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Results extends AppCompatActivity {

    private static final String LOG_TAG = "ArraysState";

    Button btnAgain;
    Button btnGoHome;
    TextView txtTime;
    TextView txtMistakes;
    TextView title;
    TextView rightAnswersHeader;
    TextView userAnswersHeader;
    LinearLayout rightAnswersArrayLinearLayout;
    LinearLayout userAnswersArrayLinearLayout;
    LinearLayout questionsArrayLinearLayout;
    DrawerLayout navigationDrawerLayout;
    ImageView menuIcon;
    NavigationView navView;

    int expressionsCount = 0;
    Double time = 0.0;

    ArrayList<String> questions = new ArrayList<>();           // массив выражений
    List<String> userAnswers = new ArrayList<>();              // массив пользовательских ответов
    List<Integer> rightAnswers = new ArrayList<>();             // массив правильных ответов

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //btnAgain = findViewById(R.id.btnAgain);
        btnGoHome = findViewById(R.id.btnGoHome);
        txtTime = findViewById(R.id.solution_time);
        txtMistakes = findViewById(R.id.mistakes_count);
        rightAnswersHeader = findViewById(R.id.rightAnswersHeader);
        userAnswersHeader = findViewById(R.id.userAnswersHeader);
        rightAnswersArrayLinearLayout = findViewById(R.id.rightAnswersArrayLinearLayout);
        userAnswersArrayLinearLayout = findViewById(R.id.userAnswersArrayLinearLayout);
        questionsArrayLinearLayout = findViewById(R.id.questionsArrayLinearLayout);
        navigationDrawerLayout = findViewById(R.id.navigation_drawer_layout);
        menuIcon = findViewById(R.id.menu_icon);
        navView = findViewById(R.id.navigation);
        title = findViewById(R.id.title_of_appBar);
        title.setText(R.string.title_of_resultsActivity);

        // обработка navigation drawer
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                navigationDrawerLayout.openDrawer(Gravity.START);
            }
        });

        // обработка навигации
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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
                        finish();
                        break;
                }
                return true;
            }
        });

        // получение времени выполнения задания
        Intent intent = getIntent();
        time = intent.getDoubleExtra("time", 0.0);
        Log.d(LOG_TAG, "Time from Training: " + time);
        txtTime.setText(getResources().getString(R.string.solution_time) + " " + getTimerText(time));

        // получение пользовательских ответов
        userAnswers = intent.getStringArrayListExtra("userAnswers");

        // получение верных ответов
        rightAnswers = intent.getIntegerArrayListExtra("rightAnswers");

        // получение массива выражений
        questions = intent.getStringArrayListExtra("questions");

        generateTextViewFromArray();

        // вывод кол-ва ошибок
        txtMistakes.setText(getResources().getString(R.string.mistakes_count) + " " + expressionsCount + " / " + rightAnswers.size());

        // обрабока нажатия на кнопку Home
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time = 0.0;
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // получение времени
    String getTimerText(Double time) {
        int rounded = (int) Math.round(time);
        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;

        return formatTime(seconds, minutes);
    }

    // форматирование вывода времени
    String formatTime(int seconds, int minutes) {
        return String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }

    void generateTextViewFromArray(){
        for(int i = 0; i < userAnswers.size(); i++){
            // настройка layout атрибутов
            LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            textViewLayoutParams.setMargins(10, 10, 10, 10);

            // натройка оформления вывода правильного ответа
            TextView rightItem = new TextView(Results.this, null, 0, R.style.custom_right_answers);
            rightItem.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.roboto_bold));
            rightItem.setLayoutParams(textViewLayoutParams);

            // натройка оформления вывода пользовательского ответа
            TextView userItem = new TextView(Results.this, null, 0, R.style.custom_wrong_answers);
            userItem.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.roboto_bold));
            userItem.setLayoutParams(textViewLayoutParams);

            // натройка оформления вывода примеров
            TextView que = new TextView(Results.this, null, 0, R.style.custom_right_answers);
            que.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.roboto_bold));
            que.setLayoutParams(textViewLayoutParams);

            // установка цвета ответа
            if(rightAnswers.get(i) == Integer.parseInt(userAnswers.get(i))){
                userItem.setBackgroundResource(R.drawable.custom_right_answers_background);
            } else {
                userItem.setBackgroundResource(R.drawable.custom_wrong_answers_background);

                // подсчёт кол-ва ошибок
                expressionsCount++;
            }

            // вывод текущего верного ответа
            rightItem.setText(Integer.toString(rightAnswers.get(i)));
            rightAnswersArrayLinearLayout.addView(rightItem);

            // вывод текущего пользовательського ответа
            userItem.setText(userAnswers.get(i));
            userAnswersArrayLinearLayout.addView(userItem);

            // вывод текущего пользовательського ответа
            que.setText(questions.get(i));
            questionsArrayLinearLayout.addView(que);
        }
    }
}