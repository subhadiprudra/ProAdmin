package com.easylife.proadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easylife.proadmin.Adapder.MatchAdapter;
import com.easylife.proadmin.Models.AccountModel;
import com.easylife.proadmin.Models.Item;
import com.easylife.proadmin.Models.JoiendModel;
import com.easylife.proadmin.Models.MatchModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Result extends AppCompatActivity {

    EditText kill,namet;
    Button add,save,addNew;
    RecyclerView recyclerView,searchView;

    List<Item> itemList,copy;
    ResultAdapter adapter;
    SearchAdapter searchAdapter;
    TextView winner;

    DatabaseReference fdb,fdb1;
    List<String> names;
    List<JoiendModel> joinedList;
    List<AccountModel> accountList;

    String matchId;
    String perKill,action;
    int x;
    HashMap<String,String> data= new HashMap<>();
    int z=0;

    List<String> winnerNames;

    Dialog dialog;
    int fstp;
    NotificatonHelper notificatonHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        winnerNames = new ArrayList<>();

        matchId= getIntent().getStringExtra("matchId");
        perKill=getIntent().getStringExtra("perKill");
        action = getIntent().getStringExtra("action");
        fstp= Integer.parseInt(getIntent().getStringExtra("1stPrize"));
        notificatonHelper= new NotificatonHelper(this);




        save=findViewById(R.id.save_result);
        recyclerView =findViewById(R.id.re_result);
        addNew=findViewById(R.id.addNew);



        dialog=new Dialog(this);
        dialog.setContentView(R.layout.search_view);
        searchView=dialog.findViewById(R.id.search_view);
        kill=dialog.findViewById(R.id.kill);
        namet=dialog.findViewById(R.id.name);
        add=dialog.findViewById(R.id.add);

        itemList= new ArrayList<>();
        copy= new ArrayList<>();

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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Result.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });


        adapter= new ResultAdapter(itemList,this);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        names = new ArrayList<>();
        joinedList = new ArrayList<>();

        fdb= FirebaseDatabase.getInstance().getReference("pubg mobile").child("joined");
        fdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               names.clear();
               joinedList.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    JoiendModel joiendModel = data.getValue(JoiendModel.class);
                    assert joiendModel != null;
                    if(joiendModel.getMatchId().equals(matchId)){
                        joinedList.add(joiendModel);
                    }
                }

                for(int i=0;i<joinedList.size();i++){
                    names.add(joinedList.get(i).getName());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Result.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });


        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });




        add.setOnClickListener(new View.OnClickListener() {
            int j;

            @Override
            public void onClick(View v) {
                if (!namet.getText().toString().equals("") && !kill.getText().toString().equals("")) {


                String kills = kill.getText().toString();
                String name = namet.getText().toString();
                j = 0;
                for (int i = 0; i < itemList.size(); i++) {
                    if (name.equals(itemList.get(i).name)) {
                        j = 1;
                        break;
                    }
                }

                if (j == 0) {
                    Item item = new Item();
                    item.setKills(kills);
                    item.setName(name);
                    itemList.add(item);
                    adapter.setItemlist(getShortedList(itemList));
                    adapter.notifyDataSetChanged();
                    namet.setText("");
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Result.this, "User name is already added", Toast.LENGTH_SHORT).show();
                }
            }
        }
        });

        searchAdapter= new SearchAdapter(names,this);
        searchView.hasFixedSize();
        searchView.setAdapter(searchAdapter);
        searchView.setLayoutManager(new LinearLayoutManager(this));

        namet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searchAdapter.setList(getSearchedList(names,s.toString()));
                searchAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {




                    AlertDialog.Builder builder = new AlertDialog.Builder(Result.this);
                    builder.setTitle("Conform ?");

                    builder.setMessage("You can not change result after adding this. Are you sure to go forward ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    createResult();

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();






            }
        });




    }






    public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

        List<Item> itemlist;
        Context mContext;

        public ResultAdapter(List<Item> itemlist, Context mContext) {
            this.itemlist = itemlist;
            this.mContext = mContext;
        }

        @NonNull
        @Override
        public ResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(mContext).inflate(R.layout.result_item,parent,false);
            return new  ResultAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ResultAdapter.ViewHolder holder, final int position) {

            holder.slNo.setText(position+1+"");
            holder.name.setText(itemlist.get(position).name);
            holder.kills.setText(itemlist.get(position).kills);


            holder.card.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {


                    if(!winnerNames.contains(itemlist.get(position).name)){
                        winnerNames.add(itemlist.get(position).name);
                        holder.card.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }else {
                        winnerNames.remove(itemlist.get(position).name);
                        holder.card.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    return false;
                }
            });

            holder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemList.remove(position);
                    winnerNames.remove(itemlist.get(position).name);
                    notifyDataSetChanged();
                    Toast.makeText(mContext, "Removed Successfully", Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public int getItemCount() {
            if(itemlist==null){
                return 0;
            }else {
                return itemlist.size();
            }
        }

        private class ViewHolder extends RecyclerView.ViewHolder {

            TextView slNo,name,kills;
            LinearLayout card;
            Button cancel;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                slNo=itemView.findViewById(R.id.slNo);
                name=itemView.findViewById(R.id.name);
                kills = itemView.findViewById(R.id.kills);
                card=itemView.findViewById(R.id.r_card);
                cancel=itemView.findViewById(R.id.remove);


            }
        }

        public void setItemlist(List<Item> itemlist) {
            this.itemlist = itemlist;
        }

    }


    private class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

        List<String> list;
        Context mContext;

        public SearchAdapter(List<String> list, Context mContext) {
            this.list = list;
            this.mContext = mContext;
        }

        @NonNull
        @Override
        public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(mContext).inflate(R.layout.searched_item,parent,false);
            return new  SearchAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, final int position) {

            holder.textView.setText(list.get(position));
            holder.searchSurface.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    namet.setText(list.get(position));


                }
            });


        }

        @Override
        public int getItemCount() {
            if(list==null){
                return 0;
            }else {
                return list.size();
            }
        }

        private class ViewHolder extends RecyclerView.ViewHolder{

            TextView textView;
            CardView searchSurface;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                textView=itemView.findViewById(R.id.text);
                searchSurface=itemView.findViewById(R.id.search_sarface);
            }
        }

        public void setList(List<String> list){
            this.list=list;
        }


    }


    public List<Item> getShortedList(List<Item> list){

        for(int i=0;i<list.size()-1;i++){
            for(int j=0;j<list.size()-1;j++){
                int a= Integer.parseInt(list.get(j).kills);
                int b= Integer.parseInt(list.get(j+1).kills);
                if(b>a){
                   Item temp=list.get(j);
                    list.set(j,list.get(j+1));
                    list.set(j+1,temp);

                }
            }
        }

        return list;
    }

    List<String> sheachedList= new ArrayList<>();

    public List<String> getSearchedList(List<String> list,String key) {

        if (!sheachedList.equals(null)) {
            sheachedList.clear();
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).toLowerCase().contains(key.toLowerCase())) {
                sheachedList.add(list.get(i));
            }
        }


        return sheachedList;

    }




    public void createResult(){

        int z=0;

        //sendMoney(winnerName,winnerPrize);


        fdb= FirebaseDatabase.getInstance().getReference("pubg mobile").child("results").child(matchId);
        fdb1 = FirebaseDatabase.getInstance().getReference("accounts");
        for(int i=0;i<itemList.size();i++) {
            data.put("name", itemList.get(i).name);

            String k=itemList.get(i).kills;
            z=0;
            for(int j=0;j<winnerNames.size();j++){
                if(itemList.get(i).name.equals(winnerNames.get(j))){
                    z=1;
                    break;
                }
            }
            if(z==1){
                k=k+"(Winner)";
            }
            data.put("kills", k);

            x = i;
            fdb.push().setValue(data);

            try {

                String pk= perKill;
                int p= Integer.parseInt(pk);
                int kill= Integer.parseInt(itemList.get(x).kills);
                int mo=p*kill;
                z=0;
                for(int j=0;j<winnerNames.size();j++){
                    if(itemList.get(i).name.equals(winnerNames.get(j))){
                        z=1;
                        break;
                    }
                }
                if(z==1){
                    mo=fstp+mo;
                }
                String m= String.valueOf(mo);

                sendMoney(itemList.get(x).name,m);

            }catch (Exception e){}

            if(i==itemList.size()-1){
                FirebaseDatabase.getInstance().getReference("pubg mobile").child("matches").child(matchId).child("status").setValue("released");
                Toast.makeText(this, "Result released", Toast.LENGTH_SHORT).show();
                notificatonHelper.sendNotificaton("/topics/match"+matchId,"Result released","Result of match #"+matchId+" is released");
                finish();
            }

        }

    }


    public void sendMoney(String pubgName,String money){
        String t = getAccountBalance(pubgName);
        int balance = Integer.parseInt(t);

        balance = balance+Integer.parseInt(money);
        String b = String.valueOf(balance);
        FirebaseDatabase.getInstance().getReference("accounts").child(getAccountNum(pubgName)).child("balance").setValue(b);
        notificatonHelper.sendNotificaton(token(pubgName),"Money received","Your wallet is credited by ₹"+money);

        HashMap<String,String> map= new HashMap<>();
        map.put("action","Your won prize for match #"+matchId);
        map.put("amount",money);
        map.put("time",getDateTime());
        FirebaseDatabase.getInstance().getReference("transactions").child(getAccountNum(pubgName)).push().setValue(map);

    }

    public String getDateTime(){
        String  currentDateTimeString = DateFormat.getDateTimeInstance()
                .format(new Date());

        return currentDateTimeString;
    }



    int i;
    public void updateResult(){


        for(i=0;i<copy.size();i++) {



                String aid = getAccountNum(copy.get(i).name);
                fdb1 = FirebaseDatabase.getInstance().getReference("accounts").child(aid);
                fdb1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String t = dataSnapshot.child("balance").getValue(String.class);
                            // Toast.makeText(Result.this, t, Toast.LENGTH_SHORT).show();
                            int balance = Integer.parseInt(t);
                            int pk = Integer.parseInt(perKill);
                            int kill = Integer.parseInt(copy.get(i).kills);
                            balance = balance - (pk * kill);
                            String b = String.valueOf(balance);

                            fdb1.child("balance").setValue(b);

                        if(i==copy.size()-1){

                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        }





    }


    public String getAccountBalance(String acc){

        for(int i=0;i<accountList.size();i++){
            if(accountList.get(i).getPubgName().equals(acc)){
                return accountList.get(i).getBalance();
            }
        }

        return "";

    }

    public String getAccountNum(String name){
        for(int i=0;i<joinedList.size();i++){
            if(joinedList.get(i).getName().equals(name)){
                return joinedList.get(i).getAccountId();
            }
        }
        return null;
    }

    public String token(String name){
        for(int i=0;i<accountList.size();i++){
            if(accountList.get(i).getPubgName().equals(name)){
                return accountList.get(i).getToken();
            }
        }
        return null;
    }







}
