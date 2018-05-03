package ramzy.task.presenters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import ramzy.task.contracts.MoviesListContract;
import ramzy.task.database.MoviesDatabase;
import ramzy.task.interactors.MoviesListInteractor;
import ramzy.task.models.Movie;
import ramzy.task.networks.MoviesResponse;
import ramzy.task.networks.RestClient;

public class MoviesListPresenter implements MoviesListContract.MoviesListPresenter {

    private static final int SEARCH_DELAY = 300;

    @Movie.FILTER
    private String mFilter;
    private RestClient.TMDBService mService;
    private MoviesListInteractor mMoviesListInteractor;
    private MoviesListContract.MoviesListView mView;
    private int mCurrentPage = 1;
    private String mQueryText;

    @Override
    public void attach(@NonNull MoviesListContract.MoviesListView view, @NonNull RestClient.TMDBService service
            , @NonNull MoviesDatabase moviesDatabase) {
        mMoviesListInteractor = new MoviesListInteractor(moviesDatabase.getMoviesDao());
        mView = view;
        mService = service;
    }

    @Override
    public void detach() {
        if (mMoviesListInteractor != null) {
            mMoviesListInteractor.detach();
        }
    }

    private void getMoviesBy(@Movie.FILTER String filter, int page) {
        mFilter = filter;
        mCurrentPage = page;
        final Observable<MoviesResponse> call;
        switch (filter) {
            case Movie.FILTER.SEARCH:
                call = mService.moviesByQuery(mQueryText, mCurrentPage)
                        .debounce(SEARCH_DELAY, TimeUnit.MILLISECONDS)
                        .distinctUntilChanged();
                break;
            case Movie.FILTER.LATEST_MOVIES:
                call = mService.latestMovies(mCurrentPage);
                break;
            case Movie.FILTER.POPULAR_MOVIES:
                call = mService.popularMovies(mCurrentPage);
                break;
            case Movie.FILTER.TOP_RATED_MOVIES:
                call = mService.topRatedMovies(mCurrentPage);
                break;
            case Movie.FILTER.UPCOMING_MOVIES:
                call = mService.upcomingMovies(mCurrentPage);
                break;
            default:
                call = mService.topMovies(mCurrentPage);
        }
        mView.updateLoading(true);
        mMoviesListInteractor.getMoviesBy(mCurrentPage, call, new MoviesListInteractor.OnGetMoviesListener() {
            @Override
            public void onGetMoviesDone(@Nullable MoviesResponse moviesResponse) {
                mView.updateLoading(false);
                if (moviesResponse == null || moviesResponse.getmMovies().isEmpty()) {
                    mView.showNoResults();
                } else {
                    mView.updateMovies(moviesResponse.getmMovies(), mCurrentPage == 1);
                    saveToDatabase(moviesResponse.getmMovies());
                }
            }

            @Override
            public void onGetMoviesError() {
                mView.updateLoading(false);
                mView.showConnectionError();
            }
        }, mFilter);
    }

    private void saveToDatabase(List<Movie> movies) {
        mMoviesListInteractor.saveToLocalDatabase(movies, mFilter);
    }

    public void nextPage() {
        mCurrentPage++;
        getMoviesBy(mFilter, mCurrentPage);
    }

    public void setQueryText(@NonNull final String query) {
        mQueryText = query;
    }

    public void getMoviesBy(@NonNull @Movie.FILTER String filter) {
        getMoviesBy(filter, 1);
    }
}
