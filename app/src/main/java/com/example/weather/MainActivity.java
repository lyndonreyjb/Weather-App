package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    TextView resultTxt;
    private final String api = "0467e9991fae0e912bd4f9a5d899519c";
    DecimalFormat df = new DecimalFormat("#.##");
    VideoView VidView;

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


        resultTxt = findViewById(R.id.result);
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
                    result +=  "\n Temp: " + df.format(temp) + " °C" + "\n Description: " + desc + "\n City: " + cityName + " " + lat + " " + lon;

                    weeklyWeather(lat,lon);
                    resultTxt.setText(result);

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
        String tempUrl = "https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&exclude={alerts,minutely}"+"&appid=" + api;
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

}