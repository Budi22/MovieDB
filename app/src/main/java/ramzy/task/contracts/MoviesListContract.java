package ramzy.task.contracts;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Observable;
import ramzy.task.interactors.BaseInteractor;
import ramzy.task.models.Movie;
import ramzy.task.networks.MoviesResponse;
import ramzy.task.presenters.BasePresenter;

public interface MoviesListContract {

    interface MoviesListView {
        void showByFilter(@NonNull @Movie.FILTER String filter);

        void setQueryText(@NonNull String query);

        void updateLoading(boolean shouldShow);

        void updateMovies(@NonNull List<Movie> movies, boolean clear);

        void showConnectionError();

        void showNoResults();
    }

    interface MoviesListPresenter extends BasePresenter<MoviesListView> {
        void nextPage();

        void setQueryText(@NonNull String query);

        void getMoviesBy(@NonNull @Movie.FILTER String filter);
    }

    interface MoviesListInteractor extends BaseInteractor {
        void getMoviesBy(int page, @NonNull Observable<MoviesResponse> movieObservable
                , @NonNull OnGetMoviesListener listener
                , @Movie.FILTER String filter);

        void saveToLocalDatabase(List<Movie> movies
                , @Movie.FILTER String filter);

        interface OnGetMoviesListener {
            void onGetMoviesDone(@Nullable MoviesResponse moviesResponse);

            void onGetMoviesError();
        }
    }
}
