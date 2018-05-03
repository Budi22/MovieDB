package ramzy.task.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import ramzy.task.R;
import ramzy.task.contracts.MoviesListContract;
import ramzy.task.fragments.MoviesListFragment;
import ramzy.task.fragments.NoInternetFragment;
import ramzy.task.fragments.NoResultsFragment;
import ramzy.task.models.Movie;
import ramzy.task.utils.MainActivityUpdater;

public class MainActivity extends AppCompatActivity implements NoInternetFragment.RetryClickListener
        , NavigationView.OnNavigationItemSelectedListener, MainActivityUpdater {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    private SearchView mSearchView;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupActionBarAndMenu();
        openMoviesListFragment();
    }

    private void setupActionBarAndMenu() {
        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout
                , mToolbar
                , R.string.menu_open
                , R.string.menu_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                final int stackCount = getSupportFragmentManager().getBackStackEntryCount();
                if (stackCount == 0) {
                    updateMenuIcon(false);
                }
            }
        });

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mNavigationView.setCheckedItem(R.id.menu_top);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void updateMenuIcon(boolean showBackButton) {
        final ActionBar actionBar = getSupportActionBar();
        if(showBackButton) {
            if (actionBar != null) {
                mDrawerToggle.setDrawerIndicatorEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(true);
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }

        } else {
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(false);
                mDrawerToggle.setDrawerIndicatorEnabled(true);
                mDrawerToggle.setToolbarNavigationClickListener(null);
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        }
    }

    @Override
    public void goBack() {
        onBackPressed();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                final Fragment currentFragment = getCurrentFragment();
                if (currentFragment != null && currentFragment instanceof MoviesListContract.MoviesListView) {
                    ((MoviesListContract.MoviesListView) currentFragment).setQueryText(newText);
                } else if (currentFragment instanceof NoResultsFragment) {
                    openMoviesListFragment();
                }
                return false;
            }
        });
        return true;
    }

    @Nullable
    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.container);
    }

    @Override
    public void retryConnection() {
        openMoviesListFragment();
    }

    private MoviesListFragment openMoviesListFragment() {
        final MoviesListFragment moviesListFragment = MoviesListFragment.newInstance(mSearchView != null
                ? mSearchView.getQuery().toString()
                : null);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, moviesListFragment)
                .commit();
        return moviesListFragment;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        String filter = null;
        if (id == R.id.menu_top) {
            filter = Movie.FILTER.TOP_MOVIES;
        } else if (id == R.id.menu_latest) {
            filter = Movie.FILTER.LATEST_MOVIES;
        } else if (id == R.id.menu_popular) {
            filter = Movie.FILTER.POPULAR_MOVIES;
        } else if (id == R.id.menu_top_rated) {
            filter = Movie.FILTER.TOP_RATED_MOVIES;
        } else if (id == R.id.menu_upcoming) {
            filter = Movie.FILTER.UPCOMING_MOVIES;
        }
        if (filter != null) {
            Fragment currentFragment = getCurrentFragment();
            if (currentFragment == null || !(currentFragment instanceof MoviesListContract.MoviesListView)) {
                currentFragment = openMoviesListFragment();
            }
            ((MoviesListContract.MoviesListView) currentFragment).showByFilter(filter);
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
