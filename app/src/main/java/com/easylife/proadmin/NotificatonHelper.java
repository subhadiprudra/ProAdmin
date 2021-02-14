package com.easylife.proadmin;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class NotificatonHelper {

    JSONObject root = new JSONObject();
    JSONObject notification = new JSONObject();
    Context context;

    public NotificatonHelper(Context context) {
        this.context = context;
    }

    public void sendNotificaton(String to, String title, String body){

        try {
            root.put("to",to);
            notification.put("body",body);
            notification.put("title",title);
            root.put("notification",notification);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", root, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i("onResponse", "" + response.toString());
                Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","key=AAAAP8Os9s8:APA91bHNwrhGXs8dIrWHO1hCWCdVc-SIJYkpdX1_j0kxa8Ki6IEk02ydSfVB5lCJ4wNPkjF4JJa_iQ6p0YVR5bjZ-veZOV3ZYvBQasHFi786GFbvPwScbg_HkaSNFeN407wWia9r3ALb");
                params.put("Content-Type","application/json");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);

    }


}
