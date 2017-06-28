package com.dreammesh.app.bakingapp.ui.fragment;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreammesh.app.bakingapp.R;
import com.dreammesh.app.bakingapp.data.model.Step;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.dreammesh.app.bakingapp.util.CommonUtil.STEP_FRAGMENT_KEY;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeStepFragment extends Fragment implements
        ExoPlayer.EventListener {

    // Final Strings to store state information about the list of steps and list index
    public static final String STEP_ID_LIST = "stepList";
    public static final String LIST_INDEX = "list_index";

    @BindView(R.id.recipe_step_desc_card)
    CardView descriptionCard;

    @BindView(R.id.recipe_step_image)
    ImageView recipeStepImage;

    @BindView(R.id.recipe_step_desc)
    TextView recipeStepDesc;

    @BindView(R.id.recipe_step_video)
    SimpleExoPlayerView exoPlayerView;

    @BindView(R.id.next_step_btn)
    Button nextBtn;

    @BindView(R.id.previous_step_btn)
    Button previousBtn;

    @BindBool(R.bool.is_landscape)
    boolean isLandscape;

    @BindBool(R.bool.is_tablet)
    boolean isTablet;

    SimpleExoPlayer exoPlayer;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;

    private List<Step> stepList;
    private int currentStep;

    public RecipeStepFragment() {}

    public static RecipeStepFragment newInstance() {
        return new RecipeStepFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            stepList = savedInstanceState.getParcelableArrayList(STEP_ID_LIST);
            currentStep = savedInstanceState.getInt(LIST_INDEX);
        }

        View view = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Step step = stepList.get(currentStep);
        setUpViews(step);
    }

    private void setUpViews(Step step) {
        recipeStepDesc.setText(step.getDescription());

        String imageUrl = step.getThumbnailURL();
        setUpImageView(imageUrl);

        String videoUrl = step.getVideoURL();
        setUpVideoView(videoUrl);

        if(isTablet) {
            setViewVisibility(nextBtn, false);
            setViewVisibility(previousBtn, false);
        }
    }

    private void setUpVideoView(String videoUrl) {
        if (videoUrl != null && !videoUrl.isEmpty()) {

            // Init and show video view
            setViewVisibility(exoPlayerView, true);
            initializeMediaSession();
            initializePlayer(Uri.parse(videoUrl));

            if (isLandscape && !isTablet) {
                // Expand video, hide description, hide system UI
                expandVideoView(exoPlayerView);
                setViewVisibility(descriptionCard, false);
                hideSystemUI();
            }

        } else {
            // Hide video view
            setViewVisibility(exoPlayerView, false);
        }
    }

    private void setUpImageView(String imageUrl) {
        if(imageUrl.isEmpty()) {
            Picasso.with(getActivity())
                    .load(R.drawable.recipe_placeholder)
                    .into(recipeStepImage);
        } else {
            Picasso.with(getActivity())
                    .load(imageUrl)
                    .error(R.drawable.recipe_placeholder)
                    .into(recipeStepImage);
        }
    }

    private void initializeMediaSession() {

        mediaSession = new MediaSessionCompat(getContext(), "RecipeStepSinglePageFragment");

        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mediaSession.setMediaButtonReceiver(null);
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                exoPlayer.setPlayWhenReady(true);
            }

            @Override
            public void onPause() {
                exoPlayer.setPlayWhenReady(false);
            }

            @Override
            public void onSkipToPrevious() {
                exoPlayer.seekTo(0);
            }
        });
        mediaSession.setActive(true);
    }

    private void initializePlayer(Uri mediaUri) {
        if (exoPlayer == null) {

            TrackSelector trackSelector = new DefaultTrackSelector();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(getContext(), "StepVideo");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    // Src: https://developer.android.com/training/system-ui/immersive.html
    private void hideSystemUI() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar != null) {
            actionBar.hide();
        }
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void expandVideoView(SimpleExoPlayerView exoPlayer) {
        exoPlayer.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        exoPlayer.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
    }

    private void setViewVisibility(View view, boolean show) {
        if (show) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }

        if (mediaSession != null) {
            mediaSession.setActive(false);
        }
    }

    @OnClick(R.id.next_step_btn)
    void doNextStep() {
        if (currentStep == stepList.size() - 1) {
            if (nextBtn.isEnabled()) nextBtn.setEnabled(false);
        } else {
            currentStep++;

            if(!previousBtn.isEnabled()) previousBtn.setEnabled(true);

            Step step = stepList.get(currentStep);
            releasePlayer();
            setUpViews(step);
        }
    }

    @OnClick(R.id.previous_step_btn)
    void doPreviousStep() {
        if (currentStep == 0) {
            if(previousBtn.isEnabled()) previousBtn.setEnabled(false);
        } else {
            currentStep--;

            if (!nextBtn.isEnabled()) nextBtn.setEnabled(true);

            Step step = stepList.get(currentStep);
            releasePlayer();
            setUpViews(step);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelableArrayList(STEP_ID_LIST, (ArrayList<Step>) stepList);
        currentState.putInt(LIST_INDEX, currentStep);
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
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, exoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, exoPlayer.getCurrentPosition(), 1f);
        }
        mediaSession.setPlaybackState(stateBuilder.build());
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

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

    public void setStepList(List<Step> stepList) {
        this.stepList = stepList;
    }
}
