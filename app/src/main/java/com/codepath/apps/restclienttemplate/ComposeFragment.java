package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ComposeFragment extends DialogFragment implements TextView.OnEditorActionListener {
    private EditText etCompose;
    private Button btCompose;
    TwitterClient client;
    Context context;

    public ComposeFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public interface ComposeDialogListener {
        void onFinishComposeDialog(String inputText);
    }


    public static ComposeFragment newInstance(String title) {
        ComposeFragment frag = new ComposeFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.compose_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        client = new TwitterClient(context);
        // Get field from view
        etCompose = (EditText) view.findViewById(R.id.etCompose);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        etCompose.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        etCompose.setOnEditorActionListener(this);


//        btCompose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                client.sendTweet(etCompose.getText().toString(), new JsonHttpResponseHandler () {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                        try {
//                            Tweet tweet = Tweet.fromJSON(response);
//                            Intent i;
//                            if (getActivity().getClass().getSimpleName().equals(DetailsActivity.class.getName())) {
//                                i = new Intent(context, DetailsActivity.class);
//                            } else {
//                                i = new Intent(context, TimelineActivity.class);
//                            }
//                            i.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
//                            i.putExtra("message", etCompose.getText().toString());
//                            i.putExtra("code", 10);
////                            setResult(RESULT_OK, i);
//                            context.startActivity(i);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                        super.onFailure(statusCode, headers, throwable, errorResponse);
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//                        super.onFailure(statusCode, headers, throwable, errorResponse);
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                        super.onFailure(statusCode, headers, responseString, throwable);
//                    }
//                });
//            }
//        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text back to activity through the implemented listener
            ComposeDialogListener listener = (ComposeDialogListener) getActivity();
            listener.onFinishComposeDialog(etCompose.getText().toString());
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }
        return false;
    }


}
