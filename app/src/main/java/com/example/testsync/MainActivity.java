package com.example.testsync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText etNombre;
    RecyclerView.LayoutManager layoutManager;

    RecyclerAdapter adapter;
    ArrayList<Contact> lstContact = new ArrayList<>();
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.recyclerView = (RecyclerView)findViewById(R.id.rcvContacto);
        this.etNombre = (EditText)findViewById(R.id.cNombre);
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.setHasFixedSize(true);

        this.adapter = new RecyclerAdapter(this.lstContact);
        this.recyclerView.setAdapter(this.adapter);

        this.readFromLocalStorage();
        this.broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                readFromLocalStorage();
            }
        };
    }

    public void grabarNombre(View view)
    {
        String name = this.etNombre.getText().toString();
        this.saveToAppServer(name);

        this.etNombre.setText("");
    }

    private void readFromLocalStorage()
    {
        this.lstContact.clear();
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = dbHelper.readFromLocalDB(database);

        while(cursor.moveToNext())
        {
            String name = cursor.getString(cursor.getColumnIndex(DbContract.NAME));
            int sync_status = cursor.getInt(cursor.getColumnIndex(DbContract.SYNC_STATUS));

            this.lstContact.add(new Contact(name, sync_status));
        }

        adapter.notifyDataSetChanged();
        cursor.close();
        dbHelper.close();
    }

    private void saveToAppServer(final String name)
    {


        if(this.checkNetworkConnection())
        {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try
                            {
                                JSONObject jsonObject = new JSONObject(response);
                                String Response = jsonObject.getString("response");

                                if(Response.equals("OK"))
                                {
                                    saveToLocalStorage(name,DbContract.SYNC_STATUS_OK);
                                }
                                else
                                {
                                    saveToLocalStorage(name,DbContract.SYNC_STATUS_FAILED);
                                }
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener(){
                public void onErrorResponse(VolleyError error){
                    saveToLocalStorage(name,DbContract.SYNC_STATUS_FAILED);
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("name",name);
                    return params;
                }
            };

            MySingleton.getInstance(MainActivity.this).addToRequestQue(stringRequest);
        }
        else
        {
            this.saveToLocalStorage(name, DbContract.SYNC_STATUS_FAILED);
        }
    }

    public boolean checkNetworkConnection()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    private void saveToLocalStorage(String name, int sync)
    {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        dbHelper.saveToLocalDB(name, sync, database);
        this.readFromLocalStorage();
        dbHelper.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(this.broadcastReceiver,new IntentFilter(DbContract.UI_UPDATE_BROADCAST));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(this.broadcastReceiver);
    }
}
