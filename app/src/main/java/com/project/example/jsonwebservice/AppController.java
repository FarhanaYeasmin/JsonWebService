package com.project.example.jsonwebservice;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Date;

/**
 * Created by nipun on 11/9/2016.
 */

public class AppController extends Application {

    private static AppController instance;
    private static RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }
    public static AppController getInstance(){
        return instance;
    }
    private RequestQueue getRequestQueue(){
        if (requestQueue==null){
            requestQueue = Volley.newRequestQueue(this);
        }
        return requestQueue;
    }
    public void addToRequestQueue(Request request){
        getRequestQueue().add(request);
    }

}
