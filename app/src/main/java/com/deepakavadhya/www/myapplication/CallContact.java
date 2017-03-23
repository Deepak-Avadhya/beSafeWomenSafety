package com.deepakavadhya.www.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CallContact extends AppCompatActivity {


    List<String> nameList;
    List<String> numberList;
    //String totalNumber;
    Cursor c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_contact);
        nameList = new ArrayList<>();
        numberList = new ArrayList<>();
        SQLiteDatabase db;
        db=openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
        c=db.rawQuery("SELECT * FROM details", null);
        while(c.moveToNext())
        {
            nameList.add(c.getString(0));
            numberList.add(c.getString(1));

        }
        Object[] objDays = nameList.toArray();
        String[] nameToAdapter = Arrays.copyOf(objDays, objDays.length, String[].class);


        /*
        Intent intent = getIntent();
        totalNumber = intent.getStringExtra("size");
        for (int i=0;i<Integer.parseInt(totalNumber);i++){
            nameList.add(intent.getStringExtra("name"+i));
            numberList.add(intent.getStringExtra("number"+i));
        }
        */


        /*
        for(int i=0;i<MainActivity.listOfSafePlaces.size();i++){
            List<String> detailOfPlace = listOfSafePlaces.get(i);
            nameList.add(detailOfPlace.get(0));
            latlngList.add(detailOfPlace.get(1));
            //numberList.add(detailOfPlace.get(2));
        }
        */

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.number_list,nameToAdapter );

        ListView listView = (ListView) findViewById(R.id.mobile_list2);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String item = ((TextView)view).getText().toString();
                int index=nameList.indexOf(item);



                Toast.makeText(getBaseContext(),"calling "+numberList.get(index), Toast.LENGTH_LONG).show();

            }
        });
    }
}