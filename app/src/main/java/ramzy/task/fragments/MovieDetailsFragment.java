package ramzy.task.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ramzy.task.R;
import ramzy.task.contracts.MovieDetailsContract;
import ramzy.task.models.Movie;
import ramzy.task.presenters.MovieDetailsPresenter;
import ramzy.task.utils.MainActivityUpdater;

public class MovieDetailsFragment extends BaseServiceFragment implements MovieDetailsContract.MovieDetailsView {

    private static final String ARGS_MOVIE_ID = "movieId";

    @BindView(R.id.progress_bar_loading)
    ProgressBar mLoadingBar;
    @BindView(R.id.imageView_poster_image)
    ImageView mPosterImage;
    @BindView(R.id.textView_movie_date)
    TextView mMovieDate;
    @BindView(R.id.textView_movie_description)
    TextView mMovieDescription;
    private MovieDetailsPresenter mPresenter;
    private MainActivityUpdater mCallback;

    public static MovieDetailsFragment newInstance(@NonNull String movieId) {
        MovieDetailsFragment movieDetails = new MovieDetailsFragment();
        final Bundle data = new Bundle();
        data.putString(ARGS_MOVIE_ID, movieId);
        movieDetails.setArguments(data);
        return movieDetails;
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new MovieDetailsPresenter();
        mPresenter.attach(this, getService(), getMoviesDatabase());
        final Bundle data = getArguments();
        if (data != null) {
            final String movieId = data.getString(ARGS_MOVIE_ID, "");
            mPresenter.getMovie(movieId);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detach();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (MainActivityUpdater) context;
            mCallback.updateMenuIcon(true);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement MainActivityUpdater");
        }
    }

    @Override
    public void showMovieDetails(@NonNull Movie movie) {
        final Activity activity = getActivity();
        if (activity != null) {
            Picasso.with(activity.getApplicationContext())
                    .load(movie.getPosterURL())
                    .into(mPosterImage);

            mMovieDate.setText(movie.getReleaseDate());
            mMovieDescription.setText(movie.getOverView());
        }
    }

    @Override
    public void updateLoading(boolean shouldShow) {
        mLoadingBar.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showConnectionError() {
        back();
    }

    @Override
    public void showNoResults() {
        back();
    }

    private void back() {
        if (getActivity() != null && mCallback != null) {
            mCallback.goBack();
        }
    }
}
