// Subclass of RecyclerView.Adapter for binding data to RecyclerView items

package com.example.dgyou.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class NewsListActivity extends AppCompatActivity {

    private HashMap<String, URL> articles; // hashmap containing article titles and urls
    private NewsListAdapter adapter; // for binding data to RecyclerView
    private List<String> titles; // array for holding news article titles


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        try {
            Bundle extras = getIntent().getExtras();
            articles = (HashMap<String, URL>) extras.getSerializable("articleMap");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // populate titles List with key values from the articles HashMap
        titles = new ArrayList<>(articles.keySet());
        Collections.sort(titles, String.CASE_INSENSITIVE_ORDER);

        // get references to the RecyclerView to configure it
        RecyclerView recyclerView =
                (RecyclerView) findViewById(R.id.recyclerView);

        // use a LinearLayoutManager to display items in a vertical list
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create RecyclerView.Adapter to bind tags to the RecyclerView
        adapter = new NewsListAdapter(titles, articles, itemClickListener);
        recyclerView.setAdapter(adapter);

        // specify a custom ItemDecorator to draw lines between list items
        recyclerView.addItemDecoration(new ItemDivider(this));

    }

    // itemClickListener launches web browser to display search results
    private final View.OnClickListener itemClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get url for the article
                    String title = ((TextView) v).getText().toString();
                    URL articleUrl = articles.get(title);

                    String urlString = articleUrl.toString();

                    // create an Intent to launch a web browser
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(urlString));

                    startActivity(webIntent); // show results in web browser
                }
            };
}
