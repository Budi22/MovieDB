package ramzy.task.utils;

import android.support.annotation.NonNull;
import android.text.Html;

public class StringUtils {

    private StringUtils() {}

    public static String processHtmlString(@NonNull final String htmlString) {
        final String processedString;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            processedString = Html.fromHtml(htmlString, Html.FROM_HTML_MODE_COMPACT).toString();
        } else {
            processedString = Html.fromHtml(htmlString).toString();
        }
        return processedString;
    }
}
