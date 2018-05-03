package ramzy.task.adapters;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import ramzy.task.R;
import ramzy.task.models.Movie;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private final List<Movie> mMovies;

    private MoviesAdapter(final List<Movie> movies) {
        this.mMovies = movies;
    }

    public MoviesAdapter() {
        this(new ArrayList<Movie>(0));
    }

    public void updateMovies(final List<Movie> movies, boolean clear) {
        if (clear) {
            mMovies.clear();
        }
        mMovies.addAll(movies);
        notifyDataSetChanged();
    }

    public List<Movie> getMovies() {
        return mMovies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, final int position) {
        final Movie movie = mMovies.get(position);
        final Transformation transformation = new RoundedCornersTransformation(
                holder.mPosterCornerRadius, holder.mPosterCornerMargin);
        if (movie.getPosterURL().isEmpty()) {
            holder.mPosterImageView.setImageDrawable(holder.mNoImageDrawable);
        } else {
            Picasso.with(holder.mPosterImageView.getContext())
                    .load(mMovies.get(position).getPosterURL())
                    .transform(transformation)
                    .into(holder.mPosterImageView);
        }
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindDimen(R.dimen.poster_corner_radius)
        int mPosterCornerRadius;
        @BindDimen(R.dimen.poster_corner_margin)
        int mPosterCornerMargin;
        @BindDrawable(R.drawable.ic_no_image)
        Drawable mNoImageDrawable;
        @BindView(R.id.card_movie_poster)
        ImageView mPosterImageView;

        MovieViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
