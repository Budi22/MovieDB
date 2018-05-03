package ramzy.task.networks;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import ramzy.task.models.Movie;
import ramzy.task.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RestClient {

    private static TMDBService mTMBDService;

    public static TMDBService getTMBDService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .connectTimeout(3, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        if (mTMBDService == null) {
            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
            mTMBDService = retrofit.create(TMDBService.class);
        }
        return mTMBDService;
    }

    public interface TMDBService {

        @Headers(Constants.REQUEST_CONTENT_TYPE)
        @GET(Constants.END_POINT_SEARCH_MOVIES)
        Observable<MoviesResponse> moviesByQuery(@Query("query") String query,
                                                 @Query("page") int page);

        @Headers(Constants.REQUEST_CONTENT_TYPE)
        @GET(Constants.END_POINT_TOP_MOVIES)
        Observable<MoviesResponse> topMovies(@Query("page") int page);

        @Headers(Constants.REQUEST_CONTENT_TYPE)
        @GET(Constants.END_POINT_LATEST_MOVIES)
        Observable<MoviesResponse> latestMovies(@Query("page") int page);

        @Headers(Constants.REQUEST_CONTENT_TYPE)
        @GET(Constants.END_POINT_POPULAR_MOVIES)
        Observable<MoviesResponse> popularMovies(@Query("page") int page);

        @Headers(Constants.REQUEST_CONTENT_TYPE)
        @GET(Constants.END_POINT_TOP_RATED_MOVIES)
        Observable<MoviesResponse> topRatedMovies(@Query("page") int page);

        @Headers(Constants.REQUEST_CONTENT_TYPE)
        @GET(Constants.END_POINT_UPCOMING_MOVIES)
        Observable<MoviesResponse> upcomingMovies(@Query("page") int page);

        @Headers(Constants.REQUEST_CONTENT_TYPE)
        @GET(Constants.END_POINT_GET_MOVIE)
        Observable<Movie> fetchMovie(@Path("movie_id") String movieId);
    }
}
