// Subclass of RecyclerView.Adapter for binding data to RecyclerView items
package com.example.dgyou.newsapp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dgyou on 2023-10-11.
 */
public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    // listeners from NewsListActivity that are registered for each list item
    private final View.OnClickListener clickListener;

    // map of articles
    public static HashMap<String, URL> articles;
    private final List<String> titles;

    // constructor
    public NewsListAdapter(List<String> titles, HashMap<String, URL> articles, View.OnClickListener clickListener) {
        this.articles = articles;
        this.titles = titles;
        this.clickListener = clickListener;
    }

    // nested subclass of RecyclerView.ViewHolder used to implement
    // the view-holder pattern in the context of a RecyclerView--the logic
    // of recycling views that have scrolled offscreen is handled for you
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView textView;

        // configures a RecyclerView item's ViewHolder
        public ViewHolder(View itemView, View.OnClickListener clickListener) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);

            // attach listeners to itemView
            itemView.setOnClickListener(clickListener);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the list_item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item, parent, false);

        // create a ViewHolder for current item
        return (new ViewHolder(view, clickListener));
    }

    // sets the text of the list item to display the new article title
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.textView.setText(titles.get(position));
    }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }
}
