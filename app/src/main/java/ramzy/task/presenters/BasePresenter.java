package ramzy.task.presenters;

import android.support.annotation.NonNull;

import ramzy.task.database.MoviesDatabase;
import ramzy.task.networks.RestClient;

public interface BasePresenter<V> {
    void attach(@NonNull V view, @NonNull RestClient.TMDBService service, @NonNull MoviesDatabase moviesDatabase);

    void detach();
}
