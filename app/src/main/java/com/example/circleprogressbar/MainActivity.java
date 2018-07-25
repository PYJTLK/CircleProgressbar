package com.example.circleprogressbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import circleprogressbar.CircleProgressBar;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private CircleProgressBar progressBar;
    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        progressBar = findViewById(R.id.progress);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress+=10;
                if(progress > 100){
                    progress = 0;
                }
                progressBar.setProgress(progress);
            }
        });
    }
}
