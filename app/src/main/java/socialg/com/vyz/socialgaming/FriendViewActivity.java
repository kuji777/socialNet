package socialg.com.vyz.socialgaming;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.List;

import socialg.com.vyz.socialgaming.bean.Friend;
import socialg.com.vyz.socialgaming.connection.UserInfo;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link FriendViewActivityConfigureActivity FriendViewActivityConfigureActivity}
 */
public class FriendViewActivity extends AppWidgetProvider {


    private static final String MyOnClick = "myOnClickTag";

    static void updateAppWidget(final Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = FriendViewActivityConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.friend_view_activity);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        List<Friend> friendList = UserInfo.getInstance().getFriendList();
        for (Friend friend : friendList ) {

//            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            final LinearLayout childView = (LinearLayout) layoutInflater.inflate(R.layout.single_profile, null);
            RemoteViews childView = new RemoteViews(context.getPackageName(), R.layout.single_profile);
            childView.setTextViewText(R.id.single_view_title,friend.getPseudo());

            final String id = friend.getId_user();
//            childView.setOnClickPendingIntent();
            views.addView(R.id.widget_friend_container,childView);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            FriendViewActivityConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}

