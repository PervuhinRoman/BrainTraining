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
import android.widget.SeekBar;
import android.widget.TextView;

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

        btnApply = (Button) findViewById(R.id.btnApply);
        expressionsCount = (SeekBar) findViewById(R.id.seekBar);
        txtExpressionsCount = (TextView) findViewById(R.id.txtExpressionsCount);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Training.class);
                intent.putExtra("expressionsCount", Integer.parseInt(txtExpressionsCount.getText().toString()));
                startActivity(intent);
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

        Button testBtn = (Button) findViewById(R.id.button4);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), Results.class);
                startActivity(intent1);
            }
        });
    }
}

// TODO: добавить обработку исключений ввода (отсутсвие ввода или буквы) в поле @+id/txtExpressionsCount
// TODO: добавить таймер
