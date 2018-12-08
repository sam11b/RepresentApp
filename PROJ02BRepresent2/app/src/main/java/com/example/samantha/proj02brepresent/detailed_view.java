package com.example.samantha.proj02brepresent;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class detailed_view extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_view);
        Toolbar mTopToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTopToolbar.setTitle("represent.");
        ImageView iv = (ImageView) findViewById(R.id.imageView3);
        setImage(iv, search_page.senator_code);
        getInfo();

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

    public void getInfo() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.propublica.org/congress/v1/members/" + search_page.senator_code + ".json";
        StringRequest postRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject myObject = new JSONObject(response);
                            JSONArray house_mem = myObject.getJSONArray("results");
                            TextView tv = (TextView) findViewById(R.id.textView2);
                            String name = house_mem.getJSONObject(0).get("first_name").toString() + " " + house_mem.getJSONObject(0).get("last_name");
                            tv.setText(name);
                            TextView tv2 = (TextView) findViewById(R.id.textView3);
                            if (house_mem.getJSONObject(0).get("current_party").toString().equals("D")) {
                                String party = tv2.getText().toString() + "Democratic Party";
                                tv2.setText(party);
                            }
                            if (house_mem.getJSONObject(0).get("current_party").toString().equals("R")) {
                                String party = tv2.getText().toString() + "Republican Party";
                                tv2.setText(party);
                            }
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
}
