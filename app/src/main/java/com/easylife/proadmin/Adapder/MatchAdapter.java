package com.easylife.proadmin.Adapder;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.easylife.proadmin.CreateMatch;
import com.easylife.proadmin.Models.JoiendModel;
import com.easylife.proadmin.Models.MatchModel;
import com.easylife.proadmin.R;
import com.easylife.proadmin.ReleaseIdPass;
import com.easylife.proadmin.Result;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder>  {

    List<MatchModel> itemlist;
    Context mContext;
    final String[] statusa={"ongoing","locked","dead","released"};
    DatabaseReference dbr;
    List<JoiendModel> joinedList;



    public MatchAdapter(List<MatchModel> itemlist, Context mContext) {
        this.itemlist = itemlist;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MatchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view= LayoutInflater.from(mContext).inflate(R.layout.match_item,parent,false);
        return new  MatchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MatchAdapter.ViewHolder holder, final int position) {

        holder.title.setText(itemlist.get(position).getType()+" -Match #"+itemlist.get(position).getMatchId());
        holder.subTitel.setText("Starts on "+itemlist.get(position).getDate()+" at "+itemlist.get(position).getTime());
      //  holder.prizePool.setText(itemlist.get(position).getPrizePool());
        holder.perKill.setText(itemlist.get(position).getPerKill());
        holder.entryFee.setText(itemlist.get(position).getEntryFee());
        holder.winp.setText(itemlist.get(position).getPrizeDetails());
        holder.version.setText(itemlist.get(position).getVersion());
        holder.map.setText(itemlist.get(position).getMap());
        holder.process.setText("Joined : "+joinNumber(joinedList,itemlist.get(position).getMatchId())+" / "+itemlist.get(position).getMaxEntries());
        holder.progressBar.setMax(Integer.parseInt(itemlist.get(position).getMaxEntries()));
        holder.progressBar.setProgress(joinNumber(joinedList,itemlist.get(position).getMatchId()));
        holder.type.setText(itemlist.get(position).getType());

        ArrayAdapter ta = new ArrayAdapter(mContext,android.R.layout.simple_spinner_item,statusa);
        ta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(ta);

        dbr= FirebaseDatabase.getInstance().getReference("pubg mobile").child("matches").child(itemlist.get(position).getMatchId()).child("status");
        final int[] p = {0};
        final String[] statu = {"ongoing"};
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                statu[0] = dataSnapshot.getValue(String.class);

                if(statu[0].equals("ongoing")){
                    p[0] =0;
                }else if(statu[0].equals("locked")){
                    p[0] =1;
                }else if(statu[0].equals("dead")){
                    p[0] =2;
                }else {
                    p[0]=3;
                }



                holder.spinner.setSelection(p[0]);

                if(holder.spinner.getSelectedItemPosition()==2){
                    holder.releaseResult.setVisibility(View.VISIBLE);
                    holder.idpass.setVisibility(View.GONE);
                    holder.editResult.setVisibility(View.GONE);
                }else if(holder.spinner.getSelectedItemPosition()==1){
                    holder.releaseResult.setVisibility(View.GONE);
                    holder.idpass.setVisibility(View.VISIBLE);
                    holder.editResult.setVisibility(View.GONE);
                }else if(holder.spinner.getSelectedItemPosition()==0){
                    holder.releaseResult.setVisibility(View.GONE);
                    holder.idpass.setVisibility(View.GONE);
                    holder.editResult.setVisibility(View.GONE);
                }

                if(statu[0].equals("released")){
                    holder.releaseResult.setVisibility(View.GONE);
                    holder.idpass.setVisibility(View.GONE);
                    holder.editResult.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbr= FirebaseDatabase.getInstance().getReference("pubg mobile").child("matches").child(itemlist.get(position).getMatchId());
                dbr.child("status").setValue(holder.spinner.getSelectedItem().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(mContext, "Done", Toast.LENGTH_SHORT).show();
                        }else  {
                            Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        holder.idpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ReleaseIdPass.class);
                intent.putExtra("id",itemlist.get(position).getMatchId());
                mContext.startActivity(intent);
            }
        });




        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(p[0]==0) {
                    Intent intent = new Intent(mContext, CreateMatch.class);
                    intent.putExtra("action", "edit");
                    intent.putExtra("id", itemlist.get(position).getMatchId());
                    intent.putExtra("time", itemlist.get(position).getTime());
                    intent.putExtra("date", itemlist.get(position).getDate());
                   // intent.putExtra("prize pool", itemlist.get(position).getPrizePool());
                    intent.putExtra("prize details", itemlist.get(position).getPrizeDetails());
                    intent.putExtra("per kill", itemlist.get(position).getPerKill());
                    intent.putExtra("entry fee", itemlist.get(position).getEntryFee());
                    intent.putExtra("type", itemlist.get(position).getType());
                    intent.putExtra("version", itemlist.get(position).getVersion());
                    intent.putExtra("map", itemlist.get(position).getMap());
                    intent.putExtra("max entries", itemlist.get(position).getMaxEntries());
                    intent.putExtra("entries", itemlist.get(position).getEntryNumber());
                    intent.putExtra("status", statu[0]);
                    mContext.startActivity(intent);
                }
            }
        });

        holder.releaseResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fstp= Integer.parseInt(itemlist.get(position).getPrizeDetails());
                int fstppm=0;
                switch (itemlist.get(position).getType()){
                    case "Squard":
                        fstppm=fstp/4;
                        break;

                    case "Duo" :
                        fstppm=fstp/2;
                        break;

                    case "Solo" :
                        fstppm=fstp;
                        break;
                }
                Intent intent = new Intent(mContext, Result.class);
                intent.putExtra("matchId",itemlist.get(position).getMatchId());
                intent.putExtra("perKill",itemlist.get(position).getPerKill());
                intent.putExtra("1stPrize",String.valueOf(fstppm));
                intent.putExtra("action","create");
                intent.putExtra("type",itemlist.get(position).getType());
                mContext.startActivity(intent);
            }
        });

        holder.editResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Result.class);
                intent.putExtra("matchId",itemlist.get(position).getMatchId());
                intent.putExtra("perKill",itemlist.get(position).getPerKill());
                //intent.putExtra("action",g);
                mContext.startActivity(intent);
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,subTitel,prizePool,perKill,entryFee,version,map,process,winp,type;
        ProgressBar progressBar;
        CardView cardView;
        Spinner spinner;
        Button save,editResult;
        Button releaseResult,idpass;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.titel);
            subTitel=itemView.findViewById(R.id.subTitle);
           // prizePool=itemView.findViewById(R.id.prize_pool_r);
            perKill=itemView.findViewById(R.id.per_kill_r);
            entryFee=itemView.findViewById(R.id.entry_fee_r);
            version=itemView.findViewById(R.id.version_r);
            map=itemView.findViewById(R.id.map_r);
            process=itemView.findViewById(R.id.joined);
            cardView=itemView.findViewById(R.id.card);
            spinner = itemView.findViewById(R.id.status_sp);
            progressBar =itemView.findViewById(R.id.processbar);
            save=itemView.findViewById(R.id.save_status);
            releaseResult=itemView.findViewById(R.id.r_result);
            idpass=itemView.findViewById(R.id.r_idp);
            editResult=itemView.findViewById(R.id.edit_result);
            winp=itemView.findViewById(R.id.fstprize_r);
            type=itemView.findViewById(R.id.type);







        }
    }

    public void setItemlist(List<MatchModel> itemlist) {
        this.itemlist = itemlist;
    }

    public int joinNumber(List<JoiendModel> list,String matchId){

        int i=0;

        for(JoiendModel model : list){
            if(model.getMatchId().equals(matchId)){
                i++;
            }
        }

        return i;

    }

    public void setJoinedList(List<JoiendModel> joinedList) {
        this.joinedList = joinedList;
    }
}
