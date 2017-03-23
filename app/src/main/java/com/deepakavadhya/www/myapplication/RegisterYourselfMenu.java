package com.deepakavadhya.www.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterYourselfMenu extends AppCompatActivity {
    EditText name, number;
    //Cursor c;
    String Name;
    String Number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_yourself_menu);

    }

    public void storeInDB(View v) {
        Toast.makeText(getApplicationContext(), "save started", Toast.LENGTH_LONG).show();
        name = (EditText) this.findViewById(R.id.editText1);
        number = (EditText) this.findViewById(R.id.editText2);
        String str_name = name.getText().toString();
        String str_number = number.getText().toString();
        SQLiteDatabase db;
        db = openOrCreateDatabase("PersonalNumDB", Context.MODE_PRIVATE, null);
        //Toast.makeText(getApplicationContext(), "personal db created",Toast.LENGTH_LONG).show();

        db.execSQL("CREATE TABLE IF NOT EXISTS details(name VARCHAR,number VARCHAR);");
        //Toast.makeText(getApplicationContext(), "table created",Toast.LENGTH_LONG).show();
        //Cursor c=db.rawQuery("SELECT * FROM details", null);


        db.execSQL("INSERT INTO details VALUES('" + str_name + "','" + str_number + "');");
        Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();
        db.close();


    }
}
