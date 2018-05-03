package ramzy.task.interactors;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import ramzy.task.contracts.MoviesListContract;
import ramzy.task.database.MoviesDao;
import ramzy.task.models.Movie;
import ramzy.task.networks.MoviesResponse;

public class MoviesListInteractor implements MoviesListContract.MoviesListInteractor {

    private CompositeDisposable mCompositeDisposable;
    private MoviesDao mMoviesDao;
    private OnGetMoviesListener mListener;
    private Consumer<MoviesResponse> mOnSuccess = new Consumer<MoviesResponse>() {
        @Override
        public void accept(MoviesResponse moviesResponse) throws Exception {
            mListener.onGetMoviesDone(moviesResponse);
        }
    };
    private Consumer<Throwable> mOnError = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            mListener.onGetMoviesError();
        }
    };

    public MoviesListInteractor(@NonNull MoviesDao moviesDao) {
        mMoviesDao = moviesDao;
    }

    @Override
    public void getMoviesBy(int page
            , @NonNull Observable<MoviesResponse> movieObservable
            , @NonNull final OnGetMoviesListener listener
            , @Movie.FILTER String filter) {
        mListener = listener;
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(movieObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(getDBObservable(page, filter))
                .subscribe(mOnSuccess, mOnError));
    }

    private Observable<MoviesResponse> getDBObservable(int page, @Movie.FILTER String filter) {
        return mMoviesDao
                .getMoviesList(page, filter).flatMap(new Function<List<Movie>, SingleSource<MoviesResponse>>() {
                    @Override
                    public SingleSource<MoviesResponse> apply(List<Movie> movies) throws Exception {
                        return Single.just(new MoviesResponse(movies));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .doOnNext(mOnSuccess).doOnError(mOnError);
    }

    @Override
    public void saveToLocalDatabase(final List<Movie> movies, @Movie.FILTER final String filter) {
        mCompositeDisposable.add(Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                for (Movie movie : movies) {
                    movie.setSort(filter);
                    mMoviesDao.saveMovie(movie);
                }
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
