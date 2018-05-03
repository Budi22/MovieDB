package ramzy.task.contracts;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.Observable;
import ramzy.task.interactors.BaseInteractor;
import ramzy.task.models.Movie;
import ramzy.task.presenters.BasePresenter;

public interface MovieDetailsContract {

    interface MovieDetailsView {
        void showMovieDetails(@NonNull Movie movie);

        void updateLoading(boolean shouldShow);

        void showConnectionError();

        void showNoResults();
    }

    interface MovieDetailsPresenter extends BasePresenter<MovieDetailsView> {
        void getMovie(@NonNull String movieId);
    }

    interface MovieDetailsInteractor extends BaseInteractor {
        void getMoviesBy(@NonNull String movieId
                , @NonNull Observable<Movie> movieObservable
                , @NonNull OnGetMovieListener listener);

        void saveToLocalDatabase(Movie movie);

        interface OnGetMovieListener {
            void onGetMovieDone(@Nullable Movie movie);

            void onGetMovieError();
        }
    }
}
