package com.ma.bakingrecipes.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import com.ma.bakingrecipes.AppExecutor;
import com.ma.bakingrecipes.data.RecipeRepository;
import com.ma.bakingrecipes.data.database.BakingDatabase;
import com.ma.bakingrecipes.data.database.RecipeDao;
import com.ma.bakingrecipes.model.Ingredient;
import com.ma.bakingrecipes.model.Recipe;
import com.ma.bakingrecipes.utilities.InjectorUtils;

import java.util.ArrayList;
import java.util.List;

public class WidgetService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new ViewFactory(this.getApplicationContext(),
                intent));
    }

}
