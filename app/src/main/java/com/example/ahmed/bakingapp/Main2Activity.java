package com.example.ahmed.bakingapp;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main2Activity extends AppCompatActivity {

    @BindView(R.id.player_view)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.previous_btn)
    Button prevBtn;
    @BindView(R.id.next_btn)
    Button nextBtn;
    @BindView(R.id.desc_txt)
    TextView descTxt;


    SimpleExoPlayer mExoPlayer;

    String description, shortDescription, videoUrl, name;
    int stepId, length;
    long seekTo = 0;

    private ArrayList<AbstractModel> modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ButterKnife.bind(this);

        modelList = MainActivity.modelListAll;
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar


        Bundle bundle = getIntent().getBundleExtra("bundle");
        description = bundle.getString("description");
        shortDescription = bundle.getString("short");
        stepId = bundle.getInt("stepId");
        videoUrl = modelList.get(MainActivity.listId).getSteps().get(stepId).getVideoURL();
        name = bundle.getString("name");
        length = bundle.getInt("length");

        Log.d("modelList", "onCreate: modelList = " + modelList.get(MainActivity.listId).getSteps().get(stepId).getVideoURL());

        Log.d("video", "onCreate: video = " + videoUrl);
        setTitle(shortDescription + " - " + name);
        descTxt.setText(description);

        updateBtns();

        initializePlayer(Uri.parse(videoUrl));

    }

    private void updateBtns() {
        if (stepId == 0) {
            prevBtn.setVisibility(View.INVISIBLE);
            nextBtn.setVisibility(View.VISIBLE);
        } else if (stepId == length - 1) {
            nextBtn.setVisibility(View.INVISIBLE);
            prevBtn.setVisibility(View.VISIBLE);
        } else {
            nextBtn.setVisibility(View.VISIBLE);
            prevBtn.setVisibility(View.VISIBLE);
        }
        Log.d("buttons", "updateBtns: buttons updated");
    }

    @OnClick(R.id.previous_btn)
    public void returnToPrev(View view) {

        stepId--;

        updateBtns();

        descTxt.setText(modelList.get(MainActivity.listId).getSteps().get(stepId).getDescription());

        videoUrl = modelList.get(MainActivity.listId).getSteps().get(stepId).getVideoURL();
        releasePlayer();
        initializePlayer(Uri.parse(videoUrl));

    }

    @OnClick(R.id.next_btn)
    public void moveToNext(View view) {

        stepId++;

        updateBtns();

        descTxt.setText(modelList.get(MainActivity.listId).getSteps().get(stepId).getDescription());
        videoUrl = modelList.get(MainActivity.listId).getSteps().get(stepId).getVideoURL();
        releasePlayer();
        initializePlayer(Uri.parse(videoUrl));

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(Uri.parse(videoUrl));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            initializePlayer(Uri.parse(videoUrl));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mediaUri.toString().equals("")) {
            Toast.makeText(this, "No media found", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(this, "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.seekTo(seekTo);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            seekTo = mExoPlayer.getCurrentPosition();
            mExoPlayer.release();
            mExoPlayer = null;
            seekTo = 0;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mExoPlayer != null)
            seekTo = mExoPlayer.getCurrentPosition();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mPlayerView.getLayoutParams();

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;

            mPlayerView.setLayoutParams(params);
        } else {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
    }
}
// TODO: 4/12/2018 Espresso
// TODO: 4/12/2018 widget
