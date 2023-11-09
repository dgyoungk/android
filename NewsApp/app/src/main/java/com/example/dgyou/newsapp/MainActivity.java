package com.example.dgyou.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.support.design.widget.Snackbar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // variables to hold the value of the selected value in the spinners
    private String regionChoice;
    private String topicChoice;

    // variables for GUI references
    private Button regionButton;
    private Button topicButton;


    // HashMap to store the article titles/urls to display in the next Activity
    public static HashMap<String, URL> articleMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // references to GUI elements
        Spinner regionSpinner = (Spinner) findViewById(R.id.regionSpinner);
        regionSpinner.setOnItemSelectedListener(regionSelectedListener);

        Spinner topicSpinner = (Spinner) findViewById(R.id.topicSpinner);
        topicSpinner.setOnItemSelectedListener(topicSelectedListener);

        regionButton = (Button) findViewById(R.id.regionButton);
        topicButton = (Button) findViewById(R.id.topicButton);
        regionButton.setOnClickListener(newsListener);
        topicButton.setOnClickListener(newsListener);
    }

    // listeners for spinners to respond to different items being selected
    private final AdapterView.OnItemSelectedListener regionSelectedListener =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    regionChoice = parent.getSelectedItem().toString();
                    Log.d("Debugger", regionChoice);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) { }
            };

    private final AdapterView.OnItemSelectedListener topicSelectedListener =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    topicChoice = parent.getSelectedItem().toString();
                    Log.d("Debugger", topicChoice);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) { }
            };

    // listener for the two buttons that will display another activity showing all the articles
    private final View.OnClickListener newsListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    URL url = createURL(regionChoice, topicChoice);

                    if (url != null) {
                        GetNewsTask getNewsTask = new GetNewsTask();
                        getNewsTask.execute(url);
                    } else {
                        Snackbar.make(findViewById(R.id.mainLayout),
                                "Invalid URL", Snackbar.LENGTH_LONG).show();
                    }
                }
            };

    // create newsapi.org web service URL
    private URL createURL(String region, String topic) {
        URL apiUrl = null;

        // string resources for URL building
        String apiKey = getString(R.string.api_key);
        String baseUrl = getString(R.string.api_url);
        String apiKeyBuilder = getString(R.string.news_api_key);
        String regionUrl = getString(R.string.news_api_country);
        String topicUrl = getString(R.string.news_api_category);

        try {
            // create URL for specified city and imperial units (Fahrenheit)
            String urlString = baseUrl + regionUrl + URLEncoder.encode(region, "UTF-8") +
                    "&" + topicUrl + URLEncoder.encode(topic, "UTF-8") + "&" + apiKeyBuilder + apiKey;
            Log.d("Debugger", urlString);
            apiUrl = new URL(urlString);
            return apiUrl;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return apiUrl; // URL was malformed
    }

    // makes the REST web service call to get new articles data
    // and save it into a HashMap to pass on to the next Activity
    private class GetNewsTask
            extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            articleMap.clear();
        }

        @Override
        protected JSONObject doInBackground(URL... params) {
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) params[0].openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36");
                int response = connection.getResponseCode();
                Log.d("Debugger", Integer.toString(response));

                if (response == HttpURLConnection.HTTP_OK) {
                    StringBuilder builder = new StringBuilder();

                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()))) {

                        String line;

                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    }
                    catch (IOException e) {
                        Snackbar.make(findViewById(R.id.mainLayout),
                                R.string.read_error, Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                    return new JSONObject(builder.toString());
                }
                else {
                    Snackbar.make(findViewById(R.id.mainLayout),
                            R.string.connect_error, Snackbar.LENGTH_LONG).show();
                }
            }
            catch (Exception e) {
                Snackbar.make(findViewById(R.id.mainLayout),
                        R.string.connect_error, Snackbar.LENGTH_LONG).show();
                e.printStackTrace();
            }
            finally {
                connection.disconnect(); // close the HttpURLConnection
            }

            return null;
        }

        // process JSON response and update ListView
        @Override
        protected void onPostExecute(JSONObject news) {
            convertJSONtoArrayList(news);
            displayArticles();
        }
    }

    // create Weather objects from JSONObject containing the forecast
    private void convertJSONtoArrayList(JSONObject newsObject) {

        try {
            // get news's "articles" JSONArray
            JSONArray list = newsObject.getJSONArray("articles");

            // convert each element of list to a Weather object
            for (int i = 0; i < list.length(); i++) {
                JSONObject article = list.getJSONObject(i); // get one article's data

                // get article title
                String articleTitle = article.getString("title");

                // get the article URL
                String articleUrlString = article.getString("url");
                URL articleUrl = new URL(articleUrlString);

                articleMap.put(articleTitle, articleUrl);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // called when JSON processing is over
    // starts an Intent and launches the next Activity
    private void displayArticles() {

        Intent intent = new Intent(MainActivity.this, NewsListActivity.class);
        intent.putExtra("articleMap", articleMap);
        startActivity(intent);
    }
}
