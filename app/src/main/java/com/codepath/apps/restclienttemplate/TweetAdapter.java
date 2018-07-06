package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.lang.ref.WeakReference;
import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    private final ClickListener listener;
    private final int REQUEST_CODE = 10;
    TwitterHelper helper = new TwitterHelper();
    TwitterClient client;
    Context context;

    // pass in Tweets array in constructor
    public TweetAdapter(List<Tweet> tweets, ClickListener listener) {
        mTweets = tweets;
        this.listener = listener;
    }

    // for each row, inflate the layout and cache references into ViewHolder
    // only called when creating new rows
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        client = new TwitterClient(context);
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView, listener);
        return viewHolder;
    }


    // bind value of Tweet object based on position of element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get data according to position
        Tweet tweet = mTweets.get(position);

        // populate views according to the data
        holder.tvUserName.setText(tweet.getUser().getName());
        holder.tvBody.setText(tweet.getBody());
        holder.tvRelativeDate.setText(tweet.getRelativeDate());
        holder.tvScreenName.setText(tweet.getUser().getScreenName());

        // Load favorite icon
        if (tweet.isFavorited()) {
            helper.favoriteImage(holder.ivFavorite);
        } else {
            helper.unfavoriteImage(holder.ivFavorite);
        }

        // Load retweet icon
        if (tweet.isRetweeted()) {
            helper.retweetOn(holder.ivRetweet);
        } else {
            helper.retweetOff(holder.ivRetweet);
        }

        Glide.with(context).load(tweet.getUser().getProfileImageUrl()).into(holder.ivProfileImage);
        if (tweet.getEmbeddedUrl() != null) {
            Glide.with(context).load(tweet.getEmbeddedUrl()).into(holder.ivEmbedded);
        }
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // create ViewHolder class

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView tvRelativeDate;
        public TextView tvScreenName;
        public ImageView ivEmbedded;
        public ImageView ivCompose;
        public ImageView ivFavorite;
        public ImageView ivRetweet;
        private WeakReference<ClickListener> listenerRef;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);

            listenerRef = new WeakReference<>(listener);

            // perform findViewById lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvRelativeDate = (TextView) itemView.findViewById(R.id.tvRelativeDate);
            tvScreenName = (TextView) itemView.findViewById(R.id.tvScreenName);
            ivEmbedded = (ImageView) itemView.findViewById(R.id.ivEmbedded);
            ivCompose = (ImageView) itemView.findViewById(R.id.ivCompose);
            ivFavorite = (ImageView) itemView.findViewById(R.id.ivFavorite);
            ivRetweet = (ImageView) itemView.findViewById(R.id.ivRetweet);


            // attach a click listener to the row
            itemView.setOnClickListener(this);
            ivCompose.setOnClickListener(this);
            ivFavorite.setOnClickListener(this);
            ivRetweet.setOnClickListener(this);
            // attach click listeners to the buttons
        }

        public void onClick(View view) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the tweet at the position, this won't work if the class is static
                Tweet tweet = mTweets.get(position);
                if (view.getId() == ivCompose.getId()) {
                    listenerRef.get().onComposeClicked(position, tweet.getUser().getScreenName());
                } else if (view.getId() == ivFavorite.getId()) {
                    helper.favoriteItem(tweet, client, ivFavorite);
                } else if (view.getId() == ivRetweet.getId()) {
                    helper.retweetItem(tweet, client, ivRetweet);
                } else {
                    // create intent for the new activity
                    Intent intent = new Intent(context, DetailsActivity.class);
                    // serialize the tweet using parceler, use its short name as a key
                    intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                    // show the activity
                    context.startActivity(intent);
                }
            }
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
