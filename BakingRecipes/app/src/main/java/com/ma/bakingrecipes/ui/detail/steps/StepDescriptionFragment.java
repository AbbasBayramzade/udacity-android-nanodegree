package com.ma.bakingrecipes.ui.detail.steps;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.ma.bakingrecipes.R;
import com.ma.bakingrecipes.model.Step;
import com.ma.bakingrecipes.ui.detail.SharedViewModel;
import com.ma.bakingrecipes.ui.detail.SharedViewModelFactory;
import com.ma.bakingrecipes.utilities.InjectorUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepDescriptionFragment extends Fragment implements ExoPlayer.EventListener {

    private final String TAG = StepDescriptionFragment.class.getName();
    private final String KEY_RECIPE_NAME = "recipe_name";
    private final String KEY_DESCRIPTION_NUMBER = "description_number";

    private SharedViewModel mViewModel;
    private String recipeName;
    private int descriptionNumber;
    private SimpleExoPlayer exoPlayer;
    private int numberOfAvailableSteps;
    private MediaSessionCompat mediaSessionCompat;
    private PlaybackStateCompat.Builder stateBuilder;

    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView playerView;

    @BindView(R.id.next_button)
    Button nextButton;

    public StepDescriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_description,
                container, false);

        ButterKnife.bind(this, rootView);

        if (getArguments() != null && getArguments().containsKey(KEY_RECIPE_NAME)
                && getArguments().containsKey(KEY_DESCRIPTION_NUMBER)) {
            recipeName = getArguments().getString(KEY_RECIPE_NAME);
            descriptionNumber = getArguments().getInt(KEY_DESCRIPTION_NUMBER);
        }

        Log.v(TAG, "recipe name: " + recipeName);
        Log.v(TAG, "step description num: " + descriptionNumber);

        // Initialize the Media Session.
        initializeMediaSession();

        SharedViewModelFactory factory =
                InjectorUtils.provideDetailViewModelFactory(getContext(), recipeName);
        mViewModel = ViewModelProviders.of(getActivity(), factory)
                .get(SharedViewModel.class);

        mViewModel.getRecipe().observe(this, value -> {
            if (value != null) {
                numberOfAvailableSteps = value.getSteps().size();

                checkButtonVisibility();

                Step step = value.getSteps().get(descriptionNumber);
                if (!step.getVideoURL().isEmpty())
                    initializePlayer(Uri.parse(step.getVideoURL()));
                else if (!step.getThumbnailURL().isEmpty()) {
                    initializePlayer(Uri.parse(step.getThumbnailURL()));
                } else {
                    playerView.setVisibility(View.GONE);
                }
            }
        });

        nextButton.setOnClickListener(view -> {
            nextButtonClick();
        });

        return rootView;
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     * Source: https://github.com/udacity/AdvancedAndroid_ClassicalMusicQuiz
     */
    private void initializeMediaSession() {
        // Create a MediaSessionCompat.
        mediaSessionCompat = new MediaSessionCompat(getActivity(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mediaSessionCompat.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mediaSessionCompat.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSessionCompat.setPlaybackState(stateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mediaSessionCompat.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mediaSessionCompat.setActive(true);
    }

    private void checkButtonVisibility() {
        if(descriptionNumber + 1 == numberOfAvailableSteps)
            nextButton.setVisibility(View.GONE);
        else
            nextButton.setVisibility(View.VISIBLE);

    }

    private void nextButtonClick() {
        Bundle bundle = new Bundle();
        bundle.putString("recipe_name", recipeName);
        bundle.putInt(KEY_DESCRIPTION_NUMBER, descriptionNumber + 1);
        StepDescriptionFragment fragment = new StepDescriptionFragment();
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.step_description_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initializePlayer(Uri uri) {
        if (exoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            playerView.setPlayer(exoPlayer);

            exoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //release exoplayer
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        //release exoplayer
        releasePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //release exoplayer
        releasePlayer();
        mediaSessionCompat.setActive(false);
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }

    }


    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    exoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    exoPlayer.getCurrentPosition(), 1f);
        }
        mediaSessionCompat.setPlaybackState(stateBuilder.build());

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
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity() {
    }

    // source https://github.com/udacity/AdvancedAndroid_ClassicalMusicQuiz
    private class MySessionCallback extends MediaSessionCompat.Callback {
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
    }
}
