package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {

    Tweet tweet;
    TwitterClient client;
    TwitterHelper helper;
    ImageView ivProfileImage;
    TextView tvBody;
    TextView tvUserName;
    TextView tvRelativeDate;
    TextView tvScreenName;
    ImageView ivFavorite;
    ImageView ivRetweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        client = new TwitterClient(this);
        helper = new TwitterHelper();

        // Fetch views
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvRelativeDate = (TextView) findViewById(R.id.tvRelativeDate);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        ivFavorite = (ImageView) findViewById(R.id.ivFavorite);
        ivRetweet = (ImageView) findViewById(R.id.ivRetweet);

        // Extract tweet from intent extras
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        // Use tweet object to populate views with content
//        String imageUrl = tweet.getUser().getProfileImageUrl();
        tvBody.setText(tweet.getBody());
        tvUserName.setText(tweet.getUser().getName());
        tvRelativeDate.setText(tweet.getRelativeDate());
        tvScreenName.setText(tweet.getUser().getScreenName());

        // Load profile image
        Glide.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);

        // Load favorite icon
        if (tweet.isFavorited()) {
            helper.favoriteImage(ivFavorite);
        } else {
            helper.unfavoriteImage(ivFavorite);
        }

        // Load retweet icon
        if (tweet.isRetweeted()) {
            helper.retweetOn(ivRetweet);
        } else {
            helper.retweetOff(ivRetweet);
        }

        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.favoriteItem(tweet, client, ivFavorite);
            }
        });

        ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.retweetItem(tweet, client, ivRetweet);
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
////        // Locate MenuItem with ShareActionProvider
////        MenuItem item = menu.findItem(R.id.menu_item_share);
////        // Fetch reference to the share action provider
////        miShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
////        attachShareIntentAction(); // call here in case this method fires second
////        // Return true to display menu
//
//        return true;
//    }
}
