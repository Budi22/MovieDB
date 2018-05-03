package ramzy.task.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import ramzy.task.R;
import ramzy.task.intents.MainScreenIntent;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startMoviesListActivity();
            }
        }, SPLASH_DELAY);
    }

    private void startMoviesListActivity() {
        startActivity(new MainScreenIntent(getApplicationContext()));
        finish();
    }
}
