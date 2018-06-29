package codepath.com.flixster.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Movie {
     String title;
     String overview;
     String posterPath;
     String backdropPath;

     Double voteAverage;

     Integer id;

     public Movie() {

     }

    public Movie(JSONObject obj) throws JSONException {
        title = obj.getString("title");
        overview = obj.getString("overview");
        posterPath = obj.getString("poster_path");
        backdropPath = obj.getString("backdrop_path");
        voteAverage = obj.getDouble("vote_average");
        id = Integer.parseInt(String.valueOf(obj.getJSONObject("id")));
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

    public String getBackdropPath() { return backdropPath; }

    public Double getVoteAverage() { return voteAverage; }
}
