package com.easylife.proadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.FieldClassification;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button create,matches,result,block,sendNoti,joinList,deleteex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        create= findViewById(R.id.creatematch);
        matches = findViewById(R.id.matches);
        result = findViewById(R.id.result);
        block=findViewById(R.id.block);
        sendNoti=findViewById(R.id.sendNoti);
        joinList= findViewById(R.id.joinList);
        deleteex = findViewById(R.id.del);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CreateMatch.class);
                intent.putExtra("action","create");
                startActivity(intent);
            }
        });

        matches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Matches.class);
                startActivity(intent);
            }
        });
        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MoneyRequest.class);
                startActivity(intent);
            }
        });

        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Block.class);
                startActivity(intent);
            }
        });

        sendNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SendNotification.class);
                startActivity(intent);
            }
        });

        joinList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinedPerson.class);
                startActivity(intent);
            }
        });

        deleteex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DeleteExpairedMatch.class);
                startActivity(intent);
            }
        });
    }
}
