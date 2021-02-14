package com.easylife.besttools.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.easylife.besttools.BasicFunction;
import com.easylife.besttools.FriendModel;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.HashMap;
import java.util.List;

public class SendInviteService extends Service{

    String pageId,fb_dtsg,inviteNote;
    List<FriendModel> friendModelList;
    Boolean sendToMessenger;
    BasicFunction basicFunction;

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public void setFb_dtsg(String fb_dtsg) {
        this.fb_dtsg = fb_dtsg;
    }

    public void setInviteNote(String inviteNote) {
        this.inviteNote = inviteNote;
    }

    public void setFriendModelList(List<FriendModel> friendModelList) {
        this.friendModelList = friendModelList;
    }

    public void setSendToMessenger(Boolean sendToMessenger) {
        this.sendToMessenger = sendToMessenger;
    }

    public void setBasicFunction(BasicFunction basicFunction) {
        this.basicFunction = basicFunction;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        basicFunction= new BasicFunction();

        new SendInvite().execute();






        return START_STICKY;
    }






    public class SendInvite extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] objects) {

            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("c_user", basicFunction.getCookie().c_user);
            hm.put("xs", basicFunction.getCookie().xs);

            HashMap<String, String> data = new HashMap<String, String>();

            data.put("page_id",pageId);
            data.put("invite_note",inviteNote);
            data.put("send_in_messenger",sendToMessenger.toString());
            data.put("ref","modal_page_invite_dialog_v2");
            data.put("fb_dtsg",fb_dtsg);

            for(int i=0;i<40;i++){
                String s="invitees["+i+"]";
                data.put(s,friendModelList.get(i).id);

            }

            try{

            Connection.Response homePage = Jsoup.connect("https://www.facebook.com/pages/batch_invite_send/")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36 OPR/62.0.3331.116")
                    .data(data)
                    .cookies(hm)
                    .method(Connection.Method.POST)
                    .execute();}

	         catch (Exception e) {
            e.printStackTrace();

        }

            return null;
        }
    }
}
