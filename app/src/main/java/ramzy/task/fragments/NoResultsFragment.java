package ramzy.task.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ramzy.task.R;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NoResultsFragment extends Fragment {

    @BindView(R.id.no_results_found_text)
    TextView mNoResultsTextView;
    @BindString(R.string.no_results_found)
    String mNoResultsText;

    public static NoResultsFragment newInstance() {
        return new NoResultsFragment();
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_no_results_found, container, false);
        ButterKnife.bind(this, view);
        mNoResultsTextView.setText(mNoResultsText);
        return view;
    }
}
