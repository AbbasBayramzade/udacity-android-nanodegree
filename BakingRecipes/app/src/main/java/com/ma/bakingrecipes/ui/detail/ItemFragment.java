package com.ma.bakingrecipes.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ma.bakingrecipes.R;
import com.ma.bakingrecipes.model.Recipe;
import com.ma.bakingrecipes.utilities.InjectorUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemFragment extends Fragment {

    private final String TAG = ItemFragment.class.getName();

    // OnItemClickListener interface, calls a method in the host activity named onItemSelected
    public interface OnItemClickListener {
        void onItemSelected(int position);
    }

    // Define a new interface OnItemClickListener that triggers a callback in the host activity
    OnItemClickListener mCallback;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    // private List<Integer> instructionAndSteps = new ArrayList<>();

    private DetailActivityViewModel mViewModel;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // for testing purposes just passing the name
        DetailViewModelFactory factory =
                InjectorUtils.provideDetailViewModelFactory(getContext(), "Nutella Pie");

        mViewModel = ViewModelProviders.of(this, factory)
                .get(DetailActivityViewModel.class);

        mViewModel.getRecipe().observe(this, value -> {
            if (value != null){
                Log.d(TAG, value.getName());
                Log.d(TAG,value.getIngredients().get(0).getIngredient());
                bindData(value);
            }
        });

        return view;
    }

    private void bindData(Recipe value) {
        int count = value.getSteps().size() + 1;
        recyclerView.setAdapter(new MyItemRecyclerViewAdapter(count));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        mCallback.onItemSelected(position);
                        Log.d(TAG, "FRAGMENT OPEN ING onItemClick");
                    }

                    @Override public void onLongItemClick(View view, int position) {
                    }
                })
        );
    }


    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

}
