package com.place.finder.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.place.finder.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by rohit.anvekar on 5/20/2017.
 */

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.ViewHolder> {
    private List<Bitmap> mDataset;
    Context mContext;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public RelativeLayout mRelativeLayout;
        public ImageView photoView;
        public ImageView photoDownload;

        public ViewHolder(RelativeLayout v) {
            super(v);
            mRelativeLayout = v;
            photoView =  ButterKnife.findById(v, R.id.photoView);
            photoDownload =  ButterKnife.findById(v,R.id.photoDownload);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PhotoListAdapter(Context context, List<Bitmap> photoList) {
        mContext = context;
        mDataset = photoList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PhotoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_view, parent, false);
        // set the view's size, margins, paddings and layout parameters

        PhotoListAdapter.ViewHolder vh = new PhotoListAdapter.ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PhotoListAdapter.ViewHolder holder, final int position) {

        holder.photoView.setImageBitmap(mDataset.get(position));
        
        holder.photoDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String filename = "place_"+position+".png";
                File dest = new File(Environment.getExternalStorageDirectory() , "PlacePhoto");
                if (!dest.exists()) {
                    Log.i("photoDownload","creating");
                    dest.mkdir();
                    Log.i("photoDownload","created...");
                }

                File imageFile = new File(dest,filename);
                try {
                    FileOutputStream out = new FileOutputStream(imageFile);
                    mDataset.get(position).compress(Bitmap.CompressFormat.PNG, 75, out);
                    out.flush();
                    out.close();
                    Toast.makeText(mContext, "Image downloaded successfully here: "+dest.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
