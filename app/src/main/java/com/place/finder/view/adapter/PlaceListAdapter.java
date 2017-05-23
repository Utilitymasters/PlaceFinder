package com.place.finder.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.place.finder.R;
import com.place.finder.model.Result;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by rohit.anvekar on 5/19/2017.
 */

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.ViewHolder> implements Filterable {
    private List<Result> mDataset;
    private List<Result> mFilteredList;
    Context mContext;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public RelativeLayout mRelativeLayout;
        public ImageView placeIcon;
        public TextView txtPlaceName;
        public TextView txtPlaceAddress;

        public ViewHolder(RelativeLayout v) {
            super(v);
            mRelativeLayout = v;
            placeIcon =  ButterKnife.findById(v,R.id.placeIcon);
            txtPlaceName =  ButterKnife.findById(v,R.id.placeName);
            txtPlaceAddress =  ButterKnife.findById(v,R.id.placeAddress);
        }




    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PlaceListAdapter(Context context, List<Result> resultList) {
        mContext = context;
        mDataset = resultList;
        mFilteredList = resultList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PlaceListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item_view, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        Glide.with(mContext)
                .load(mFilteredList.get(position).getIcon())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .skipMemoryCache(true)
                .into(holder.placeIcon);
        holder.txtPlaceName.setText(mFilteredList.get(position).getName());
        holder.txtPlaceAddress.setText(mFilteredList.get(position).getFormattedAddress());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }


    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mDataset;
                } else {

                    ArrayList<Result> filteredList = new ArrayList<>();

                    for (Result result : mDataset) {

                       // if (String.getApi().toLowerCase().contains(charString) || String.getName().toLowerCase().contains(charString) || String.getVer().toLowerCase().contains(charString)) {
                        if(result.getName().contains(charString)){

                            filteredList.add(result);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Result>) filterResults.values;
                notifyDataSetChanged();
            }
        };

    }

    public void reset(){
        mFilteredList.clear();;
        notifyDataSetChanged();
    }
}

