package codepath.com.flixster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    //CONSTANTS
    public final static String API_BASE_URL = "https://www.themoviedb.org/3";
    public final static String API_KEY_PARAM = "api_key";
    //logging helper for errors
    public final static String TAG = "MovieListActivity";

    //instance variables
    AsyncHttpClient client;
    String imageBaseURL;
    String  posterSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        client = new AsyncHttpClient();

        //get configuration on start of app creation
        getConfiguration();
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
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

}
