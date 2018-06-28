package codepath.com.flixster;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import java.util.ArrayList;

import codepath.com.flixster.models.Config;
import codepath.com.flixster.models.Movie;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    ArrayList<Movie> movies;

    //config needed for image urls
    Config config;
    Context context;


    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    //creates and inflates new view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //get context from parent
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //create view using the item movie layout
        View movieView = inflater.inflate(R.layout.item_movie, viewGroup, false);

        return new ViewHolder(movieView);
    }

    //binds an inflated view to a new item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //get movie data at specified position
        Movie movie = movies.get(i);
        //populate the view with the movie data
        viewHolder.tvTitle.setText(movie.getTitle());
        viewHolder.tvOverview.setText(movie.getOverview());

        //determine current orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        //build url for poster image
        String imageUrl = null;

        //if in portrait mode
        if(isPortrait) {
            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        } else {
            //load backdrop
            imageUrl = config.getImageUrl(config.getBackDropSize(), movie.getBackdropPath());
        }

        //get correct placeholder
        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? viewHolder.ivPoster : viewHolder.ivBackdropImage;
        int radius = 30;
        int margin = 10;
        //load image using glide
        GlideApp.with(context)
                .load(imageUrl)
                .placeholder(placeholderId)
                .error(placeholderId)
                .transform(new RoundedCornersTransformation(radius, margin))
                .into(imageView);

    }

    //returns the total number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        //track view objects
        ImageView ivPoster;
        ImageView ivBackdropImage;
        TextView tvTitle;
        TextView tvOverview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //lookup view object by id
            ivPoster = (ImageView) itemView.findViewById(R.id.ivPoster);
            ivBackdropImage = (ImageView) itemView.findViewById(R.id.ivBackdropImage);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this);
        }

        // when the user clicks on a row, show MovieDetailsActivity for the selected movie
        @Override
        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the activity
                context.startActivity(intent);
            }
        }
    }


}
