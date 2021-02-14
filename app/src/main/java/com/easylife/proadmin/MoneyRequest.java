package com.easylife.proadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.easylife.proadmin.Adapder.MatchAdapter;
import com.easylife.proadmin.Adapder.MoneyAdapter;
import com.easylife.proadmin.Models.MatchModel;
import com.easylife.proadmin.Models.MoneyModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MoneyRequest extends AppCompatActivity {

    private static final int UPI_PAYMENT = 0;
    RecyclerView recyclerView;
    MoneyAdapter adapter;

    List<MoneyModel> requestList;

    DatabaseReference ref;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_request);

        requestList = new ArrayList<>();

        recyclerView=findViewById(R.id.mo_request);
        adapter= new MoneyAdapter(requestList,this);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Initialising...");
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();

        ref= FirebaseDatabase.getInstance().getReference("withdraw request");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestList.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    MoneyModel moneyModel = data.getValue(MoneyModel.class);
                    assert moneyModel != null;

                    requestList.add(moneyModel);


                }

                adapter.setItemList(requestList);
                adapter.notifyDataSetChanged();
                dialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







    }



}
