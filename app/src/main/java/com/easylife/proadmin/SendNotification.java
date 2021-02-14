package com.easylife.proadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SendNotification extends AppCompatActivity {

    EditText title,body;
    Button send;
    NotificatonHelper notificatonHelper;
    String to = "/topics/all";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

        title=findViewById(R.id.title);
        body=findViewById(R.id.body);
        send=findViewById(R.id.send);
        notificatonHelper= new NotificatonHelper(this);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificatonHelper.sendNotificaton("/topics/all",title.getText().toString(),body.getText().toString());

            }
        });

    }
}
