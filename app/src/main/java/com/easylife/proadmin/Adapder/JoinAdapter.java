package com.easylife.proadmin.Adapder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.easylife.proadmin.Models.JoiendModel;
import com.easylife.proadmin.Models.MoneyModel;
import com.easylife.proadmin.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class JoinAdapter extends RecyclerView.Adapter<JoinAdapter.ViewHolder>  {

    List<JoiendModel> itemlist;
    Context mContext;

    public JoinAdapter(List<JoiendModel> itemlist, Context mContext) {
        this.itemlist = itemlist;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public JoinAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.join_item,parent,false);
        return new  JoinAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JoinAdapter.ViewHolder holder, int position) {

        holder.matchId.setText("Match id #"+itemlist.get(position).getMatchId());
        holder.username.setText("PUBG username : "+itemlist.get(position).getName());
        holder.pubgId.setText("PUBG id : "+itemlist.get(position).getPubgId());
        holder.email.setText("Email : "+itemlist.get(position).getEmail());

    }

    @Override
    public int getItemCount() {
        if(itemlist==null){
            return 0;
        }else {
           // Toast.makeText(mContext, itemlist.size()+"", Toast.LENGTH_SHORT).show();
            return itemlist.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView matchId,username,pubgId,email;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            matchId= itemView.findViewById(R.id.matchId);
            username= itemView.findViewById(R.id.username);
            pubgId= itemView.findViewById(R.id.userId);
            email= itemView.findViewById(R.id.email);
        }
    }

    public void setItemlist(List<JoiendModel> itemlist) {
        this.itemlist = itemlist;
    }
}
