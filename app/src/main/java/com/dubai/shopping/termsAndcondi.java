package com.dubai.shopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dubai.shopping.Prevalent.Prevalent;

public class termsAndcondi extends AppCompatActivity {
private Button buts,ex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_andcondi);
        buts = (Button) findViewById(R.id.button);
        ex = (Button) findViewById(R.id.button2);
        buts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(termsAndcondi.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    ex.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
    System.exit(0);
    }
});

    }
}