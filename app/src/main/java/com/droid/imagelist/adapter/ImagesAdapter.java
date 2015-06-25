package com.droid.imagelist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.droid.imagelist.R;
import com.droid.imagelist.interfaces.OnListItemClickListener;
import com.droid.imagelist.model.ImageData;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private OnListItemClickListener onListItemClickListener;
    private List<ImageData> imageDataList;
    private ImageLoader imageLoader;

    public ImagesAdapter(List<ImageData> commentList, OnListItemClickListener clickListener, Context context, ImageLoader imageLoader) {
        this.mContext = context;
        this.imageDataList = commentList;
        this.onListItemClickListener = clickListener;
        this.imageLoader = imageLoader;
    }

    public List<ImageData> getDataSource() {
        return imageDataList;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_example, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // String dataItem = getItem(position);
        ImageData imageData = imageDataList.get(position);
        setItemData((ViewHolder) holder, imageData, position);
        // cast holder to VHItem and set data

    }

    @Override
    public int getItemCount() {
        return imageDataList.size();
    }

    private void setItemData(ViewHolder holder, ImageData listItemData, int position) {
        holder.imageView.setImageUrl(listItemData.getThumbnailUrl(), imageLoader);
        holder.descriptionTextView.setText(listItemData.getTitle());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        NetworkImageView imageView;
        TextView descriptionTextView;

        public ViewHolder(View view) {
            super(view);
            imageView = (NetworkImageView) view.findViewById(R.id.mapNetworkImageView);
            descriptionTextView = (TextView) view.findViewById(R.id.info_text);

            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onListItemClickListener != null)
                onListItemClickListener.onListItemClick(v, this);
        }

    }
}
