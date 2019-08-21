package com.jhk.memorie_alert;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

   private EditText et;
    private RecycleAdapter adapter;
    private final String dbName ="memorie_alert";
    private final String tablename = "memory";
    public static Context mContext;

    SQLiteDatabase sqlite = null;

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    btn = findViewById(R.id.button);

    if(isMyServiceRunning(ExampleService.class)) {
       // Toast.makeText(getApplicationContext(), "서비스 실행중", Toast.LENGTH_SHORT).show();
        StopService();

    }
    mContext = this;
        try{
            sqlite = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);

            sqlite.execSQL("CREATE TABLE IF NOT EXISTS " + tablename +
                    " ( title VARCHAR(20), content VARCHAR(50));");


        }catch (SQLiteException se) {
            Toast.makeText(getApplicationContext(),se.getMessage(),Toast.LENGTH_LONG).show();
            Log.e("",se.getMessage());
        }

        init();
        getData();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Add.class);
                intent.putExtra("activity", this.getClass());
                startActivity(intent);

            }
        });
    }


   public void getData(){
        SQLiteDatabase ReadDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
        Cursor c= ReadDB.rawQuery("SELECT * FROM  "+ tablename,null);

        if(c!= null){
            if(c.moveToFirst()){
                do{
                    Data data = new Data();
                    data.setTitle(c.getString(c.getColumnIndex("title")));
                    data.setContent(c.getString(c.getColumnIndex("content")));
                    adapter.addItem(data);
                }while(c.moveToNext());
            }
        }
        ReadDB.close();

        adapter.notifyDataSetChanged();
    }

    public void init(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecycleAdapter();
        recyclerView.setAdapter(adapter);
    }

    public void delete_items(String s) {
        sqlite.execSQL("DELETE FROM memory WHERE content is '"+s+"'");
        init();
        getData();

    }

    public void startService(String title, String content) {

        Intent serviceIntent = new Intent(this, ExampleService.class);
        serviceIntent.putExtra("inputExtra",title);
        serviceIntent.putExtra("content",content);

        startService(serviceIntent);
    }

    public void StopService() {
        Intent serviceIntent = new Intent(this, ExampleService.class);
        stopService(serviceIntent);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;

            }
        }
        return false;
    }


    }


