package com.easylife.proadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import com.easylife.proadmin.Adapder.MatchAdapter;
import com.easylife.proadmin.Models.JoiendModel;
import com.easylife.proadmin.Models.MatchModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Matches extends AppCompatActivity {

    DatabaseReference fdb;
    List<MatchModel> matchModelList;
    MatchAdapter adapter;
    RecyclerView recyclerView;
    ProgressDialog dialog;
    List<JoiendModel> joinedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        matchModelList=new ArrayList<>();

        adapter= new MatchAdapter(matchModelList,this);
        recyclerView= findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Initialising...");
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();





        fdb=FirebaseDatabase.getInstance().getReference("pubg mobile").child("matches");
        fdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                matchModelList.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    MatchModel matchModel = data.getValue(MatchModel.class);
                    assert matchModel != null;
                    matchModelList.add(matchModel);
                    isDeletable(matchModel.getDate(),matchModel.getTime());


                }

                joinedList= new ArrayList<>();

                FirebaseDatabase.getInstance().getReference("pubg mobile").child("joined").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        joinedList.clear();
                        for(DataSnapshot data : dataSnapshot.getChildren()){
                            JoiendModel model = data.getValue(JoiendModel.class);
                            joinedList.add(model);

                        }

                        adapter.setItemlist(matchModelList);
                        adapter.setJoinedList(joinedList);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Matches.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }


    public boolean isDeletable(String date,String time){

        int a=date.indexOf("/");
        int b= date.indexOf("/",a+1);

        int day= Integer.parseInt(date.substring(0,a));
        int month = Integer.parseInt(date.substring(a+1,b));
        int year = Integer.parseInt(date.substring(b+1));

        int c= time.indexOf(":");
        int d= time.indexOf(" ");
        int hour = Integer.parseInt(time.substring(0,c));
        int mint= Integer.parseInt(time.substring(c+1,d));

        if(time.substring(d+1).equals("PM")){
            hour=hour+12;
        }

        day=day+1;

        Calendar cl = Calendar.getInstance();
        cl.set(year,month-1,day,hour,mint,0);

        long t= System.currentTimeMillis();

        Log.d("xxxxxxxxxx",cl.getTimeInMillis()+"");
        Log.d("xxxxxxxxxx",t+"");


        if(t>cl.getTimeInMillis()){
            Log.d("xxxxxxxxxx","true");
            return true;
        }else {
            Log.d("xxxxxxxxxx","false");
            return false;
        }


    }

    DatabaseReference ref;

    public void deleteMatch(String matchId){

        ref =FirebaseDatabase.getInstance().getReference("pubg mobile");

        ref.child("matches").child(matchId).removeValue();
        ref.child("IdPass").child(matchId).removeValue();
        ref.child("result").child(matchId).removeValue();


    }


}
