package com.droid.imagelist.module;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.droid.imagelist.fragment.ExamplesListFragment;
import com.droid.imagelist.fragment.NetworkImageFragment;
import com.droid.imagelist.util.LruBitmapCache;
import com.droid.imagelist.util.OkHttpStack;
import com.droid.imagelist.util.PreferenceManager;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = { NetworkImageFragment.class, ExamplesListFragment.class},
        library = true, complete = false)
public class RestModule {

    private Context context;

    public RestModule(Context appContext) {
        context = appContext;
    }

    @Provides
    @Named("App")
    public Context provideAppContext() {
        return context;
    }

    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient() {
        return new OkHttpClient();
    }

    @Singleton
    @Provides
    public OkUrlFactory provideOkUrlFactory(OkHttpClient okHttpClient) {
        return new OkUrlFactory(okHttpClient);
    }

    @Provides
    @Singleton
    public RequestQueue provideRequestQueue(OkUrlFactory okUrlFactory,
                                            @Named("App") Context context) {
        /** Set up to use OkHttp */
        return Volley.newRequestQueue(context, new OkHttpStack(okUrlFactory));
    }

    @Provides
    @Singleton
    public ImageLoader provideImageLoader(RequestQueue requestQueue) {
        final int imgSize = 1024;
        final int count = 8;
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / imgSize / count);
        return new ImageLoader(requestQueue, new LruBitmapCache(maxSize));
    }

    @Provides
    @Singleton
    public PreferenceManager providePreferenceManager() {
        return new PreferenceManager(context);
    }







}
