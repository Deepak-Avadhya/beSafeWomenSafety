package com.deepakavadhya.www.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class ShowPath extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_path);
    }

    public void nearestSafePlace(View view) {
        Intent i_send = new Intent(ShowPath.this,SafePlaces.class);
        startActivity(i_send);
    }

    public void mySavedPlace(View view) {
        Toast.makeText(getApplicationContext(),"Work in Progress",Toast.LENGTH_SHORT).show();
    }


}
