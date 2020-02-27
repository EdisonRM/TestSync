package com.example.testsync.Helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HDataBase {

    public boolean checkNetworkConnection(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }
}
