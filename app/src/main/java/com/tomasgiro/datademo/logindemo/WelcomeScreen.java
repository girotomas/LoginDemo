package com.tomasgiro.datademo.logindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeScreen extends AppCompatActivity {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        button =findViewById(R.id.take_a_picture);

    }

    public void onTakeAPicture(View view){
        startActivity(new Intent(this,mRecyclerView.class));
    }
}
