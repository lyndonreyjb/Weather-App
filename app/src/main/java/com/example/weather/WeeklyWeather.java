package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeeklyWeather extends AppCompatActivity {

    TextView Date1, Date2, Date3, Date4, Date5, Condition1, Condition2, Condition3, Condition4, Condition5, Temp1, Temp2, Temp3, Temp4, Temp5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_weather);


        Date1 = findViewById(R.id.Date1);
        Date2 = findViewById(R.id.Date2);
        Date3 = findViewById(R.id.Date3);
        Date4 = findViewById(R.id.Date4);
        Date5 = findViewById(R.id.Date5);


        Temp1 = findViewById(R.id.Temp1);
        Temp2 = findViewById(R.id.Temp2);
        Temp3 = findViewById(R.id.Temp3);
        Temp4 = findViewById(R.id.Temp4);
        Temp5 = findViewById(R.id.Temp5);

        TextView[] conditions =
                        {Condition1 = findViewById(R.id.Condition1),
                        Condition2 = findViewById(R.id.Condition2),
                        Condition3 = findViewById(R.id.Condition3),
                        Condition4 = findViewById(R.id.Condition4),
                        Condition5 = findViewById(R.id.Condition5)};


        Intent intent = getIntent();
        String url = intent.getStringExtra(Intent.EXTRA_TEXT);
        weeklyWeather(url,conditions);

    }

    private void weeklyWeather(String url, TextView[] conditions) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                try{
                    for(int i = 0; i < 5; i++) {

                        JSONObject jsonRes = new JSONObject(response);
                        JSONArray jsonArray = jsonRes.getJSONArray("daily");
                        JSONObject jsonDaily = jsonArray.getJSONObject(i);
                        JSONArray jsonWeatherArray = jsonDaily.getJSONArray("weather");
                        JSONObject jsonWeather = jsonWeatherArray.getJSONObject(0);
                        String condition = jsonWeather.getString("description");
                        //JSONArray jsonTempArray = jsonDaily.getJSONArray("temp");
                        //JSONObject jsonTemp = jsonTempArray.getJSONObject(0);
                        //double temp = jsonTemp.getDouble("day") - 273.15;
                        //tempt = "\n " + df.format(temp) + " °C ";
                        conditions[i].setText(condition);
                        //conditionDisplay.setText(condition);
                        //tempDisplayName.setText(temp);
                    }

                }catch(JSONException exception){
                  exception.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    public void backButton(View view) {
        Intent intent = new Intent(WeeklyWeather.this, MainActivity.class);
        startActivity(intent);
    }

}