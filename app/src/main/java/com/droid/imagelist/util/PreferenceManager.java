package com.droid.imagelist.util;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sudendra on 24/1/15.
 */
@Singleton
public final class PreferenceManager {

    private static final String DATA_IS_SYNCED = "data_synced_state";
    private SharedPreferences mSharedPreferences;
    private Context mContext;

    @Inject
    public PreferenceManager(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences("test-pref", Context.MODE_PRIVATE);
    }

    private SharedPreferences getPref() {
        return mSharedPreferences;
    }

    public boolean isDataSynced() {
        return mSharedPreferences.getBoolean(DATA_IS_SYNCED, false);
    }

    public void setDataSynced() {
        mSharedPreferences.edit().putBoolean(DATA_IS_SYNCED, true).apply();
    }

}