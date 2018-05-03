package ramzy.task.utils;

public class Constants {

    private Constants(){}

    private static final String TMBD_API_KEY = "?api_key=ac557a0de76b32bc4259da4b77e95ea3";

    public static final String API_BASE_URL = "https://api.themoviedb.org/3/";
    public static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500";
    public static final String REQUEST_CONTENT_TYPE = "Content-Type: application/json;charset=utf-8";
    public static final String END_POINT_SEARCH_MOVIES = "search/movie" + TMBD_API_KEY;
    public static final String END_POINT_TOP_MOVIES = "movie/top_rated" + TMBD_API_KEY;
    public static final String END_POINT_LATEST_MOVIES = "movie/latest" + TMBD_API_KEY;
    public static final String END_POINT_POPULAR_MOVIES = "movie/popular" + TMBD_API_KEY;
    public static final String END_POINT_TOP_RATED_MOVIES = "movie/top_rated" + TMBD_API_KEY;
    public static final String END_POINT_UPCOMING_MOVIES = "movie/upcoming" + TMBD_API_KEY;
    public static final String END_POINT_GET_MOVIE = "movie/{movie_id}" + TMBD_API_KEY;
}
