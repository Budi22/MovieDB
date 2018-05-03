package ramzy.task.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import javax.inject.Inject;

import ramzy.task.database.MoviesDatabase;
import ramzy.task.deps.DaggerTMBDComponent;
import ramzy.task.deps.TMBDComponent;
import ramzy.task.deps.TMBDModule;
import ramzy.task.networks.RestClient;

public abstract class BaseServiceFragment extends Fragment {

    @Inject
    RestClient.TMDBService mService;
    @Inject
    MoviesDatabase mMoviesDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TMBDComponent component = DaggerTMBDComponent.builder().tMBDModule(new TMBDModule(getActivity())).build();
        component.inject(this);
    }

    public RestClient.TMDBService getService() {
        return mService;
    }

    public MoviesDatabase getMoviesDatabase() {
        return mMoviesDatabase;
    }
}
