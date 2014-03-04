package uk.ac.aber.dcs.wim2.studentbudgetapplication.widget;

import java.util.Calendar;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.EnterActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Detail;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.BalanceUtilities;

public class AppWidgetProvider extends android.appwidget.AppWidgetProvider {

    public static String INCREMENT_BUTTON = ".widget.INCREMENT_AMOUNT_BUTTON";
    public static String DECREMENT_BUTTON = ".widget.DECREMENT_AMOUNT_BUTTON";
    public static String SUBMIT_BUTTON = ".widget.SUBMIT_BUTTON";

    private static Float value = new Float(5);
    private SQLiteDatabaseHelper db;
    private static int dbSize = 0;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        db = new SQLiteDatabaseHelper(context);
        dbSize = db.getAllDetails().size();
        // Perform this loop procedure for each App Widget that belongs to this
        // provider
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_simple);

            appWidgetManager.updateAppWidget(appWidgetId, views);

            //setup increment and decrement buttons for onclick
            Intent increment = new Intent(INCREMENT_BUTTON);
            Intent decrement = new Intent(DECREMENT_BUTTON);

            PendingIntent pendingIncrement = PendingIntent.getBroadcast(context, 0,
                    increment, PendingIntent.FLAG_UPDATE_CURRENT);

            PendingIntent pendingDecrement = PendingIntent.getBroadcast(context, 0,
                    decrement, PendingIntent.FLAG_UPDATE_CURRENT);


            //setup the submit button for on click
            PendingIntent pendingSubmit;
            Intent submitIntent;
            if(dbSize==0){
                submitIntent = new Intent(context, EnterActivity.class);
                pendingSubmit = PendingIntent.getActivity(context, 0, submitIntent, 0);
            }
            else{
                submitIntent = new Intent(SUBMIT_BUTTON);
                pendingSubmit = PendingIntent.getBroadcast(context, 0,
                        submitIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                revalidateBalance(views);
            }

            views.setOnClickPendingIntent(R.id.widgetSubmit, pendingSubmit);
            views.setOnClickPendingIntent(R.id.widgetAddButton, pendingIncrement);
            views.setOnClickPendingIntent(R.id.widgetMinusButton, pendingDecrement);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private void revalidateBalance(RemoteViews views) {
        Detail detail = db.getAllDetails().get(0);
        BalanceUtilities.recalculateBalance(detail, db);
        views.setTextViewText(R.id.widgetRemainingWeekly, detail.getWeeklyBalance().toString());
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_simple);
        db = new SQLiteDatabaseHelper(context);
        if(INCREMENT_BUTTON.equals(intent.getAction())){
            value += (float)0.5;
            views.setTextViewText(R.id.widgetValueField, value+"");
        }
        else if(DECREMENT_BUTTON.equals(intent.getAction())){
            value -= (float)0.5;
            views.setTextViewText(R.id.widgetValueField, value+"");
        }
        else if(SUBMIT_BUTTON.equals(intent.getAction())){
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            String today = day+"/"+(month+1)+"/"+year;
            Transaction trans = new Transaction(value, "Micro transaction", "minus", "Micro Transaction", today);
            db.addTransaction(trans);
            revalidateBalance(views);
        }

        pushUpdate(context, views);

    }

    private void pushUpdate(Context context, RemoteViews views) {
        ComponentName widget =
                new ComponentName(context.getApplicationContext(), AppWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context.getApplicationContext());
        manager.updateAppWidget(widget, views);
    }
}



/**
 * code for launching an activity from the app
 */
// Create an Intent to launch ExampleActivity
//            Intent intent = new Intent(context, EnterActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);


/**
 * code for getting views by layout and setting on click listeners
 */
// Get the layout for the App Widget and attach an on-click listener
// to the button
//            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_simple);
//            views.setOnClickPendingIntent(R.id.button, pendingIntent);

/**
 * code to update
 */
// To update a label
//            views.setTextViewText(R.id.widget1label, df.format(new Date()));

// Tell the AppWidgetManager to perform an update on the current app
// widget
//            appWidgetManager.updateAppWidget(appWidgetId, views);


