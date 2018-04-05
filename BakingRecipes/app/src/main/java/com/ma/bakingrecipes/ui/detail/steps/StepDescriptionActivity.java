package com.ma.bakingrecipes.ui.detail.steps;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ma.bakingrecipes.R;

public class StepDescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_description);

        if(savedInstanceState == null) {

            StepDescriptionFragment fragment = new StepDescriptionFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.step_instruction_container, fragment)
                    .commit();

        }
    }
}
