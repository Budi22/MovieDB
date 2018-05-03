package ramzy.task.intents;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import ramzy.task.activities.MainActivity;

public class MainScreenIntent extends Intent {

    public MainScreenIntent(@NonNull Context context) {
        super(context, MainActivity.class);
    }
}
