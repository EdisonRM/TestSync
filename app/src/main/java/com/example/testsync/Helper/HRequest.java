package com.example.testsync.Helper;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class HRequest {
    private static HRequest mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private HRequest(Context context)
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
    public static synchronized HRequest getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new HRequest(context);
        }

        return mInstance;
    }

    public<T> void addToRequestQue(Request<T> request)
    {
        this.getRequestQueue().add(request);
    }
}
