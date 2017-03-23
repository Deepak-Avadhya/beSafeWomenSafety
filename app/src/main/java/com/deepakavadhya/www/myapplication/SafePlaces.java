package com.deepakavadhya.www.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.deepakavadhya.www.myapplication.MainActivity.listOfSafePlaces;

public class SafePlaces extends AppCompatActivity {


    List<String> nameList;
    //List<String> numberList;
    List<String> latlngList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_places);
        nameList = new ArrayList<>();
        //numberList = new ArrayList<>();
        latlngList = new ArrayList<>();


        for(int i = 0; i< listOfSafePlaces.size(); i++){
            List<String> detailOfPlace = listOfSafePlaces.get(i);
            nameList.add(detailOfPlace.get(0));
            latlngList.add(detailOfPlace.get(1));
            //numberList.add(detailOfPlace.get(2));
        }
        Object[] objDays = nameList.toArray();
        String[] nameToAdapter = Arrays.copyOf(objDays, objDays.length, String[].class);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.number_list, nameToAdapter);

        ListView listView = (ListView) findViewById(R.id.mobile_list2);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String item = ((TextView)view).getText().toString();
                int index=nameList.indexOf(item);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + latlngList.get(index)));
                startActivity(intent);


                //Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();

            }
        });
    }
}