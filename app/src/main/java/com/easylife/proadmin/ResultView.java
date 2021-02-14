package com.easylife.proadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.easylife.proadmin.Adapder.MatchAdapter;
import com.easylife.proadmin.Models.MatchModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ResultView extends AppCompatActivity {

    List<MatchModel> matchModelList;
    MatchAdapter adapter;
    RecyclerView recyclerView;

    DatabaseReference fdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_view);

        matchModelList=new ArrayList<>();

        adapter= new MatchAdapter(matchModelList,this);
        recyclerView= findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        fdb= FirebaseDatabase.getInstance().getReference("pubg mobile").child("matches");
        fdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                matchModelList.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    MatchModel matchModel = data.getValue(MatchModel.class);
                    assert matchModel != null;

                    if(matchModel.getStatus().equals("released")){
                        matchModelList.add(matchModel);
                    }


                }

                adapter.setItemlist(matchModelList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(ResultView.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });


    }
}
