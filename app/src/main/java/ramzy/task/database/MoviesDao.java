package ramzy.task.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;
import ramzy.task.models.Movie;

@Dao
public interface MoviesDao {
    @Query("SELECT * FROM movie where id = :movieId limit 1")
    Movie getMovie(String movieId);

    @Query("SELECT * FROM movie where id = :movieId and has_details = 1 limit 1")
    Single<Movie> getDetailedMovie(String movieId);

    @Query("SELECT * FROM movie where sort = :filter limit ((:page - 1) * 10), (:page * 10)")
    Single<List<Movie>> getMoviesList(int page, @Movie.FILTER String filter);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveMovie(Movie movie);
}
