package com.easylife.proadmin.Adapder;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.easylife.proadmin.Models.MoneyModel;
import com.easylife.proadmin.MoneyRequest;
import com.easylife.proadmin.NotificatonHelper;
import com.easylife.proadmin.R;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CLIPBOARD_SERVICE;

public class MoneyAdapter extends RecyclerView.Adapter<MoneyAdapter.ViewHolder> {

    private static final int UPI_PAYMENT =0 ;
    List<MoneyModel> itemlist;
    Context mContext;
    String token;

    private ClipboardManager myClipboard;
    private ClipData myClip;
    NotificatonHelper notificatonHelper;


    public MoneyAdapter(List<MoneyModel> itemList, Context mContext) {
        this.itemlist = itemList;
        this.mContext = mContext;
        notificatonHelper= new NotificatonHelper(mContext);
    }

    @NonNull
    @Override
    public MoneyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        myClipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
        View view= LayoutInflater.from(mContext).inflate(R.layout.money_request_item,parent,false);
        return new  MoneyAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MoneyAdapter.ViewHolder holder, final int position) {

        token=itemlist.get(position).getToken();

        holder.number.setText("Number : "+itemlist.get(position).getNumber());
        holder.amount.setText("Amount : "+itemlist.get(position).getAmount());
        holder.id.setText("User id : "+itemlist.get(position).getId());
        holder.time.setText(itemlist.get(position).getTime());
        holder.slNo.setText(position+1+".");
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Delete");

                builder.setMessage("Are you sure to delete this request?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                notificatonHelper.sendNotificaton(token,"Money request approved","Your money has been sent to your paytm wallet.");
                                FirebaseDatabase.getInstance().getReference("withdraw request").child(itemlist.get(position).getParent()).removeValue();



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

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myClip = ClipData.newPlainText("text", itemlist.get(position).getNumber());
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(mContext, "Number copied", Toast.LENGTH_SHORT).show();


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

        TextView number,amount,id,time,slNo;
        CardView cardView;
        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            number=itemView.findViewById(R.id.number);
            amount = itemView.findViewById(R.id.amount);
            id=itemView.findViewById(R.id.accountNo);
            cardView=itemView.findViewById(R.id.card);
            time=itemView.findViewById(R.id.time);
            slNo=itemView.findViewById(R.id.slno);
            button=itemView.findViewById(R.id.delete);

        }
    }

    public void setItemList(List<MoneyModel> list){
        itemlist=list;
    }



}
