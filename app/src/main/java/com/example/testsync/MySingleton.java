package com.example.testsync;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static  MySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private MySingleton(Context context)
    {
        this.mCtx = context;
        this.requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue()
    {
        if(this.requestQueue == null)
        {
            this.requestQueue = Volley.newRequestQueue(this.mCtx.getApplicationContext());
        }

        return this.requestQueue;
    }
    public static synchronized MySingleton getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new MySingleton(context);
        }

        return mInstance;
    }

    public<T> void addToRequestQue(Request<T> request)
    {
        this.getRequestQueue().add(request);
    }
}
