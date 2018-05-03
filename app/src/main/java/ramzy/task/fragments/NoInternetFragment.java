package ramzy.task.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ramzy.task.R;

public class NoInternetFragment extends Fragment {

    @BindView(R.id.retry_text)
    TextView mRetryText;
    private RetryClickListener mCallback;

    public static NoInternetFragment newInstance() {
        return new NoInternetFragment();
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_no_internet, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        try {
            mCallback = (RetryClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement RetryClickListener");
        }
    }

    @OnClick(R.id.retry_text)
    void retry() {
        final Activity activity = getActivity();
        if (activity != null) {
            mCallback.retryConnection();
        }
    }

    public interface RetryClickListener {
        void retryConnection();
    }
}
