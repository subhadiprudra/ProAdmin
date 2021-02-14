package com.easylife.proadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.easylife.proadmin.Adapder.JoinAdapter;
import com.easylife.proadmin.Adapder.MatchAdapter;
import com.easylife.proadmin.Models.JoiendModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JoinendPerson2 extends AppCompatActivity {

    String matchId;
    List<JoiendModel> list;

    JoinAdapter adapter;
    RecyclerView recyclerView;
    ProgressDialog dialog;

    EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joinend_person2);

        searchText=findViewById(R.id.search);

        matchId= getIntent().getStringExtra("matchId");
        list= new ArrayList<>();

        dialog = new ProgressDialog(JoinendPerson2.this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Initialising...");
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();


        adapter= new JoinAdapter(list,this);
        recyclerView= findViewById(R.id.joinedList);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseDatabase.getInstance().getReference("pubg mobile").child("joined").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    JoiendModel model = data.getValue(JoiendModel.class);
                    if(model.getMatchId().equals(matchId)){
                        list.add(model);
                    }
                }

                adapter.setItemlist(list);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                adapter.setItemlist(getSearchedList(list,s.toString()));
                adapter.notifyDataSetChanged();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




    }


    List<JoiendModel> sheachedList= new ArrayList<>();

    public List<JoiendModel> getSearchedList(List<JoiendModel> list,String key) {

        if (!sheachedList.equals(null)) {
            sheachedList.clear();
        }

        for (int i = 0; i < list.size(); i++) {
            if ( list.get(i).getName().toLowerCase().contains(key.toLowerCase()) || list.get(i).getPubgId().toLowerCase().contains(key.toLowerCase())) {
                sheachedList.add(list.get(i));
            }
        }


        return sheachedList;

    }


}
