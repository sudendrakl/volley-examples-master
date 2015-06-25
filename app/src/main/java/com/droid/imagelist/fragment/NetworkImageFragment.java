package com.droid.imagelist.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.droid.imagelist.R;
import com.droid.imagelist.VolleyApp;
import com.droid.imagelist.util.AppConstants;

import javax.inject.Inject;

public class NetworkImageFragment extends Fragment {

    public static final String TAG = NetworkImageFragment.class.getName();

    @Inject ImageLoader imageLoader;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        VolleyApp app = (VolleyApp) activity.getApplication();
        app.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        getActivity().getActionBar().setTitle(R.string.networkimage_example);

        View root = inflater.inflate(R.layout.fragment_network_image, container, false);

        NetworkImageView imageView = (NetworkImageView) root.findViewById(R.id.mapNetworkImageView);
        // imageView.setErrorImageResId(R.drawable.my_error_image);
        // imageView.setDefaultImageResId(R.drawable.my_default_image);
        Bundle extras = getArguments();
        imageView.setImageUrl(extras.getString(AppConstants.FRAGMENT_EXTRA_IMAGE_URL), imageLoader);

        return root;
    }
}
