package com.example.oaapp;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private TextView osebe;
    private String url = "http://10.0.2.2:5065/api/ObjavaIscemOaApi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        osebe = (TextView) findViewById(R.id.osebe);
    }

    public  void prikaziOsebe(View view){
        if (view != null){
            JsonArrayRequest request = new JsonArrayRequest(url, jsonArrayListener, errorListener);
            requestQueue.add(request);
        }
    }

    private Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response){
            ArrayList<String> data = new ArrayList<>();

            for (int i = 0; i < response.length(); i++){
                try {
                    JSONObject object =response.getJSONObject(i);
                    String name = object.getString("ime");
                    String surname = object.getString("priimek");
                    String description = object.getString("opis");
                    String dateStr = object.getString("datumObjave");
                     if (description.isEmpty()) {
                         description = "/";
                     }

                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                    try {
                        Date date = inputFormat.parse(dateStr);
                        if (date != null) {
                            String formattedDate = outputFormat.format(date);
                            data.add(name + " " + surname + "\n\n" + description + "\n\n" + formattedDate);
                        } else {
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                    return;

                }
            }

            osebe.setText("");


            for (String row: data){
                String currentText = osebe.getText().toString();
                osebe.setText(currentText + "\n\n" + "-------------------------------------------------------------------------" + "\n\n" + row);
            }

        }

    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("REST error", error.getMessage());
        }
    };


}