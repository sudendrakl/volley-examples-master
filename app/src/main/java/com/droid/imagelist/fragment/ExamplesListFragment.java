package com.droid.imagelist.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.droid.imagelist.R;
import com.droid.imagelist.VolleyApp;
import com.droid.imagelist.adapter.ImagesAdapter;
import com.droid.imagelist.interfaces.OnListItemClickListener;
import com.droid.imagelist.model.ImageData;
import com.droid.imagelist.util.AppConstants;
import com.droid.imagelist.util.Bus;
import com.droid.imagelist.util.GsonRequest;
import com.droid.imagelist.util.HttpResponseEvent;
import com.droid.imagelist.util.PreferenceManager;
import com.droid.imagelist.util.RestUtils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ExamplesListFragment extends Fragment implements OnListItemClickListener {
    public static final String TAG = ExamplesListFragment.class.getSimpleName();
    ImagesAdapter imagesAdapter;
    @Inject
    RequestQueue requestQueue;
    @Inject
    ImageLoader imageLoader;
    @Inject
    PreferenceManager preferenceManager;
    private List<ImageData> responseList = new ArrayList<>();
    private Dialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        getActivity().getActionBar().setTitle(R.string.app_name);

        RecyclerView recyclerView = new RecyclerView(getActivity());
        LinearLayoutManager llManager = new LinearLayoutManager(getActivity());
        llManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llManager);
        imagesAdapter = new ImagesAdapter(responseList, this, getActivity(), imageLoader);
        recyclerView.setAdapter(imagesAdapter);
        if (responseList.size() == 0) {
            startRequest();
        }

        return recyclerView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((VolleyApp) activity.getApplication()).inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        Bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Bus.unregister(this);
    }

    @Override
    public void onListItemClick(View view, RecyclerView.ViewHolder viewHolder) {
        Bundle fragmentExtras = new Bundle();
        fragmentExtras.putString(AppConstants.FRAGMENT_EXTRA_IMAGE_URL, responseList.get(viewHolder.getPosition()).getThumbnailUrl());
        Bus.postEvent(new AttachFragmentEvent(NetworkImageFragment.TAG, fragmentExtras));
    }


    private void startRequest() {
        progressDialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (preferenceManager.isDataSynced()) {
            List<ImageData> list = ImageData.getAll();
            responseList.addAll(list);
            imagesAdapter.notifyDataSetChanged();
            progressDialog.dismiss();
        } else {
            final Type listType = new TypeToken<List<ImageData>>() {
            }.getType();
            final GsonRequest<ImageData> rq = new GsonRequest<ImageData>(Request.Method.GET,
                    RestUtils.IMAGES_URL, listType, null,
                    new Response.Listener<List<ImageData>>() {
                        @Override
                        public void onResponse(List<ImageData> response) {
                            Bus.postEvent(new HttpResponseEvent<List<ImageData>>().setResponse(response));
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Bus.postEvent(new HttpResponseEvent<ImageData>().setVolleyError(error));
                        }
                    }
            );

            rq.setTag(TAG);
            rq.setShouldCache(true);
            requestQueue.add(rq);
        }

    }

    public void onEventMainThread(HttpResponseEvent<List<ImageData>> event) {
        VolleyError error = event.getVolleyError();
        if (error == null) {
            List<ImageData> list = (List<ImageData>) event.getResponse();
            ActiveAndroid.beginTransaction();
            try {
                int i;
                for (i = 0; i < list.size(); i++) {
                    ImageData item = list.get(i);
                    item.save();
                    if (i % 100 == 0) {
                        ActiveAndroid.setTransactionSuccessful();
                        ActiveAndroid.endTransaction();
                        ActiveAndroid.beginTransaction();
                    }
                }
                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }

            responseList.addAll(list);
            imagesAdapter.notifyDataSetChanged();
            preferenceManager.setDataSynced();
            progressDialog.dismiss();
        } else {
            Toast.makeText(getActivity(), "Please check n/w connection", Toast.LENGTH_LONG).show();
        }
    }

    public static class AttachFragmentEvent {
        private String fragmentTag;
        private Bundle fragmentExtras;

        public AttachFragmentEvent(String fragmentTag, Bundle fragmentExtras) {
            this.fragmentTag = fragmentTag;
            this.fragmentExtras = fragmentExtras;
        }

        public String getFragmentTag() {
            return fragmentTag;
        }

        public Bundle getFragmentExtras() {
            return fragmentExtras;
        }

        public void setFragmentExtras(Bundle fragmentExtras) {
            this.fragmentExtras = fragmentExtras;
        }
    }
}
