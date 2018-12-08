package com.example.samantha.proj02brepresent;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.TextureView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.support.v7.app.ActionBar;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.io.IOException;
import java.net.HttpURLConnection;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import android.view.LayoutInflater;
import android.view.View;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.widget.Button;
import java.util.ArrayList;
import java.util.Random;
import android.net.Uri;

import android.text.method.*;

public class search_page extends Activity {
    public String state_entered;
    public static String senator_code;
    public ArrayList<String> member_codes;
    public ArrayList<String> contact_urls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        member_codes = new ArrayList<String>();
        contact_urls = new ArrayList<String>();
        setContentView(R.layout.search_page);
        Toolbar mTopToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTopToolbar.setTitle("represent.");
        sendMessage(MainActivity.Zip_Entered);
        getDistrictMems();
        Button b1 = (Button) findViewById(R.id.more1);
        Button b2 = (Button) findViewById(R.id.more2);
        Button b3 = (Button) findViewById(R.id.house_more1);
        Button b4 = (Button) findViewById(R.id.house_more2);
        Button b5 = (Button) findViewById(R.id.house_more3);
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                senator_code = member_codes.get(0);
                Intent search = new Intent(getApplicationContext(), detailed_view.class);
                startActivity(search);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                senator_code = member_codes.get(1);
                Intent search = new Intent(getApplicationContext(), detailed_view.class);
                startActivity(search);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                senator_code = member_codes.get(2);
                Intent search = new Intent(getApplicationContext(), detailed_view.class);
                startActivity(search);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                senator_code = member_codes.get(3);
                Intent search = new Intent(getApplicationContext(), detailed_view.class);
                startActivity(search);
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                senator_code = member_codes.get(4);
                Intent search = new Intent(getApplicationContext(), detailed_view.class);
                startActivity(search);
            }
        });
        Button c1 = (Button) findViewById(R.id.contact1);
        Button c2 = (Button) findViewById(R.id.contact2);
        Button c3 = (Button) findViewById(R.id.house_contact1);
        Button c4 = (Button) findViewById(R.id.house_contact2);
        Button c5 = (Button) findViewById(R.id.house_contact3);
        c1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(contact_urls.get(0)));
                startActivity(browserIntent);
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(contact_urls.get(1)));
                startActivity(browserIntent);
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(contact_urls.get(2)));
                startActivity(browserIntent);
            }
        });
        c4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(contact_urls.get(3)));
                startActivity(browserIntent);
            }
        });
        c5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(contact_urls.get(4)));
                startActivity(browserIntent);
            }
        });
    }

    public void sendMessage(String Zip_Entered) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final TextView mTextView = (TextView) findViewById(R.id.responseView);
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + Zip_Entered + "&key=AIzaSyArftX7tWCCxN0kff0EZIxcrnzMOUmFGv8";
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject myObject = new JSONObject(response);
                            if (myObject.getJSONArray("results").length() == 0) {
                                MainActivity.Zip_Entered = generateNewZip();
                                sendMessage(generateNewZip());
                                getDistrictMems();
                            }
                            JSONArray city_name = myObject.getJSONArray("results").getJSONObject(0)
                                    .getJSONArray("address_components");
                            for (int i = 0; i < city_name.length(); i++) {
                                if (city_name.getJSONObject(i).getJSONArray("types").get(0).toString().equals("locality")
                                        || city_name.getJSONObject(i).getJSONArray("types").get(0).toString().equals("neighborhood")
                                        && city_name.getJSONObject(i).getJSONArray("types").get(1).toString().equals("political")) {
                                    mTextView.setText(city_name.getJSONObject(i).get("long_name").toString());
                                } else if (city_name.getJSONObject(i).getJSONArray("types").get(0).toString().equals("administrative_area_level_1")
                                        && city_name.getJSONObject(i).getJSONArray("types").get(1).toString().equals("political")) {
                                    String state = city_name.getJSONObject(i).get("short_name").toString();
                                    state_entered = city_name.getJSONObject(i).get("short_name").toString();
                                    senateMembers(state);
                                }

                            } //name of City
                        } catch (JSONException e) {
                            mTextView.setText("Error");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "Error occurred ", error);
            }
        });
        queue.add(stringRequest);
    }

    public String generateNewZip() {
        String zip = "";
        Random rand = new Random();
        for (int i = 0; i < 5; i++) {
            int a = rand.nextInt(9);
            zip += a;
        }
        return zip;
    }

    public void senateMembers(String state) {
        final TextView senator1 = (TextView) findViewById(R.id.senator1);
        final TextView senator2 = (TextView) findViewById(R.id.senator2);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.propublica.org/congress/v1/members/senate/" + state + "/current.json";
        StringRequest postRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject myObject = new JSONObject(response);
                            JSONArray senators = myObject.getJSONArray("results");
                            senator1.setText(senators.getJSONObject(0).get("name").toString());
                            senator2.setText(senators.getJSONObject(1).get("name").toString());
                            LinearLayout l1 = (LinearLayout) findViewById(R.id.sen1layout);
                            LinearLayout l2 = (LinearLayout) findViewById(R.id.sen2layout);
                            if (senators.getJSONObject(0).get("party").equals("D")) {
                                l1.setBackgroundColor(Color.parseColor("#40000042"));
                            }
                            if (senators.getJSONObject(0).get("party").equals("R")) {
                                l1.setBackgroundColor(Color.parseColor("#40A8424C"));
                            }
                            if (senators.getJSONObject(1).get("party").equals("D")) {
                                l2.setBackgroundColor(Color.parseColor("#40000042"));
                            }
                            if (senators.getJSONObject(1).get("party").equals("R")) {
                                l2.setBackgroundColor(Color.parseColor("#40A8424C"));
                            }
                            TextView t1 = (TextView) findViewById(R.id.sen1_website);
                            TextView t2 = (TextView) findViewById(R.id.sen2_website);
                            memberAPICall(senators.getJSONObject(0).get("api_uri").toString(), t1);
                            memberAPICall(senators.getJSONObject(1).get("api_uri").toString(), t2);
                            String sID1 = senators.getJSONObject(0).get("id").toString();
                            String sID2 = senators.getJSONObject(1).get("id").toString();
                            member_codes.add(sID1);
                            member_codes.add(sID2);
                            ImageView i1 = (ImageView) findViewById(R.id.imageView);
                            ImageView i2 = (ImageView) findViewById(R.id.imageView2);
                            setImage(i1, sID1);
                            setImage(i2, sID2);


                        } catch (JSONException e) {
                            senator1.setText("error");
                            senator2.setText("error");
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-api-key", "N8YFskSNyQ44mEYjXxc0hSv0wcwo5yjFQFpSXoyX");

                return params;
            }
        };
        queue.add(postRequest);
    }

    public void getDistrictMems() {
        //final TextView tv = (TextView) findViewById(R.id.house1);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.geocod.io/v1.3/geocode?postal_code=" + MainActivity.Zip_Entered + "&fields=cd&api_key=be9f34b3405ba44b59aabba24fb5fc0c30cbac9";
        StringRequest postRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject myObject = new JSONObject(response);
                            JSONArray house = myObject.getJSONArray("results").getJSONObject(0).getJSONObject("fields").getJSONArray("congressional_districts");
                            for (int i = 0; i < house.length(); i++) {
                                String num = house.getJSONObject(i).get("district_number").toString();
                                String text = "house" + (i+1);
                                int resID = getResources().getIdentifier(text, "id", getPackageName());
                                TextView tv = (TextView) findViewById(resID);
                                String layout = "houselayout" + (i+1);
                                int id2 = getResources().getIdentifier(layout, "id", getPackageName());
                                LinearLayout l1 = (LinearLayout) findViewById(id2);
                                String image = "houseimage" + (i+1);
                                int im = getResources().getIdentifier(image, "id", getPackageName());
                                ImageView imageView = (ImageView) findViewById(im);
                                String web = "house" + (i+1)+ "_website";
                                int w_id = getResources().getIdentifier(web, "id", getPackageName());
                                TextView website = (TextView) findViewById(w_id);
                                district_member(num, tv, l1, imageView, website);
                            }
                        } catch (JSONException e) {
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

    }

    public void district_member(String num, final TextView tv, final LinearLayout l1, final ImageView imageView, final TextView web) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.propublica.org/congress/v1/members/house/" + state_entered + "/" + num + "/current.json";
        StringRequest postRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject myObject = new JSONObject(response);
                            JSONArray house_mem = myObject.getJSONArray("results");
                            tv.setText(house_mem.getJSONObject(0).get("name").toString());
                            l1.setVisibility(View.VISIBLE);
                            if (house_mem.getJSONObject(0).get("party").toString().equals("D")) {
                                l1.setBackgroundColor(Color.parseColor("#40000042"));
                            }
                            if (house_mem.getJSONObject(0).get("party").toString().equals("R")) {
                                l1.setBackgroundColor(Color.parseColor("#40A8424C"));
                            }
                            memberAPICall(house_mem.getJSONObject(0).get("api_uri").toString(), web);
                            String sid = house_mem.getJSONObject(0).get("id").toString();
                            member_codes.add(sid);
                            setImage(imageView, sid);
                        } catch (JSONException e) {
                            Log.d("ERROR", "error => " + e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-api-key", "N8YFskSNyQ44mEYjXxc0hSv0wcwo5yjFQFpSXoyX");

                return params;
            }
        };
        queue.add(postRequest);
    }

    public void memberAPICall(String url, final TextView tv) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject myObject = new JSONObject(response);
                            tv.setText(myObject.getJSONArray("results").getJSONObject(0).get("url").toString());
                            tv.setMovementMethod(LinkMovementMethod.getInstance());
                            contact_urls.add(myObject.getJSONArray("results").getJSONObject(0).getJSONArray("roles").getJSONObject(0).get("contact_form").toString());
                        } catch (JSONException e) {
                            tv.setText("issue in individual API Call");
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-api-key", "N8YFskSNyQ44mEYjXxc0hSv0wcwo5yjFQFpSXoyX");

                return params;
            }
        };
        queue.add(postRequest);
    }

    public void setImage(ImageView i, String id) {
        String url = "http://bioguide.congress.gov/bioguide/photo/" + id.charAt(0) + "/" + id + ".jpg";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src_name");
            i.setImageDrawable(d);
        } catch (Exception e) {
            Drawable myDrawable = getResources().getDrawable(R.drawable.shield);
            i.setImageDrawable(myDrawable);
        }
    }

    private class DownloadFilesTask extends AsyncTask<URL, Integer, String> {
        protected String doInBackground(URL... urls) {
            getDistrictMems();
            return "Done";
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            //showDialog("Downloaded " + result + " bytes");
        }
    }
}
