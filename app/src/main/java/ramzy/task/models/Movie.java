package ramzy.task.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ramzy.task.utils.Constants;
import ramzy.task.utils.StringUtils;

import static ramzy.task.models.Movie.FILTER.LATEST_MOVIES;
import static ramzy.task.models.Movie.FILTER.POPULAR_MOVIES;
import static ramzy.task.models.Movie.FILTER.SEARCH;
import static ramzy.task.models.Movie.FILTER.TOP_MOVIES;
import static ramzy.task.models.Movie.FILTER.TOP_RATED_MOVIES;
import static ramzy.task.models.Movie.FILTER.UPCOMING_MOVIES;

@Entity(tableName = "movie")
public class Movie {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({SEARCH, TOP_MOVIES, LATEST_MOVIES, POPULAR_MOVIES, TOP_RATED_MOVIES, UPCOMING_MOVIES})
    public @interface FILTER {
        String SEARCH = "search";
        String TOP_MOVIES = "top";
        String LATEST_MOVIES = "latest";
        String POPULAR_MOVIES = "popular";
        String TOP_RATED_MOVIES = "topRated";
        String UPCOMING_MOVIES = "upcoming";
    }

    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @NonNull
    private String mId;
    @ColumnInfo(name = "title")
    @SerializedName("title")
    private String mTitle;
    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    private String mOverView;
    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    private String mReleaseDate;
    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    private float mVote;
    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    private String mPostPath;
    @ColumnInfo(name = "has_details")
    private int mHasDetails;
    @ColumnInfo(name = "sort")
    private String mSort;

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return StringUtils.processHtmlString(mTitle);
    }

    public String getOverView() {
        return StringUtils.processHtmlString(mOverView);
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getPosterURL() {
        if (mPostPath == null) {
            return "";
        }
        return String.format("%s%s", Constants.POSTER_BASE_URL, mPostPath);
    }

    public float getVote() {
        return mVote;
    }

    public String getPostPath() {
        return mPostPath;
    }

    public int getHasDetails() {
        return mHasDetails;
    }

    public String getSort() {
        return mSort;
    }

    public void setSort(String sort) {
        this.mSort = sort;
    }

    public void setHasDetails(int hasDetails) {
        this.mHasDetails = hasDetails;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setOverView(String mOverView) {
        this.mOverView = mOverView;
    }

    public void setReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public void setVote(float mVote) {
        this.mVote = mVote;
    }

    public void setPostPath(String mPostPath) {
        this.mPostPath = mPostPath;
    }
}
