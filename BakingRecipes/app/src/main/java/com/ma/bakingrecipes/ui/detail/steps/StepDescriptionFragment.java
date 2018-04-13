package com.ma.bakingrecipes.ui.detail.steps;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.ma.bakingrecipes.R;
import com.ma.bakingrecipes.model.Recipe;
import com.ma.bakingrecipes.model.Step;
import com.ma.bakingrecipes.ui.detail.SharedViewModel;
import com.ma.bakingrecipes.ui.detail.SharedViewModelFactory;
import com.ma.bakingrecipes.ui.detail.ingredients.IngredientFragment;
import com.ma.bakingrecipes.ui.detail.ingredients.IngredientItemRecyclerViewAdapter;
import com.ma.bakingrecipes.utilities.InjectorUtils;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepDescriptionFragment extends Fragment {

    private final String TAG = StepDescriptionFragment.class.getName();
    private final String KEY_RECIPE_NAME = "recipe_name";
    private final String KEY_DESCRIPTION_NUMBER = "description_number";

    private SharedViewModel mViewModel;
    private String recipeName;
    private int descriptionNumber;
    private SimpleExoPlayer exoPlayer;

    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView playerView;

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

        if(savedInstanceState != null){
            recipeName = savedInstanceState.getString(KEY_RECIPE_NAME);
            descriptionNumber = savedInstanceState.getInt(KEY_DESCRIPTION_NUMBER);
        }else{
            if(getArguments() != null && getArguments().containsKey(KEY_RECIPE_NAME)
                    && getArguments().containsKey(KEY_DESCRIPTION_NUMBER)){
                recipeName = getArguments().getString(KEY_RECIPE_NAME);
                descriptionNumber = getArguments().getInt(KEY_DESCRIPTION_NUMBER);
            }
        }

        Log.v(TAG, "recipe name: " + recipeName);

        SharedViewModelFactory factory =
                InjectorUtils.provideDetailViewModelFactory(getContext(), recipeName);
        mViewModel = ViewModelProviders.of(getActivity(), factory)
                .get(SharedViewModel.class);

        mViewModel.getRecipe().observe(this, value -> {
            if (value != null){
                Step step = value.getSteps().get(descriptionNumber);
                if(!step.getVideoURL().isEmpty())
                    initializePlayer(Uri.parse(step.getVideoURL()));
                else if(!step.getThumbnailURL().isEmpty()){
                    initializePlayer(Uri.parse(step.getThumbnailURL()));
                }else{
                    playerView.setVisibility(View.GONE);
                }

            }

        });

        return rootView;
    }

    private void initializePlayer(Uri uri) {
        if(exoPlayer == null){
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            playerView.setPlayer(exoPlayer);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Save the fragment's state here
        outState.putString(KEY_RECIPE_NAME, recipeName);
        outState.putInt(KEY_DESCRIPTION_NUMBER, descriptionNumber);
        super.onSaveInstanceState(outState);

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
    }

    private void releasePlayer() {
        if(exoPlayer != null){
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }

    }
}
