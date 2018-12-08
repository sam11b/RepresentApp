package com.example.samantha.proj02brepresent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.graphics.Typeface;
import android.widget.ImageView;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Button;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.app.DownloadManager.Request;
import com.android.volley.toolbox.*;
import com.android.volley.*;
import com.android.volley.Request.Method.*;
import java.util.Map;
import java.util.HashMap;
import android.graphics.Color;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import java.io.InputStream;
import java.net.URL;
import com.android.volley.toolbox.ImageLoader;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.os.AsyncTask;
import java.util.Random;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.*;
import android.location.Location;
import com.google.android.gms.tasks.OnSuccessListener;
import android.support.v4.app.*;
import android.support.v4.content.*;
import android.content.pm.*;
import android.location.LocationManager;
import android.Manifest;


public class MainActivity extends AppCompatActivity {

    private Toolbar mTopToolbar;
    public static String Zip_Entered;
    public String state_entered = "";
    private FusedLocationProviderClient mFusedLocationClient;
    public double lat;
    public double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setContentView(R.layout.activity_main);
        mTopToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setTitle("represent.");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    99);
        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location == null) {
                                // Logic to handle location object
                                return;
                            }
                            lat = location.getLatitude();
                            lon = location.getLongitude();

                        }
                    });
        }
        //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText e = (EditText) findViewById(R.id.editText2);
                e.setText(e.getText());
                Zip_Entered = e.getText().toString();
                Intent search = new Intent(getApplicationContext(), search_page.class);
                startActivity(search);
            }
        });
        Button r = (Button) findViewById(R.id.button2);
        r.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Zip_Entered = "";
                Random rand = new Random();
                for (int i = 0; i < 5; i++) {
                    int a = rand.nextInt(9);
                    Zip_Entered += a;
                }
                //set Zip Entered
                Intent search = new Intent(getApplicationContext(), search_page.class);
                startActivity(search);
            }
        });
        final RequestQueue queue = Volley.newRequestQueue(this);
        Button curr = (Button) findViewById(R.id.currentlocation);
        final SettingsClient client = LocationServices.getSettingsClient(this);
        curr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lon + "&key=AIzaSyArftX7tWCCxN0kff0EZIxcrnzMOUmFGv8";
                StringRequest postRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject myObject = new JSONObject(response);
                                    JSONArray g = myObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components");
                                    for (int i = 0; i < g.length(); i++) {
                                        if (g.getJSONObject(i).getJSONArray("types").get(0).toString().equals("postal_code")) {
                                            Zip_Entered = g.getJSONObject(i).get("short_name").toString();
                                            break;
                                        }
                                    }

                                }catch (JSONException e) {
                                    Log.d("ERROR", "error => " + e.toString());
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                    }
                });
                queue.add(postRequest);
                Intent search = new Intent(getApplicationContext(), search_page.class);
                startActivity(search);
            }
        });

        }


    }