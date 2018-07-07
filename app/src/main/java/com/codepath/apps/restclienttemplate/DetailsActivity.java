package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

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
    ImageView ivEmbedded;
    ImageView ivCompose;
    private int REQUEST_CODE = 10;

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
        ivEmbedded = (ImageView) findViewById(R.id.ivEmbedded);
        ivCompose = (ImageView) findViewById(R.id.ivCompose);

        // Extract tweet from intent extras
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        tvBody.setText(tweet.getBody());
        tvUserName.setText(tweet.getUser().getName());
        tvRelativeDate.setText(tweet.getRelativeDate());
        tvScreenName.setText(tweet.getUser().getScreenName());

        // Load profile image
        Glide.with(this)
                .load(tweet.getUser().getProfileImageUrl())
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(25, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(ivProfileImage);
        if (tweet.getEmbeddedUrl() != null) {
            Glide.with(this)
                    .load(tweet.getEmbeddedUrl())
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(25, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .into(ivEmbedded);
        }

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

        ivCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, ComposeActivity.class);
                intent.putExtra("replying_to", tweet.getUser().getScreenName());
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check request code and result code
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // use data parameter
            Tweet tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
            TimelineActivity.tweets.add(0, tweet);
            TimelineActivity.tweetAdapter.notifyItemInserted(0);
            // Toast success message to display temporarily on screen
            Toast.makeText(this, "Tweet posted", Toast.LENGTH_SHORT).show();
        }
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
