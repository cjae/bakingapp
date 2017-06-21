package com.dreammesh.app.bakingapp.ui.fragment;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreammesh.app.bakingapp.R;
import com.dreammesh.app.bakingapp.data.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.DefaultRenderersFactory;
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
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dreammesh.app.bakingapp.util.CommonUtil.STEP_FRAGMENT_KEY;

/**
 * A placeholder fragment containing a simple view.
 * Help from: https://gist.github.com/olushi/c26bb8a5f27f94d73b3a4888a509927c
 */
public class RecipeStepFragment extends Fragment implements
        ExoPlayer.EventListener {

    // constant fields for saving and restoring bundle
    public static final String AUTOPLAY = "autoplay";
    public static final String CURRENT_WINDOW_INDEX = "current_window_index";
    public static final String PLAYBACK_POSITION = "playback_position";
    public static final String VIDEO_URL = "video_url";

    @BindView(R.id.recipe_step_video)
    SimpleExoPlayerView exoPlayerView;

    private SimpleExoPlayer mPlayer;
    private MediaSessionCompat mediaSession;

    // autoplay = false
    private boolean autoPlay = false;

    // used to remember the playback position
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private String videoUrl;

    public RecipeStepFragment() {}

    public static RecipeStepFragment newInstance(Step step) {
        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(STEP_FRAGMENT_KEY, step);
        recipeStepFragment.setArguments(bundle);

        return recipeStepFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // if we have saved player state, restore it
        if (savedInstanceState != null) {
            videoUrl = savedInstanceState.getString(VIDEO_URL);
            playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION, 0);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW_INDEX, 0);
            autoPlay = savedInstanceState.getBoolean(AUTOPLAY, false);

            initViews(videoUrl);
        } else {
            Step step = getArguments().getParcelable(STEP_FRAGMENT_KEY);
            String video = null;
            if (step != null) {
                video = step.getVideoURL();
            }
            initViews(video);
        }
    }

    private void initViews(String videoUrl) {
        if(videoUrl != null && !videoUrl.isEmpty()) {
            setViewVisibility(exoPlayerView, true);

            initializeMediaSession();
            initializePlayer(Uri.parse(videoUrl));
        } else {
            setViewVisibility(exoPlayerView, false);
        }
    }

    private void initializePlayer(Uri parse) {

    }

    private void initializeMediaSession() {

    }

    private void setViewVisibility(View view, boolean b) {

    }

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

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }
}
