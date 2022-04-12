package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView resultTxt, tempTxt;
    private final String api = "0467e9991fae0e912bd4f9a5d899519c";
    DecimalFormat df = new DecimalFormat("#");
    VideoView VidView;
    ImageView iconImg;
    Button dateBtn, weeklyWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VidView = findViewById(R.id.VidView);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.back);
        VidView.setVideoURI(uri);
        VidView.start();
        VidView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer media) {
                media.setLooping(true);
            }
        });

        Calendar calendar = Calendar.getInstance();
        String date = DateFormat.getDateInstance().format(calendar.getTime());

        resultTxt = findViewById(R.id.result);
        tempTxt = findViewById(R.id.temp);
        iconImg = findViewById(R.id.iconImg);
        dateBtn = findViewById(R.id.dateBtn);
        weeklyWeather = findViewById(R.id.infoBtn);
        dateBtn.setText(date);
        Connect();
    }

    private void Connect() {
        String city = "Calgary";
        String tempUrl = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=" + api;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);

                String result = "";
                String tempT = "";


                try {

                    JSONObject jsonRes = new JSONObject(response);
                    JSONArray jsonArr = jsonRes.getJSONArray("weather");
                    JSONObject jsonWeather = jsonArr.getJSONObject(0);
                    String desc = jsonWeather.getString("description");
                    JSONObject jsonMain = jsonRes.getJSONObject("main");
                    JSONObject jsonCoor = jsonRes.getJSONObject("coord");
                    String icon = jsonWeather.getString("icon");

                    String lon = jsonCoor.getString("lon");
                    String lat = jsonCoor.getString("lat");
                    double temp = jsonMain.getDouble("temp") - 273.15;
                    double feelsT = jsonMain.getDouble("feels_like") - 273.15;
                    String cityName = jsonRes.getString("name");

                    tempT += "\n " + df.format(temp) + " °C ";
                    result +=  "\n City: " + cityName + "\n Description: " + desc + "\n Feels Like: " + df.format(feelsT) + " °C ";


                    weeklyWeather(lat,lon);

                    tempTxt.setText(tempT);
                    resultTxt.setText(result);

                    Picasso.get().load("https://openweathermap.org/img/wn/"+icon+"@2x.png").into(iconImg);


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

    @Override
    protected void onPostResume() {
        VidView.resume();
        super.onPostResume();
    }

    @Override
    protected void onRestart() {
        VidView.start();
        super.onRestart();
    }
    @Override
    protected void onDestroy() {
        VidView.stopPlayback();
        super.onDestroy();
    }

    public void WeatherWeekly(View view) {
        Intent intent = new Intent(MainActivity.this, WeeklyWeather.class);
        startActivity(intent);
    }
}