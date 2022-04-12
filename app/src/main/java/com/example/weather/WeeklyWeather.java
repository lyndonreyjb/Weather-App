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

    VideoView videoView;
    TextView Date1, Date2, Date3, Date4, Date5, Condition1, Condition2, Condition3, Condition4, Condition5, Temp1, Temp2, Temp3, Temp4, Temp5;
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
        //-------------

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


        Intent intent = getIntent();
        String url = intent.getStringExtra(Intent.EXTRA_TEXT);
        weeklyWeather(url);

    }

    private void weeklyWeather(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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

    public void backButton(View view) {
    }
}