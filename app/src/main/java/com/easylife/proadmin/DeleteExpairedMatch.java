package com.easylife.proadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
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

public class DeleteExpairedMatch extends AppCompatActivity {


    DatabaseReference fdb;
    List<MatchModel> matchModelList;
    List<JoiendModel> joinedList;
    List<String> dlist;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_expaired_match);

        dlist=new ArrayList<>();
        matchModelList=new ArrayList<>();
        joinedList = new ArrayList<>();

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Initialising...");
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();







        FirebaseDatabase.getInstance().getReference("pubg mobile").child("joined").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                joinedList.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    JoiendModel model = data.getValue(JoiendModel.class);
                    joinedList.add(model);

                }



                fdb=FirebaseDatabase.getInstance().getReference("pubg mobile").child("matches");
                fdb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        matchModelList.clear();

                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            MatchModel matchModel = data.getValue(MatchModel.class);
                            assert matchModel != null;
                            matchModelList.add(matchModel);
                            if (isDeletable(matchModel.getDate(), matchModel.getTime())) {

                                deleteMatch(matchModel.getMatchId());
                                dialog.setTitle("Deleting match #"+matchModel.getMatchId());
                                dialog.setMessage("Please wait...");


                            }
                        }
                        dialog.dismiss();
                        finish();



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(DeleteExpairedMatch.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        ref.child("results").child(matchId).removeValue();

        for(JoiendModel joiendModel : joinedList){
            if(joiendModel.getMatchId().equals(matchId)){
                ref.child("joined").child(matchId+joiendModel.getAccountId()).removeValue();
            }
        }


    }

}
