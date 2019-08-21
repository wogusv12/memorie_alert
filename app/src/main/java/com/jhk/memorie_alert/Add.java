package com.jhk.memorie_alert;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Add extends Activity {


    EditText input_title, input_content;
    Button commit, exit;
    SQLiteDatabase db = null;
    MainActivity ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        input_title = findViewById(R.id.editText2);
        input_content = findViewById(R.id.editText);
        commit = findViewById(R.id.button2);
        exit = findViewById(R.id.button3);
        db = openOrCreateDatabase("memorie_alert",MODE_PRIVATE, null);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!input_title.getText().toString().equals(null) && !input_title.getText().toString().equals("")) {
                    if (!input_content.getText().toString().equals(null) && !input_content.getText().toString().equals("")) {
                        try {
                            String title = input_title.getText().toString();
                            String content = input_content.getText().toString();


                            db.execSQL("INSERT INTO memory (title, content) Values ('" + title + "', '" + content + "')");
                            db.close();
                            ((MainActivity) MainActivity.mContext).init();
                            ((MainActivity) MainActivity.mContext).getData();
                            finish();

                        } catch (SQLiteException se) {
                            Toast.makeText(getApplicationContext(), se.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("", se.getMessage());

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "내용이 입력되지 않았습니다.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "제목이 입력되지 않았습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}
