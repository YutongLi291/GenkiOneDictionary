package com.yrlapps.genkivocab;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

class WordActivity extends AppCompatActivity {

    private TextView kanaTextView;
    private TextView kanjiTextView;
    private TextView romajiTextView;
    private TextView englishTextView;
    private TextView wordTypeTextView;
    private int position;

    @Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        kanaTextView = findViewById(R.id.kana_view);
        kanjiTextView = findViewById(R.id.kanji_view);
        romajiTextView = findViewById(R.id.romaji_view);
        englishTextView = findViewById(R.id.english_view);
        wordTypeTextView = findViewById(R.id.wordtype_view);

    }

}
