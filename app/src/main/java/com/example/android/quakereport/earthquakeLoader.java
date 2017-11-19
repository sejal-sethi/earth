package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by sejal on 02-11-2017.
 */

public class earthquakeLoader extends AsyncTaskLoader<List<earthquake>> {
    String murl;
   private static final String LOG_TAG = earthquakeLoader.class.getName();

    public earthquakeLoader(Context context, String url){
        super(context);
        murl=url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.i(LOG_TAG+"","start loading");
    }

    @Override
    public List<earthquake> loadInBackground() {

        if (murl == null) {
            return null;
        }
        Log.i(LOG_TAG+"","load in background");
        List<earthquake> result = QueryUtils.fetchData(murl);
        return result;
    }
}
