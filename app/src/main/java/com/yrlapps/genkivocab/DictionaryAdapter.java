package com.yrlapps.genkivocab;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.DictionaryViewHolder> implements Filterable {


    public List<Word> words =new ArrayList<>();
    public RequestQueue requestQueue;
    public List<Word> filtered = new ArrayList<>();

    public static class DictionaryViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout containerView;
        public TextView textView;

        public DictionaryViewHolder(@NonNull View view) {
            super(view);

        containerView = view.findViewById(R.id.dictionary_row);
        textView = view.findViewById(R.id.dictionary_row_textView);


        containerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Word current = (Word)containerView.getTag();
                Intent intent =new Intent(v.getContext(), WordActivity.class);
                intent.putExtra("position", current.getPosition());

                v.getContext().startActivity(intent);

            }
        });
        }
    }
    @Override
    public Filter getFilter() {
        return new WordFilter();
    }

    DictionaryAdapter(Context context){
        requestQueue = Volley.newRequestQueue(context);
        loadWords();
    }

    public void loadWords() {
        String url = "https://raw.githubusercontent.com/drjonesy/learngenki/master/database/genki_dictionary.json";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("words");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject result = results.getJSONObject(i);
//                        String name = result.getString("name");
                        String kana = result.getString("kana");
                        String kanji = result.getString("kanji");
                        String romaji = result.getString("romaji");
                        String english = result.getString("english");

                        words.add(new Word(kana, kanji, romaji, english, i));

                    }
                    notifyDataSetChanged(); // Notifies that the data has changed
                } catch (JSONException e) {
                    Log.e("yrl", "Json error",e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("yrl","word list error");
            }
        });
        requestQueue.add(request);
    }

    @NonNull
    @Override
    public DictionaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dictionary_row,parent,false);
        return new DictionaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DictionaryViewHolder holder, int position) {

        Word current = filtered.get(position);
        holder.textView.setText(current.getEnglish() + ": " + current.getKana());
        holder.containerView.setTag(current);

    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }


    private class WordFilter extends Filter {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
           List<Word> filteredWords= new ArrayList<>();
            FilterResults results =new FilterResults();
            if (constraint.length() == 0){
                results.values = words;
                results.count = words.size();
            }
            else{
                for(Word word: words) {
                    if (isKanji(constraint.toString())) {
                        //Search in kanji fields
                        if (word.getKanji().contains(constraint.toString())) {
                            filteredWords.add(word);
                        }

                    } else if (isKana(constraint.toString())) {
                        //Search in kana fields
                        if (word.getKana().contains(constraint.toString())) {
                            filteredWords.add(word);
                        }
                    }

                    else{
                        //Search in english fields
                        if (word.getEnglish().contains(constraint.toString())){
                            filteredWords.add(word);
                        }
                    }
                }
            }
            results.values =filteredWords;
            results.count = filteredWords.size();
            return results;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        private boolean isKana(String str) {
            for(int i =0; i<str.length();){
                int codepoint = str.codePointAt(i);
                i+= Character.charCount(codepoint);
                if (Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HIRAGANA||
                        Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.KATAKANA) {
                    return true;
                }
            }
            return false;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        private boolean isKanji(String str){
            for(int i =0; i<str.length();){
                int codepoint = str.codePointAt(i);
                i+= Character.charCount(codepoint);
                if (Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN) {
                    return true;
                }
            }
            return false;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
                filtered = (List<Word>) results.values;
                notifyDataSetChanged();
        }
    }
}
