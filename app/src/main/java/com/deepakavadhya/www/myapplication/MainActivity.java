package com.deepakavadhya.www.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;
    double longitudeGPS;
    double latitideGPS;
    static String locationCordinate = "25.2633448,82.9850574";
    Button sendLocationToContactButton, showPathButton, extremePanicButton;
    List numberAndPlaceIDList;
    List placeList;
    static List<List<String>> listOfSafePlaces;
    boolean internetDownloadStatus = false;
    boolean gpsStatus = false;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        sendLocationToContactButton = (Button) findViewById(R.id.sendContactButton);
        showPathButton = (Button) findViewById(R.id.showPathButton);
        extremePanicButton = (Button) findViewById(R.id.panicButton);
        sendLocationToContactButton.setEnabled(false);
        showPathButton.setEnabled(false);
        extremePanicButton.setEnabled(false);
        progressBar.setVisibility(View.INVISIBLE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitideGPS = location.getLatitude();
                longitudeGPS = location.getLongitude();
                locationCordinate = latitideGPS + "," + longitudeGPS;
                Log.v("locationCodinate", locationCordinate);
                //editText.setText(locationCordinate);
                new MyTask().execute(locationCordinate);
                //progressBar.setVisibility(View.INVISIBLE);
                if (latitideGPS + "," + longitudeGPS != null) {
                    gpsStatus = true;
                }
                Toast.makeText(getApplicationContext(), "GPS signal" + gpsStatus + " " + locationCordinate, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        configure_button();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }


    void configure_button() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 10);
            }
            return;
        }


        sendLocationToContactButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //progressBar.setVisibility(View.VISIBLE);
                //pb.setVisibility(View.VISIBLE);
                //Toast.makeText(getApplicationContext(),"Waiting for GPS signal",Toast.LENGTH_SHORT).show();
                //noinspection MissingPermission

                Intent locationSend = new Intent(MainActivity.this, SendLocationToContact.class);
                locationSend.putExtra("presentCordinate", locationCordinate);
                startActivity(locationSend);


            }
        });
        showPathButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //progressBar.setVisibility(View.VISIBLE);
                //pb.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Waiting for GPS signal", Toast.LENGTH_SHORT).show();
                //noinspection MissingPermission
                //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
                //Toast.makeText(getApplicationContext(),"Waiting for internet signal",Toast.LENGTH_SHORT).show();
                //new MyTask().execute(locationCordinate);
                Intent locationSend = new Intent(MainActivity.this, ShowPath.class);

                startActivity(locationSend);


            }
        });
        extremePanicButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //progressBar.setVisibility(View.VISIBLE);
                //pb.setVisibility(View.VISIBLE);
                //Toast.makeText(getApplicationContext(), "Waiting for GPS signal", Toast.LENGTH_SHORT).show();
                //noinspection MissingPermission
                //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
                //Toast.makeText(getApplicationContext(),"Waiting for internet signal",Toast.LENGTH_SHORT).show();
                //new MyTask().execute(locationCordinate);
                //Intent locationSend = new Intent(MainActivity.this, ShowPath.class);
                //startActivity(locationSend);
                Toast.makeText(getApplicationContext(), "on long press to start" , Toast.LENGTH_SHORT).show();


            }
        });
        extremePanicButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                List<String> numberList = new ArrayList<String>();
                Toast.makeText(getApplicationContext(), "starting service", Toast.LENGTH_SHORT).show();
                sendLocationToContactButton.performClick();
                for (int i = 0; i < listOfSafePlaces.size(); i++) {
                    numberList.add(listOfSafePlaces.get(i).get(2));
                    Toast.makeText(getApplicationContext(), "sending msg to" + listOfSafePlaces.get(i).get(2), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });


    }


    public void callAmbulance(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:101"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 12);
            }

            return;
        }
        startActivity(callIntent);
    }

    public void callPolice(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:100"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 11);
            }

            return;
        }
        startActivity(callIntent);
        //Toast.makeText(getApplicationContext(), "work in progress", Toast.LENGTH_SHORT).show();
        //call 911
    }

    public void callCab(View view) {
        try {
            PackageManager pm = getPackageManager();
            pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES);
            String clint_ID = "8b-EqxPAxnK06ZjvCtYlktl3C82NafET";
            String baseUberUri = "uber://?action=setPickup&pickup=my_location";
            Uri builtUri = Uri.parse(baseUberUri).buildUpon()
                    .appendQueryParameter("clint_id", clint_ID)
                    .build();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(builtUri.toString()));
            startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            // No Uber app! Open mobile website.
            String url = "https://m.uber.com/sign-up?client_id=<CLIENT_ID>";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.registerContact:
                Intent registerContact = new Intent(MainActivity.this, RegisterContactMenu.class);
                startActivity(registerContact);
                //Toast.makeText(getApplicationContext(),"Item 1 Selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.registerYourself:

                Intent registerYourself = new Intent(MainActivity.this, RegisterYourselfMenu.class);
                startActivity(registerYourself);
                //Toast.makeText(getApplicationContext(),"Item 2 Selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.getGPS:
                progressBar.setVisibility(View.VISIBLE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 10);
                    }

                }
                Toast.makeText(getApplicationContext(),"waiting for gps signal", Toast.LENGTH_SHORT).show();
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class MyTask extends AsyncTask<String, Void, List<List<String>>> {


        @Override
        protected List<List<String>> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJsonStr = null;
            String location = params[0];
            String radius = "500";
            List<List<String>> templistOfSafePlaces = new ArrayList<>();

            // List list = new ArrayList<>();
            try {
                final String FORECAST_BASE_URL =
                        "https://maps.googleapis.com/maps/api/place/nearbysearch/json?opennow";
                final String LOCATION = "location";
                final String RADIUS = "radius";

                //final String NAME = "name";
                //final String RANKBY = "rankby";
                //final String TYPES = "types";
                //final String OPENNOW = "opennow";
                final String KEY = "key";
                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(LOCATION, location)
                        .appendQueryParameter(RADIUS, radius)
                        .appendQueryParameter(KEY, "AIzaSyAOy4Whr_dTq39Sdb5f-wdEbe9IGv-jZJo")
                        .build();

                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }
                forecastJsonStr = buffer.toString();
                //Log.v("json",forecastJsonStr);

                JSONObject temp = new JSONObject(forecastJsonStr);
                JSONArray results = temp.getJSONArray("results");


                for (int i = 0; i < results.length(); i++) {
                    JSONObject t = results.getJSONObject(i);
                    String place_id = t.getString("place_id");
                    /////wait


                    HttpURLConnection urlConnection2 = null;
                    BufferedReader reader2 = null;
                    String forecastJsonStr2 = null;
                    List<String> detailOfPlace2 = new ArrayList<>();

                    try {
                        final String FORECAST_BASE_URL2 =
                                "https://maps.googleapis.com/maps/api/place/details/json?";
                        final String PLACE_ID = "placeid";
                        //final String KEY = "key";
                        Uri builtUri2 = Uri.parse(FORECAST_BASE_URL2).buildUpon()
                                .appendQueryParameter(PLACE_ID, place_id)
                                .appendQueryParameter(KEY, "AIzaSyAOy4Whr_dTq39Sdb5f-wdEbe9IGv-jZJo")
                                .build();

                        URL url2 = new URL(builtUri2.toString());
                        urlConnection2 = (HttpURLConnection) url2.openConnection();
                        urlConnection2.setRequestMethod("GET");
                        urlConnection2.connect();
                        InputStream inputStream2 = urlConnection2.getInputStream();
                        StringBuffer buffer2 = new StringBuffer();

                        reader2 = new BufferedReader(new InputStreamReader(inputStream2));

                        String line2;
                        while ((line2 = reader2.readLine()) != null) {

                            buffer2.append(line2 + "\n");
                        }
                        forecastJsonStr2 = buffer2.toString();
                        //Log.v("json",forecastJsonStr);

                        JSONObject temp2 = new JSONObject(forecastJsonStr2);
                        JSONObject result2 = temp2.getJSONObject("result");

                        String formatted_phone_number;
                        try {
                            formatted_phone_number = result2.getString("formatted_phone_number");
                        } catch (Exception e) {
                            formatted_phone_number = "NA";
                        }
                        String name = result2.getString("name");

                        JSONObject geometry = result2.getJSONObject("geometry");
                        JSONObject location2 = geometry.getJSONObject("location");

                        String lat = location2.getString("lat");
                        String lng = location2.getString("lng");
                        String loc = lat + "," + lng;

                        detailOfPlace2.add(name);
                        detailOfPlace2.add(loc);
                        detailOfPlace2.add(formatted_phone_number);
                        Log.v("number", formatted_phone_number);
                        Log.v("name", name);
                        Log.v("latlng", loc);
                        //return detailOfPlace2;
                        try {
                            templistOfSafePlaces.add(detailOfPlace2);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }

                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (final IOException e) {

                            }
                        }

                    }
                    //Log.v("json",forecastJsonStr);


                    ////wait
                    //list.add(place_id);
                }

                return templistOfSafePlaces;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {

                    }
                }

            }
            //Log.v("json",forecastJsonStr);


            return null;


        }

        @Override
        protected void onPostExecute(List<List<String>> result) {
            listOfSafePlaces = result;
            extremePanicButton.setEnabled(true);
            sendLocationToContactButton.setEnabled(true);
            showPathButton.setEnabled(true);
            progressBar.setVisibility(View.INVISIBLE);



        }


    }
}
