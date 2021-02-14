package com.easylife.proadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.easylife.proadmin.Models.AccountModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Block extends AppCompatActivity {

    List<AccountModel> accountList;
    DatabaseReference fdb;

    Button block;
    EditText username;
    int z=0;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);

        block=findViewById(R.id.block);
        username=findViewById(R.id.username);

        dialog= new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        accountList= new ArrayList<>();


        fdb= FirebaseDatabase.getInstance().getReference("accounts");
        fdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                accountList.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    AccountModel accountModel = data.getValue(AccountModel.class);
                    accountList.add(accountModel);

                    // Toast.makeText(Result.this, accountModel.getName(), Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Block.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });



        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                z=0;
                String user= username.getText().toString();
                for(int i=0;i<accountList.size();i++){
                    if(accountList.get(i).getPubgName().equals(user)){
                        HashMap<String,String> map = new HashMap<>();
                        map.put("androidId",accountList.get(i).getAndroidId());
                        FirebaseDatabase.getInstance().getReference("blocked list").push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Block.this, "User is added", Toast.LENGTH_SHORT).show();
                            }
                        });
                        z=1;
                        break;
                    }
                }

                if(z==0){
                    Toast.makeText(Block.this, "No user found", Toast.LENGTH_SHORT).show();
                }


            }
        });




    }
}
