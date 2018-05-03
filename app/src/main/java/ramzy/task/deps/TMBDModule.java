package ramzy.task.deps;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ramzy.task.database.MoviesDatabase;
import ramzy.task.networks.RestClient;

@Module
public class TMBDModule {

    private Context mContext;

    public TMBDModule(Context context) {
        mContext = context;
    }

    @Provides
    @Singleton
    RestClient.TMDBService provideTMBDService() {
        return RestClient.getTMBDService();
    }

    @Provides
    @Singleton
    MoviesDatabase provideMoviesDatabase() {
        return Room.databaseBuilder(mContext, MoviesDatabase.class, "movies").build();

    }
}
