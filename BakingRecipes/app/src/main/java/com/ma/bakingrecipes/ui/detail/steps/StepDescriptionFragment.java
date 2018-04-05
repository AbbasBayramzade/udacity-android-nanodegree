package com.ma.bakingrecipes.ui.detail.steps;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ma.bakingrecipes.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepDescriptionFragment extends Fragment {

    @BindView(R.id.step_instruction)
    TextView stepInstructions;

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

        return rootView;
    }
}
