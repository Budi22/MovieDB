package ramzy.task.interactors;

import android.support.annotation.NonNull;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ramzy.task.contracts.MovieDetailsContract;
import ramzy.task.database.MoviesDao;
import ramzy.task.models.Movie;

public class MovieDetailsInteractor implements MovieDetailsContract.MovieDetailsInteractor {

    private CompositeDisposable mCompositeDisposable;
    private MoviesDao mMoviesDao;
    private OnGetMovieListener mListener;
    private Consumer<Movie> mOnSuccess = new Consumer<Movie>() {
        @Override
        public void accept(Movie movie) throws Exception {
            mListener.onGetMovieDone(movie);
        }
    };
    private Consumer<Throwable> mOnError =  new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            mListener.onGetMovieError();
        }
    };

    public MovieDetailsInteractor(@NonNull MoviesDao moviesDao) {
        mMoviesDao = moviesDao;
    }

    @Override
    public void getMoviesBy(@NonNull String movieId
            , @NonNull Observable<Movie> movieObservable, @NonNull final OnGetMovieListener listener) {
        mListener = listener;
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(movieObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(getDBObservable(movieId))
                .subscribe(mOnSuccess, mOnError));
    }

    private ObservableSource<? extends Movie> getDBObservable(String movieId) {
        return mMoviesDao
                .getDetailedMovie(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .doOnNext(mOnSuccess).doOnError(mOnError);
    }

    @Override
    public void saveToLocalDatabase(final Movie movie) {
        mCompositeDisposable.add(Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                final Movie oldMovie = mMoviesDao.getMovie(movie.getId());
                if (oldMovie != null) {
                    movie.setSort(oldMovie.getSort());
                }
                movie.setHasDetails(1);
                mMoviesDao.saveMovie(movie);
                return true;
            }
        }).subscribeOn(Schedulers.io()).subscribe());
    }

    @Override
    public void detach() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }
}
