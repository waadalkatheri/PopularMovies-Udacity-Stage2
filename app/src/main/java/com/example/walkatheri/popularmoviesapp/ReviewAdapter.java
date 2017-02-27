

package com.example.walkatheri.popularmoviesapp;


        import android.content.Context;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.example.walkatheri.popularmoviesapp.data.TrailerReview;

        import java.util.ArrayList;

public class ReviewAdapter extends ArrayAdapter<TrailerReview> {

    private ArrayList<TrailerReview> reviewlist=null;


    public ReviewAdapter(Context context, int resource, ArrayList<TrailerReview> reviewlist) {
        super(context, resource,reviewlist);
    }


    @Override
    public int getCount() {
        if(reviewlist!=null)
            return reviewlist.size();
        else

            return 0;


    }

    @Nullable
    @Override
    public TrailerReview getItem(int position) {
        return reviewlist.get(position);
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


        Log.i("TRIALER EVIEW DETAIL", trailerReview.getReviewContent());
        TextView reviewTextView = (TextView) listItemView.findViewById(R.id.trailerName);
        ImageView shareImageView = (ImageView) listItemView.findViewById(R.id.sharebutton);
        ImageView trailerImageView = (ImageView) listItemView.findViewById(R.id.imageTrailer);

        shareImageView.setVisibility(View.GONE);
        trailerImageView.setVisibility(View.GONE);

        reviewTextView.setTextSize(16);
        reviewTextView.setPadding(16,0,8,16) ;

        reviewTextView.setText(trailerReview.getReviewContent());




        return listItemView;
    }

    public void setReviewlist(ArrayList<TrailerReview> trailerreviewlist)
    {
        Log.i("here lius came",String.valueOf(trailerreviewlist.size()));
        reviewlist=trailerreviewlist;
        notifyDataSetChanged();
    }



}

