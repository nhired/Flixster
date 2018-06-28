package codepath.com.flixster.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie {
    private String title;
    private String overview;
    private String posterPath;


    public Movie(JSONObject obj) throws JSONException {
        title = obj.getString("title");
        overview = obj.getString("overview");
        posterPath = obj.getString("poster_path");
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
