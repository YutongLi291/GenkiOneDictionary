package com.yrlapps.genkivocab;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WordActivity extends AppCompatActivity {

    private TextView kanaTextView;
    private TextView kanjiTextView;
    private TextView romajiTextView;
    private TextView englishTextView;
    private TextView wordTypeTextView;
    private int position;
    private RequestQueue requestQueue;
    String url;
    @Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        kanaTextView = findViewById(R.id.kana_view);
        kanjiTextView = findViewById(R.id.kanji_view);
        romajiTextView = findViewById(R.id.romaji_view);
        englishTextView = findViewById(R.id.english_view);
        wordTypeTextView = findViewById(R.id.wordtype_view);
        url =DictionaryAdapter.url;
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        position =getIntent().getIntExtra("position",0);
        load();
    }

    public void load() {
        kanaTextView.setText("");
        kanjiTextView.setText("");
        romajiTextView.setText("");
        englishTextView.setText("");
        wordTypeTextView.setText("");

        JsonObjectRequest request =new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray words = response.getJSONArray("words");
                    JSONObject current = words.getJSONObject(position);
                    kanaTextView.setText(current.getString("kana"));
                    kanjiTextView.setText(current.getString("kanji"));
                    romajiTextView.setText(current.getString("romaji"));
                    englishTextView.setText(current.getString("english"));
                    wordTypeTextView.setText(current.getString("wordType"));
                } catch (JSONException e) {
                    Log.e("yrl", "words parsing problem!",e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("yrl", "words details error!");
            }
        });
        requestQueue.add(request);
    }

}
