package ramzy.task.networks;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import ramzy.task.models.Movie;

public class MoviesResponse {

    @SerializedName("results")
    private List<Movie> mMovies;

    public MoviesResponse() {}

    public MoviesResponse(List<Movie> mMovies) {
        this.mMovies = mMovies;
    }

    public List<Movie> getmMovies() {
        if (mMovies == null) {
            return new ArrayList<>(0);
        }
        return mMovies;
    }
}
