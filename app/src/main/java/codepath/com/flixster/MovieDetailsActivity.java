package codepath.com.flixster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import codepath.com.flixster.models.Movie;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static codepath.com.flixster.MovieAdapter.imgUrl;

public class MovieDetailsActivity extends AppCompatActivity {
    Movie movie;

    // the view objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView imageView;
    TextView releaseDate;
    TextView popularity;
    TextView voteCount;


    MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        // resolve the view objects
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        releaseDate = (TextView) findViewById(R.id.releaseDate);
        popularity = (TextView) findViewById(R.id.popularity);
        voteCount = (TextView) findViewById(R.id.voteCount);



        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        //release date
        releaseDate.setText("Release Date: " + movie.getReleaseDate());

        //vote count
       voteCount.setText("Vote Count: " + Integer.toString(movie.getVoteCount()));

       //popularity
        popularity.setText("Popularity: " + Integer.toString(movie.getPopularity()));


















    }

}
