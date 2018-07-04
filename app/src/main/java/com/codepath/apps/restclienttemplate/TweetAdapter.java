package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    Context context;

    // pass in Tweets array in constructor
    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }

    // for each row, inflate the layout and cache references into ViewHolder
    // only called when creating new rows
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }


    // bind value of Tweet object based on position of element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get data according to position
        Tweet tweet = mTweets.get(position);

        // populate views according to the data
        holder.tvUserName.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvRelativeDate.setText(tweet.relativeDate);

        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // create ViewHolder class

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView tvRelativeDate;

        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvRelativeDate = (TextView) itemView.findViewById(R.id.tvRelativeDate);
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }
}
