package com.example.ahmed.bakingapp;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main2Activity extends AppCompatActivity implements ExoPlayer.EventListener {

    private static final String POSITION = "position";
    private static final String TAG = "seekToo";
    private static final String PLAY_WHEN_READY = "playWhenReady";

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
    int stepId, length, exoIndex;
    long seekTo;
    boolean playWhenReady = true;
    Bundle seekBundle;

    private ArrayList<AbstractModel> modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ButterKnife.bind(this);

        seekBundle = new Bundle();
        seekTo = seekBundle.getLong(POSITION, 0);
        playWhenReady = seekBundle.getBoolean(PLAY_WHEN_READY);

        if (savedInstanceState != null) {
            seekTo = savedInstanceState.getLong(POSITION);
            Log.d(TAG, "onCreate: " + seekTo);
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
        }

        modelList = MainActivity.modelListAll;
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar


        Bundle bundle = getIntent().getBundleExtra("bundle");
        description = bundle.getString("description");
        shortDescription = bundle.getString("short");
        stepId = bundle.getInt("stepId");
        if (modelList != null)
            videoUrl = modelList.get(MainActivity.listId).getSteps().get(stepId).getVideoURL();
        else
            videoUrl = bundle.getString("video");
        name = bundle.getString("name");
        length = bundle.getInt("length");

        Log.d("testing", "onCreate: desc = " + description + "\nshort = " + shortDescription +
                "\nstepId = " + stepId + "\nvideo = " + videoUrl + "\nname = " + name + "\nlength = " + length);

//        Log.d("modelList", "onCreate: modelList = " + modelList.get(MainActivity.listId).getSteps().get(stepId).getVideoURL());

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
    }

    @OnClick(R.id.previous_btn)
    public void returnToPrev(View view) {

        stepId--;

        updateBtns();

        descTxt.setText(modelList.get(MainActivity.listId).getSteps().get(stepId).getDescription());

        videoUrl = modelList.get(MainActivity.listId).getSteps().get(stepId).getVideoURL();

        shortDescription = modelList.get(MainActivity.listId).getSteps().get(stepId).getShortDescription();

        setTitle(shortDescription + " - " + name);
        releasePlayer();
        initializePlayer(Uri.parse(videoUrl));

    }

    @OnClick(R.id.next_btn)
    public void moveToNext(View view) {

        stepId++;

        updateBtns();

        if (modelList != null) {
            videoUrl = modelList.get(MainActivity.listId).getSteps().get(stepId).getVideoURL();
            description = modelList.get(MainActivity.listId).getSteps().get(stepId).getDescription();
        } else { // hardcoded for testing
            description = "Recipe Introduction";
            videoUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4";
        }
        descTxt.setText(description);

        shortDescription = modelList.get(MainActivity.listId).getSteps().get(stepId).getShortDescription();

        setTitle(shortDescription + " - " + name);

        releasePlayer();
        initializePlayer(Uri.parse(videoUrl));

    }

    @Override
    public void onStart() {
        super.onStart();

            initializePlayer(Uri.parse(videoUrl));
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.d(TAG, "onResume: " + seekTo);
//        hideSystemUi();
//        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
//            initializePlayer(Uri.parse(videoUrl));
//        }
//    }

//    @Override
//    public void onPause() {
//        super.onPause();
//
//            releasePlayer();
//
//    }

    @Override
    public void onStop() {
        super.onStop();

            releasePlayer();
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        if(findViewById(R.id.layout_land_scape) != null)
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
            Log.d(TAG, "initializePlayer: " + seekTo);
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(this, "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.seekTo(seekTo);
            mExoPlayer.prepare(mediaSource, false, false);
            mExoPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            seekTo = mExoPlayer.getCurrentPosition();
            playWhenReady = mExoPlayer.getPlayWhenReady();
            Log.d(TAG, "releasePlayer: " + seekTo);
            seekBundle.putLong(POSITION, mExoPlayer.getCurrentPosition());
            seekBundle.putBoolean(PLAY_WHEN_READY, mExoPlayer.getPlayWhenReady());
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mExoPlayer != null) {
            outState.putLong(POSITION, mExoPlayer.getCurrentPosition());
            Log.d(TAG, "onSaveInstanceState: " + seekTo);
            seekBundle.putLong(POSITION, mExoPlayer.getCurrentPosition());
            seekBundle.putBoolean(PLAY_WHEN_READY, mExoPlayer.getPlayWhenReady());
            outState.putBoolean(PLAY_WHEN_READY, mExoPlayer.getPlayWhenReady());
        }
    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        if (savedInstanceState != null) {
//            seekTo = savedInstanceState.getLong(POSITION);
//            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
//            Log.d(TAG, "onRestoreInstanceState: " + seekTo);
//        }
//    }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        seekBundle.putLong(POSITION, mExoPlayer.getCurrentPosition());
//        releasePlayer();
//    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            Log.d(TAG, "onPlayerStateChanged: PLAYING");
        } else if((playbackState == ExoPlayer.STATE_READY)){
            Log.d(TAG, "onPlayerStateChanged: PAUSED");
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }
// TODO: 10/06/18 orientation changes
}
