package com.example.walkatheri.popularmoviesapp.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.MalformedURLException;
import java.net.URL;


public class NetworkUtils {


    private final static String BASE_MOVIE_URL = "http://api.themoviedb.org/3/movie/";

    private final static String BASE_IMG_URL = "http://image.tmdb.org/t/p/";
    private final static String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    private final static String API_KEY_PARAM = "api_key";
    private final static String APPENDTO_PARAM = "append_to_response";
    private final static String REVIEW_TRAILERS = "trailers,reviews";
    private final static String IMAGE_SIZE = "w185";

    private static onResponseHandler onResponseHandler;


    public interface onResponseHandler {
        void onResponse(String response);
    }


    public static URL buildApiUrl(String prefernce, String apiKey) {
        Uri builtURI = Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendEncodedPath(prefernce)
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();

        URL url = null;

        try {
            url = new URL(builtURI.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d("NETWORKUTILS", "URL " + url);

        return url;
    }

    public static URL buildImageURL(String imageresponse) {
        Uri builtURI = Uri.parse(BASE_IMG_URL).buildUpon()
                .appendPath(IMAGE_SIZE)
                .appendEncodedPath(imageresponse)
                .build();

        URL url = null;

        try {
            url = new URL(builtURI.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildReviewTrailerURL(long movieID, String apiKey) {
        Uri builtURI = Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendEncodedPath(String.valueOf(movieID))
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .appendQueryParameter(APPENDTO_PARAM, REVIEW_TRAILERS)

                .build();

        URL url = null;

        try {
            url = new URL(builtURI.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.i("URL BUILT", url.toString());
        return url;
    }

    public static String buildYoutubeurl(String trailerID) {
        return BASE_YOUTUBE_URL + trailerID;
    }


    public static void getResponseUsingVolley(String url, Context context) {
        boolean isOnline = isOnline(context);
        onResponseHandler = (onResponseHandler) context;

        if (!isOnline) {
            onResponseHandler.onResponse(null);
        } else {
            RequestQueue queue = Volley.newRequestQueue(context);


            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            onResponseHandler.onResponse(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.i("VOLLEY ERROR", error.toString());
                    onResponseHandler.onResponse(null);
                }
            });


            queue.add(stringRequest);
        }
    }

    /**
     * checks the connectivity
     *
     * @return true if internet is available else false
     */
    private static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }


}
