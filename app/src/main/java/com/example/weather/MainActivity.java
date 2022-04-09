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

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    TextView result;
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


        result = findViewById(R.id.result);
        Connect();
    }

    private void Connect() {
        String city = "Edmonton";
        String country = "Canada";
        String tempUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "," +country +"&APPID=" + api;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response",response);
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