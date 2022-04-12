package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

public class WeeklyWeather extends AppCompatActivity {

    VideoView videoView;
    TextView Date1, Date2, Date3, Date4, Date5, Condition1, Condition2, Condition3, Condition4, Condition5, Temp1, Temp2, Temp3, Temp4, Temp5;
    private final String api = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_weather);

        // Play background video
        videoView = findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.back);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer media) {
                media.setLooping(true);
            }
        });

        Date1 = findViewById(R.id.Date1);
        Date2 = findViewById(R.id.Date2);
        Date3 = findViewById(R.id.Date3);
        Date4 = findViewById(R.id.Date4);
        Date5 = findViewById(R.id.Date5);

        Condition1 = findViewById(R.id.Condition1);
        Condition2 = findViewById(R.id.Condition2);
        Condition3 = findViewById(R.id.Condition3);
        Condition4 = findViewById(R.id.Condition4);
        Condition5 = findViewById(R.id.Condition5);

        Temp1 = findViewById(R.id.Temp1);
        Temp2 = findViewById(R.id.Temp2);
        Temp3 = findViewById(R.id.Temp3);
        Temp4 = findViewById(R.id.Temp4);
        Temp5 = findViewById(R.id.Temp5);

        Connect();
    }

    private void Connect() {
        String city = "Calgary";
        String tempUrl = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=" + api;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);

                try {

                    JSONObject jsonRes = new JSONObject(response);
                    JSONArray jsonArr = jsonRes.getJSONArray("weather");
                    JSONObject jsonWeather = jsonArr.getJSONObject(0);
                    String desc = jsonWeather.getString("description");
                    JSONObject jsonMain = jsonRes.getJSONObject("main");
                    JSONObject jsonCoor = jsonRes.getJSONObject("coord");


                    String lon = jsonCoor.getString("lon");
                    String lat = jsonCoor.getString("lat");
                    double temp = jsonMain.getDouble("temp") - 273.15;
                    String cityName = jsonRes.getString("name");


                    //result +=  "\n Description: " + desc + "\n City: " + cityName;
                    //tempT += "\n " + df.format(temp) + " °C ";

                    weeklyWeather(lat,lon);

                    //tempTxt.setText(tempT);
                    //resultTxt.setText(result);

                }catch (JSONException exception) {
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

    private void weeklyWeather(String lat, String lon) {
        String tempUrl = "https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&exclude={current,alerts,minutely}"+"&appid=" + api;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);

                try {
                    JSONObject jsonRes = new JSONObject(response);
                    JSONArray jsonArray = jsonRes.getJSONArray("daily");
                    JSONObject jsonDaily = jsonArray.getJSONObject(0);

                    JSONObject jsonWeather = jsonRes.getJSONObject("weather");
                    String condition = jsonWeather.getString("description");

                    JSONObject jsonTemp = jsonRes.getJSONObject("temp");
                    String temp = jsonTemp.getString("day");

                    Condition1.setText(condition);
                    Temp1.setText(temp);


                }
                catch (JSONException e) {
                    e.printStackTrace();
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

    public void backButton(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



    // Play background video
    @Override
    protected void onPostResume() {
        videoView.resume();
        super.onPostResume();
    }

    @Override
    protected void onRestart() {
        videoView.start();
        super.onRestart();
    }
    @Override
    protected void onDestroy() {
        videoView.stopPlayback();
        super.onDestroy();
    }
}