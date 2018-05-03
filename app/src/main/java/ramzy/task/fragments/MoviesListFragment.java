package ramzy.task.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import ramzy.task.R;
import ramzy.task.adapters.MoviesAdapter;
import ramzy.task.contracts.MoviesListContract;
import ramzy.task.models.Movie;
import ramzy.task.presenters.MoviesListPresenter;
import ramzy.task.utils.RecyclerItemClickListener;
import ramzy.task.utils.RecyclerViewMargin;

public class MoviesListFragment extends BaseServiceFragment implements MoviesListContract.MoviesListView {

    private static final String ARGS_SEARCH_QUERY = "searchQuery";
    private static final int LIST_COLUMNS_NUMBER = 2;

    @BindView(R.id.progress_bar_loading)
    ProgressBar mLoadingBar;
    @BindView(R.id.cards_recycler_view)
    RecyclerView mRecyclerView;
    @BindDimen(R.dimen.distance_between_recycler_view_items)
    int mRecyclerMargin;
    private @Movie.FILTER String mFilter;
    private MoviesListPresenter mPresenter;

    public RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
            if (isLastItemDisplaying() && !mLoadingBar.isShown()) {
                mPresenter.nextPage();
            }
        }

        private boolean isLastItemDisplaying() {
            if (mRecyclerView.getAdapter().getItemCount() != 0) {
                final StaggeredGridLayoutManager layoutManager = ((StaggeredGridLayoutManager) mRecyclerView.getLayoutManager());
                final int visibleItemCount = layoutManager.getChildCount();
                final int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = 0;
                int[] firstVisibleItems = layoutManager.findFirstVisibleItemPositions(null);
                if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                    firstVisibleItemPosition = firstVisibleItems[0];
                }
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0)
                    return true;
            }
            return false;
        }
    };

    public static MoviesListFragment newInstance(@Nullable String searchQuery) {
        MoviesListFragment moviesListFragment = new MoviesListFragment();
        if (searchQuery != null && !searchQuery.isEmpty()) {
            final Bundle data = new Bundle();
            data.putString(ARGS_SEARCH_QUERY, searchQuery);
            moviesListFragment.setArguments(data);
        }
        return moviesListFragment;
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_movies_list, container, false);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        mPresenter = new MoviesListPresenter();
        mPresenter.attach(this, getService(), getMoviesDatabase());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkForSearchQuery();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detach();
    }

    private void setupRecyclerView() {
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(LIST_COLUMNS_NUMBER, OrientationHelper.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(new MoviesAdapter());
        mRecyclerView.addOnScrollListener(mScrollListener);
        final RecyclerViewMargin decoration = new RecyclerViewMargin(mRecyclerMargin, LIST_COLUMNS_NUMBER);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity()
                , mRecyclerView
                , new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final Movie movie = ((MoviesAdapter) mRecyclerView.getAdapter()).getMovies().get(position);
                openMovieDetails(movie);
            }

            @Override
            public void onLongItemClick(View view, int position) {}
        }));
    }

    private void openMovieDetails(@NonNull Movie movie) {
        final String movieId = movie.getId();
        addFragment(MovieDetailsFragment.newInstance(movieId), false);
    }

    private void checkForSearchQuery() {
        final Bundle data = getArguments();
        if (data != null) {
            final String query = data.getString(ARGS_SEARCH_QUERY, "");
            if (query != null && !query.isEmpty()) {
                setQueryText(query);
            }
        } else {
            mPresenter.getMoviesBy(mFilter != null ? mFilter : Movie.FILTER.TOP_MOVIES);
        }
    }

    private void scrollToTop() {
        if (mRecyclerView != null) {
            mRecyclerView.scrollToPosition(0);
        }
    }

    @Override
    public void showByFilter(@NonNull @Movie.FILTER String filter) {
        mFilter = filter;
        if (mPresenter != null) {
            mPresenter.getMoviesBy(mFilter);
        }
    }

    @Override
    public void setQueryText(@NonNull final String query) {
        if (mPresenter != null) {
            scrollToTop();
            mPresenter.setQueryText(query);
            showByFilter(Movie.FILTER.SEARCH);
        }
    }

    @Override
    public void updateLoading(boolean shouldShow) {
        mLoadingBar.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void updateMovies(@NonNull List<Movie> movies, boolean clear) {
        if (mRecyclerView!= null) {
            ((MoviesAdapter) mRecyclerView.getAdapter()).updateMovies(movies, clear);
        }
    }

    @Override
    public void showConnectionError() {
        addFragment(NoInternetFragment.newInstance(), true);
    }

    @Override
    public void showNoResults() {
        addFragment(NoResultsFragment.newInstance(), true);
    }

    private void addFragment(@NonNull Fragment fragment, boolean replace) {
        final FragmentActivity activity = getActivity();
        if (activity != null) {
            FragmentManager manager = getFragmentManager();
            if (manager == null && isAdded()) {
                manager = activity.getSupportFragmentManager();
            }
            if (manager != null) {
                FragmentTransaction transaction = manager.beginTransaction();
                if (replace) {
                    transaction.replace(R.id.container, fragment);
                } else {
                    transaction.add(R.id.container, fragment);
                    transaction.addToBackStack(null);
                }
                transaction.commit();
            }
        }
    }
}
