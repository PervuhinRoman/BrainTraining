package com.example.braintraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnApply;
    SeekBar expressionsCount;
    TextView txtExpressionsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // изменение заголовка активности так, чтобы не изменилось название приложения в лаунчере
        setTitle("Parameters");

        btnApply = (Button) findViewById(R.id.btnApply);
        expressionsCount = (SeekBar) findViewById(R.id.seekBar);
        txtExpressionsCount = (TextView) findViewById(R.id.txtExpressionsCount);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(getApplicationContext(), Training.class);
                    intent.putExtra("expressionsCount", Integer.parseInt(txtExpressionsCount.getText().toString()));
                    startActivity(intent);
                } catch (NumberFormatException e){
                    // обработка исключения: пустая строка кол-ва выражений
                    Toast nfeToast = Toast.makeText(getApplicationContext(), "Enter the expressions count", Toast.LENGTH_SHORT);
                    nfeToast.show();
                }
            }
        });

        expressionsCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                txtExpressionsCount.setText(Integer.toString(progress / 2));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }
}

/*
*  TODO: навести "марофет"
*/