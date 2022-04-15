package com.example.memeshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ImageView memeimageview;
    Button sharebutton,nextbutton;
    ProgressBar progressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.purple_500)));
        memeimageview = findViewById(R.id.memeimageView);
        sharebutton = findViewById(R.id.sharebutton);
        nextbutton = findViewById(R.id.nextbutton);
        progressbar = findViewById(R.id.progressBar);
    }

    public void loadMeme(){
        progressbar.setVisibility(ProgressBar.VISIBLE);
        String url ="https://meme-api.herokuapp.com/gimme";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String url = response.getString("url");
                    Glide.with(MainActivity.this).load(url).into(memeimageview);
                Glide.with(MainActivity.this).load(url).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressbar.setVisibility(View.INVISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressbar.setVisibility(View.INVISIBLE);
                        return false;
                    }
                }).into(memeimageview);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Error Occurred",Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
    public void shareMeme(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"This is my app");
        startActivity(intent);

    }

    public void nextMeme(View view) {
        loadMeme();
    }

}