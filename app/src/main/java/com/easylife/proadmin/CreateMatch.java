package com.easylife.proadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateMatch extends AppCompatActivity {

    EditText matchId,prizeDetails,perKill,entryFee,maxEntries;
    Dialog mydialog;
    TextView time,date;
    Button timep,datep,post;
    Spinner type,version,map;
    DatabaseReference ref;

    final String[] typra={"Solo","Duo","Squard"};
    final String[] versiona={"TPP","FPP"};
    final String[] mapa={"ERANGEL","SANHOK","ALL WEAPON","TDM"};
    NotificatonHelper notificatonHelper;

    HashMap<String,String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_match);
        mydialog = new Dialog(this);

         hashMap= new HashMap<>();
         notificatonHelper= new NotificatonHelper(this);

        timep=findViewById(R.id.timePicker);
        datep=findViewById(R.id.datePicker);
        time=findViewById(R.id.time);
        date=findViewById(R.id.date);
        type=findViewById(R.id.type);
        version=findViewById(R.id.version);
        map=findViewById(R.id.map);
        matchId=findViewById(R.id.id);
       // prizePool=findViewById(R.id.prize_pool);
        prizeDetails=findViewById(R.id.prize_details);
        perKill=findViewById(R.id.per_kill);
        entryFee=findViewById(R.id.entry_fee);
        maxEntries=findViewById(R.id.max_entries);
        post=findViewById(R.id.send);





        timep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        datep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        ArrayAdapter ta = new ArrayAdapter(this,android.R.layout.simple_spinner_item,typra);
        ta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(ta);

        ArrayAdapter va = new ArrayAdapter(this,android.R.layout.simple_spinner_item,versiona);
        va.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        version.setAdapter(va);

        ArrayAdapter ma = new ArrayAdapter(this,android.R.layout.simple_spinner_item,mapa);
        ma.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        map.setAdapter(ma);


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                push();
            }
        });



        if(getIntent().getStringExtra("action").equals("edit")){

            matchId.setText(getIntent().getStringExtra("id"));
            time.setText(getIntent().getStringExtra("time"));
            date.setText(getIntent().getStringExtra("date"));
           // prizePool.setText(getIntent().getStringExtra("prize pool"));
            prizeDetails.setText(getIntent().getStringExtra("prize details"));
            perKill.setText(getIntent().getStringExtra("per kill"));
            entryFee.setText(getIntent().getStringExtra("entry fee"));
            maxEntries.setText(getIntent().getStringExtra("max entries"));
            perKill.setText(getIntent().getStringExtra("per kill"));

            hashMap.put("entryNumber", getIntent().getStringExtra("entries"));
            hashMap.put("status",getIntent().getStringExtra("status"));

            //type.setSelection(typra.);



        }














    }


    public void showTimePicker(){

        TextView ok,cancel;
        final TimePicker timePicker;

        mydialog.setContentView(R.layout.time_picker);

        timePicker=mydialog.findViewById(R.id.time_picker);
        ok= mydialog.findViewById(R.id.time_picker_ok);
        cancel=mydialog.findViewById(R.id.time_picker_cancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    String x;
                    int hour;
                    if(timePicker.getHour()>12){
                        hour=timePicker.getHour()-12;
                        x="PM";
                    }else {
                        hour=timePicker.getHour();
                        x="AM";
                    }

                    setTime(hour+":"+timePicker.getMinute()+" "+x);

                }
                else {

                    String x;
                    int hour;
                    if(timePicker.getCurrentHour()>12){
                        hour=timePicker.getCurrentHour()-12;
                        x="PM";
                    }else {
                        hour=timePicker.getCurrentHour();
                        x="AM";
                    }
                    setTime(hour+":"+timePicker.getCurrentMinute()+" " +x);
                }

                mydialog.dismiss();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mydialog.dismiss();

            }
        });

        mydialog.show();


    }

    public void showDatePicker(){

        TextView ok,cancel;
        final DatePicker datePicker;

        mydialog.setContentView(R.layout.date_picker);

        datePicker=mydialog.findViewById(R.id.date_picker);
        ok= mydialog.findViewById(R.id.date_picker_ok);
        cancel=mydialog.findViewById(R.id.date_picker_cancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int m=datePicker.getMonth()+1;

                setDate(datePicker.getDayOfMonth()+"/"+m+"/"+datePicker.getYear());
               /* year=datePicker.getYear();
                month=datePicker.getMonth();
                day=datePicker.getDayOfMonth();*/
                mydialog.dismiss();


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mydialog.dismiss();

            }
        });

        mydialog.show();


    }

    public void setTime(String t){
        time.setText(t);
    }

    public void setDate(String d){
        date.setText(d);
    }


    public void push(){

        ref = FirebaseDatabase.getInstance().getReference("pubg mobile").child("matches").child(matchId.getText().toString());



        hashMap.put("matchId",matchId.getText().toString());
        hashMap.put("time",time.getText().toString());
        hashMap.put("date",date.getText().toString());
     //   hashMap.put("prizePool",prizePool.getText().toString());
        hashMap.put("prizeDetails",prizeDetails.getText().toString());
        hashMap.put("perKill",perKill.getText().toString());
        hashMap.put("entryFee",entryFee.getText().toString());
        hashMap.put("type",type.getSelectedItem().toString());
        hashMap.put("version",version.getSelectedItem().toString());
        hashMap.put("map",map.getSelectedItem().toString());
        hashMap.put("maxEntries",maxEntries.getText().toString());

        if(getIntent().getStringExtra("action").equals("create")) {
            hashMap.put("entryNumber", "0");
            hashMap.put("status","ongoing");

        }

        ref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CreateMatch.this, "Successfully pushed", Toast.LENGTH_SHORT).show();
                    if(getIntent().getStringExtra("action").equals("create")) {
                        notificatonHelper.sendNotificaton("/topics/all","New Match released","Starts on "+time.getText().toString()+". Hurry up to join");
                    }

                }else {
                    Toast.makeText(CreateMatch.this, "Error", Toast.LENGTH_SHORT).show();
                }

            }
        });






    }



}
