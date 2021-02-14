package com.easylife.proadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ReleaseIdPass extends AppCompatActivity {

    com.google.android.material.textfield.TextInputEditText id,pass;
    Button button;

    DatabaseReference ref;
    NotificatonHelper notificatonHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_id_pass);
        id=findViewById(R.id.matchId_r);
        pass = findViewById(R.id.pass_r);
        button=findViewById(R.id.r_post);
        notificatonHelper= new NotificatonHelper(this);

        ref= FirebaseDatabase.getInstance().getReference("pubg mobile").child("IdPass").child(getIntent().getStringExtra("id"));

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ida=dataSnapshot.child("id").getValue(String.class);
                String passa=dataSnapshot.child("pass").getValue(String.class);

                id.setText(ida);
                pass.setText(passa);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("id",id.getText().toString());
                hashMap.put("pass",pass.getText().toString());
                ref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete()){
                            Toast.makeText(ReleaseIdPass.this, "Done", Toast.LENGTH_SHORT).show();
                            notificatonHelper.sendNotificaton("/topics/match"+getIntent().getStringExtra("id"),"Room id and password released","Room id and password of match #"+getIntent().getStringExtra("id")+" is released. join soon");
                        }else if(task.isCanceled()){
                            Toast.makeText(ReleaseIdPass.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
}
