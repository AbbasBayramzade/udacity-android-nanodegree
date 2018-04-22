package com.ma.bakingrecipes.ui.detail.steps;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.ma.bakingrecipes.model.Recipe;
import com.ma.bakingrecipes.model.Step;
import com.ma.bakingrecipes.ui.detail.SharedViewModel;
import com.ma.bakingrecipes.ui.detail.SharedViewModelFactory;
import com.ma.bakingrecipes.ui.detail.ingredients.IngredientFragment;
import com.ma.bakingrecipes.utilities.InjectorUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepDescriptionFragment extends Fragment implements ExoPlayer.EventListener {

    private final String TAG = StepDescriptionFragment.class.getName();
    private final String KEY_RECIPE_NAME = "recipe_name";
    private final String KEY_EXO_POSITION = "exo_position";
    private final String KEY_DESCRIPTION_NUMBER = "description_number";
    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView playerView;
    @BindView(R.id.next_button)
    Button nextButton;
    @BindView(R.id.previous_button)
    Button previousButton;
    @BindView(R.id.step_description_textview)
    TextView stepDescriptionText;
    @BindView(R.id.step_image)
    ImageView stepImage;
    private SharedViewModel sharedViewModel;
    private String recipeName;
    private int descriptionNumber;
    private SimpleExoPlayer exoPlayer;
    private int numberOfAvailableSteps;
    private MediaSessionCompat mediaSessionCompat;
    private PlaybackStateCompat.Builder stateBuilder;
    private Context context;
    private long exoPlayerPosition = 0;

    public StepDescriptionFragment() {
        // Required empty public constructor
    }

    public void setContext(Context context){
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_description,
                container, false);

        ButterKnife.bind(this, rootView);

        if(savedInstanceState != null)
            exoPlayerPosition = savedInstanceState.getLong(KEY_EXO_POSITION);

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
        sharedViewModel = ViewModelProviders.of(getActivity(), factory)
                .get(SharedViewModel.class);

        sharedViewModel.getRecipe().observe(this, value -> {
            if (value != null) {
                numberOfAvailableSteps = value.getSteps().size();

                checkNextButtonVisibility();

                Step step = value.getSteps().get(descriptionNumber);
                // set description text
                stepDescriptionText.setText(step.getDescription());

                displayVideoOrImage(value);
            }
        });

        nextButton.setOnClickListener(view -> nextButtonClick());

        previousButton.setOnClickListener(view -> previousButtonClick());

        return rootView;
    }

    private void displayVideoOrImage(Recipe recipe) {

        Step step = recipe.getSteps().get(descriptionNumber);

        if (!step.getVideoURL().isEmpty()){
            // change visibility of an imageview
            initializePlayer(Uri.parse(step.getVideoURL()));

        } else if (!step.getThumbnailURL().isEmpty()) {
            initializePlayer(Uri.parse(step.getThumbnailURL()));
        } else {
            // change visibility of a playerview and an imageview
            // display recipe image
            playerView.setVisibility(View.GONE);
            stepImage.setVisibility(View.VISIBLE);

            if(recipe.getImage().isEmpty())
                stepImage.setImageResource(R.drawable.ic_recipe_placeholer_image);
            else
                Picasso.with(getContext())
                        .load(recipe.getImage())
                        .placeholder(R.drawable.ic_recipe_placeholer_image)
                        .error(R.drawable.ic_recipe_placeholer_image)
                        .into(stepImage);
        }
    }

    private void previousButtonClick() {
        if(descriptionNumber == 0) {
            // start ingredients fragment
            Bundle bundle = new Bundle();
            bundle.putString(KEY_RECIPE_NAME, recipeName);
            IngredientFragment fragment = new IngredientFragment();
            fragment.setArguments(bundle);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment);
            transaction.commit();

        } else {

            Bundle bundle = new Bundle();
            bundle.putString(KEY_RECIPE_NAME, recipeName);
            bundle.putInt(KEY_DESCRIPTION_NUMBER, descriptionNumber - 1);
            StepDescriptionFragment fragment = new StepDescriptionFragment();
            fragment.setArguments(bundle);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     * Source: https://github.com/udacity/AdvancedAndroid_ClassicalMusicQuiz
     */
    private void initializeMediaSession() {
        Log.v(TAG, "Initialize media session");
        // Create a MediaSessionCompat.
        mediaSessionCompat = new MediaSessionCompat(getActivity(), TAG);

        Log.v(TAG, "media session Initialized");

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

    private void checkNextButtonVisibility() {
        if(descriptionNumber + 1 == numberOfAvailableSteps)
            nextButton.setVisibility(View.GONE);
        else
            nextButton.setVisibility(View.VISIBLE);

    }

    private void nextButtonClick() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RECIPE_NAME, recipeName);
        bundle.putInt(KEY_DESCRIPTION_NUMBER, descriptionNumber + 1);
        StepDescriptionFragment fragment = new StepDescriptionFragment();
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
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
            exoPlayer.seekTo(exoPlayerPosition);
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
            exoPlayerPosition = exoPlayer.getCurrentPosition();
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_EXO_POSITION, exoPlayerPosition);
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
