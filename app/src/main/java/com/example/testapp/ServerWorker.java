package com.example.testapp;

import android.location.Location;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ServerWorker {

    private static final String SERVER_ADDRESS = "http://192.168.43.36:3000/";

    public interface LocationSenderCallBack{
        void onSuccessLocationSend(String result);
        void onErrorLocationSend(Exception e);
    }

    public static void sendLocation(final Location location, final LocationSenderCallBack callBack){
        StringRequest request = new StringRequest( Request.Method.POST, SERVER_ADDRESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.onSuccessLocationSend(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onErrorLocationSend( error );
            }
        } ){

            @Override
            public byte[] getBody() throws AuthFailureError {

                try {
                    JSONObject params = new JSONObject(  );
                    params.put( "latitude" , Double.toString(  location.getLatitude()));
                    params.put( "longitude" , Double.toString(  location.getLongitude()));
                    //return params.toString().getBytes();
                    return super.getBody();
                } catch (JSONException e) {
                    //e.printStackTrace();
                    callBack.onErrorLocationSend( e );
                }
                return super.getBody();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>(  );
                params.put( "latitude" , Double.toString(  location.getLatitude()));
                params.put( "longitude" , Double.toString(  location.getLongitude()));
                return params;
            }
        };
        App.addToRequestQueue( request );
    }
}
