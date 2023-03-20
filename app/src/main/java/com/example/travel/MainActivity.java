package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClick1(View view) {
        Intent intent = new Intent(MainActivity.this, BigTravel.class);
        startActivity(intent);
    }
    public void onClick2(View view){
        Intent intent = new Intent(this, RouteDay.class);
        startActivity(intent);
    }
    public void onClick3(View view){
        Intent intent = new Intent(this,Walk.class);
        startActivity(intent);
    }
}
