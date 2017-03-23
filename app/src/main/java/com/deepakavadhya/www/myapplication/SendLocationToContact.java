package com.deepakavadhya.www.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SendLocationToContact extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    String tempMessage="please help me i am in big trouble. I am at ";
    String tempPhoneNumber="8127320243";
    String presentLocation;
    Cursor c;
    List<String> name,number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_location_to_contact);

        Intent intent = getIntent();
        presentLocation=intent.getStringExtra("presentCordinate");
        name= new ArrayList<>();
        number= new ArrayList<>();
        SQLiteDatabase db;
        db=openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
        c=db.rawQuery("SELECT * FROM details", null);
        while(c.moveToNext())
        {
            name.add(c.getString(0));
            number.add(c.getString(1));

        }

    }

    public void autogenerateSMSAndSendToSavedContact(View view) {
        if(c.getCount()==0)
        {
            Toast.makeText(getApplicationContext(), "contact list is empty", Toast.LENGTH_SHORT).show();
            showMessage("Error", "No records found.");
            return;
        }

        for(int i=0;i<number.size();i++){
            sendSMSMessage(number.get(i));
        }

    }

    public void callContact(View view) {


        if(c.getCount()==0)
        {
            Toast.makeText(getApplicationContext(), "contact list is empty", Toast.LENGTH_SHORT).show();
            showMessage("Error", "No records found.");
            return;
        }
        Intent numberSend = new Intent(SendLocationToContact.this,CallContact.class);
        /*
        numberSend.putExtra("size",number.size()+"");
        for (int i=0;i<number.size();i++) {
            numberSend.putExtra("name"+i,name.get(i) );
            numberSend.putExtra("number"+i,number.get(i) );
        }
        */
        startActivity(numberSend);

    }
    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    protected void sendSMSMessage(String phoneNo) {

        tempPhoneNumber=phoneNo;



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(tempPhoneNumber, null, tempMessage, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent to"+tempPhoneNumber,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS to"+ tempPhoneNumber+"faild, please try again.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

    }

}
