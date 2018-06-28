package codepath.com.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {

    String imageBaseURL;
    String posterSize;
    String backDropSize;


    public Config(JSONObject obj) throws JSONException {
        JSONObject images = obj.getJSONObject("images");

        //get image base url
        imageBaseURL = images.getString("secure_base_url");
        //get poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        posterSize = posterSizeOptions.optString(3, "w342");

        //parse backdrop sizes
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backDropSize = backdropSizeOptions.optString(1, "w788");


    }

    //helper for creating urls
    public String getImageUrl(String size, String path) {
        return String.format("%s%s%s", imageBaseURL, size, path);
    }

    public String getImageBaseURL() {
        return imageBaseURL;
    }

    public String getPosterSize() {
        return posterSize;
    }
}
