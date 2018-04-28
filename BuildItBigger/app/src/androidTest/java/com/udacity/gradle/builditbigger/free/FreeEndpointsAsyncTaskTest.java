package com.udacity.gradle.builditbigger.free;

import android.test.AndroidTestCase;
import android.util.Log;

import com.udacity.gradle.builditbigger.EndpointsAsyncTask;

/**
 * Created by amatanat.
 */

public class FreeEndpointsAsyncTaskTest extends AndroidTestCase {

    private static final String TAG = "FreeEndpointsAsyncTaskTest";

    @SuppressWarnings("unchecked")
    public void test() {

        Log.v(TAG, "Run test");
        String result = null;
        EndpointsAsyncTask endpointsAsyncTask = new EndpointsAsyncTask(getContext());
        endpointsAsyncTask.execute();
        try {
            result = endpointsAsyncTask.get();
            Log.d(TAG, "Received string: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(result);
    }
}
