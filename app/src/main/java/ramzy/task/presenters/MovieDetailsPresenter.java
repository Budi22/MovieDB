package ramzy.task.presenters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.Observable;
import ramzy.task.contracts.MovieDetailsContract;
import ramzy.task.database.MoviesDatabase;
import ramzy.task.interactors.MovieDetailsInteractor;
import ramzy.task.models.Movie;
import ramzy.task.networks.RestClient;

public class MovieDetailsPresenter implements MovieDetailsContract.MovieDetailsPresenter {

    private RestClient.TMDBService mService;
    private MovieDetailsInteractor mMovieDetailsInteractor;
    private MovieDetailsContract.MovieDetailsView mView;

    @Override
    public void attach(@NonNull MovieDetailsContract.MovieDetailsView view, @NonNull RestClient.TMDBService service
            , @NonNull MoviesDatabase moviesDatabase) {
        mMovieDetailsInteractor = new MovieDetailsInteractor(moviesDatabase.getMoviesDao());
        mView = view;
        mService = service;
    }

    @Override
    public void detach() {
        if (mMovieDetailsInteractor != null) {
            mMovieDetailsInteractor.detach();
        }
    }

    @Override
    public void getMovie(@NonNull String movieId) {
        final Observable<Movie> call = mService.fetchMovie(movieId);
        mView.updateLoading(true);
        mMovieDetailsInteractor.getMoviesBy(movieId, call, new MovieDetailsInteractor.OnGetMovieListener() {
            @Override
            public void onGetMovieDone(@Nullable Movie movie) {
                mView.updateLoading(false);
                if (movie != null) {
                    mView.showMovieDetails(movie);
                    saveToDatabase(movie);
                } else {
                    mView.showNoResults();
                }
            }

            @Override
            public void onGetMovieError() {
                mView.updateLoading(false);
                mView.showConnectionError();
            }
        });
    }

    private void saveToDatabase(@NonNull Movie movie) {
        mMovieDetailsInteractor.saveToLocalDatabase(movie);
    }
}
