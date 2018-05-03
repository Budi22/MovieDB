package ramzy.task.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ramzy.task.models.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MoviesDatabase extends RoomDatabase{
    public abstract MoviesDao getMoviesDao();
}
