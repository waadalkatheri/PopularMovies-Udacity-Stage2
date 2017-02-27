package com.example.walkatheri.popularmoviesapp;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.walkatheri.popularmoviesapp.data.TrailerReview;

import java.util.ArrayList;

public class TrailerAdapter extends ArrayAdapter<TrailerReview> {

    private ArrayList<TrailerReview> trailerlist=null;


    public TrailerAdapter(Context context, int resource, ArrayList<TrailerReview> trailerlist) {
        super(context, resource,trailerlist);
    }


    @Override
    public int getCount() {
        if(trailerlist!=null)
            return trailerlist.size();
        else {

            return 0;

        }
    }

    @Nullable
    @Override
    public TrailerReview getItem(int position) {
        return trailerlist.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_trailers, parent, false);
        }

        TrailerReview trailerReview =getItem(position);


        Log.i("TRIALER EVIEW DETAIL", trailerReview.getTrailename());
        TextView trailernameTextView = (TextView) listItemView.findViewById(R.id.trailerName);
        trailernameTextView.setGravity(Gravity.CENTER);
        //have to use for youtube thumbnail API
        ImageView trailerImageView = (ImageView) listItemView.findViewById(R.id.imageTrailer);

        trailerImageView.setVisibility(View.GONE);
        ImageView shareImageView = (ImageView) listItemView.findViewById(R.id.sharebutton);


        shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout relativeLayout=(RelativeLayout) view.getParent();
                TextView trailerText= (TextView)relativeLayout.getChildAt(1);
                String trailerName= trailerText.getText().toString();
                String trailerUrl= gettrailerURLfromName(trailerName);
                Log.i("trailerUrl",trailerUrl);

                MovieDetails movieDetail=(MovieDetails) getContext();
                movieDetail.shareTrailer(trailerUrl);

            }
        });

        trailernameTextView.setText(trailerReview.getTrailename());




        return listItemView;
    }

    public void setTrailerlist(ArrayList<TrailerReview> trailerreviewlist)
    {
        Log.i("here lius came",String.valueOf(trailerreviewlist.size()));
        trailerlist=trailerreviewlist;
        notifyDataSetChanged();
    }

    public String gettrailerURLfromName(String trailerName)
    {
        String trailerUrl="";
        if(trailerlist!=null)
        {
            for(int iter=0;iter<trailerlist.size();iter++)
            {
                TrailerReview trailerReview=trailerlist.get(iter);
                if(trailerReview.getTrailename().equals(trailerName))
                {
                    trailerUrl=trailerReview.getTrailerurl();
                    break;
                }
            }
        }
        return trailerUrl;

    }




}

