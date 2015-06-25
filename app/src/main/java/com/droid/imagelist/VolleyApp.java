package com.droid.imagelist;

import com.activeandroid.ActiveAndroid;
import com.droid.imagelist.module.RestModule;

import java.util.Arrays;

import dagger.ObjectGraph;

public class VolleyApp extends com.activeandroid.app.Application {

    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        objectGraph = ObjectGraph.create(getModules());
        ActiveAndroid.initialize(this);
    }

    public void inject(Object target) {
        objectGraph.inject(target);
    }

    public Object[] getModules() {
        return Arrays.asList(new RestModule(this)).toArray();
    }
}
