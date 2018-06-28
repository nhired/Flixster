package codepath.com.flixster;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


import codepath.com.flixster.models.Movie;
import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    //CONSTANTS
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    public final static String API_KEY_PARAM = "api_key";
    //logging helper for errors
    public final static String TAG = "MovieListActivity";

    //instance variables
    AsyncHttpClient client;
    String imageBaseURL;
    String posterSize;

    ArrayList<Movie> movieList;

    RecyclerView rvMovies;
    MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        client = new AsyncHttpClient();
        movieList = new ArrayList<Movie>();
        //intialize adapter
        adapter = new MovieAdapter(movieList);

        //resolve the recycler view and connect a layout manager and the adapter
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        //get configuration on start of app creation
        getConfiguration();

    }

    private void getNowPlaying() {
        //create URL
        String url = API_BASE_URL + "/movie/now_playing";
        //set request params
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        //execute a GET request
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load results into movie list
                try {
                    JSONArray results = response.getJSONArray("results");
                    //iterate thru array to create movie objects
                    for(int i = 0; i < results.length(); i++) {
                        Movie movie = new Movie(results.getJSONObject(i));
                        movieList.add(movie);
                        //notify adapter that a row was added
                        adapter.notifyItemInserted(movieList.size() - 1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    logError("Failed to parse now playing movies", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now playing endpoint", throwable, true);
            }
        });
    }

    private void getConfiguration() {
         //create URL
        String url = API_BASE_URL + "/configuration";
        //set request params
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        //execute GET request expecting JSON object
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject images = response.getJSONObject("images");

                    //get image base url
                    imageBaseURL = images.getString("secure_base_url");
                    //get poster size
                    JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
                    posterSize = posterSizeOptions.optString(3, "w342");
                    Log.i(TAG, String.format("Loaded configuraton with imageBaseURL %s and posterSize %s", imageBaseURL, posterSize));
                    //get now playing movie list
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
               logError("Failed getting configuration", throwable, true);
            }
        });
    }

    //handle my own errors
    private void logError(String message, Throwable error, boolean alertUser){
        Log.e(TAG, message, error);
        //alert user through a Toast
        if(alertUser) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

}
