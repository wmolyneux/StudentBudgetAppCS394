package uk.ac.aber.dcs.wim2.studentbudgetapplication.widget;

import java.util.Calendar;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.DetailActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.EnterActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Detail;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.BalanceUtilities;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.FragmentUtilities;

/**
 * This class contains the functionality for the home screen widget
 *
 * @author wim2
 * @version 1.0
 */
public class AppWidgetProvider extends android.appwidget.AppWidgetProvider {

    public static String INCREMENT_BUTTON = ".widget.INCREMENT_AMOUNT_BUTTON";
    public static String DECREMENT_BUTTON = ".widget.DECREMENT_AMOUNT_BUTTON";
    public static String SUBMIT_BUTTON = ".widget.SUBMIT_BUTTON";

    private static Float value = new Float(1.5);
    private SQLiteDatabaseHelper db;
    private static int dbSize = 0;
    private static Context context;
    private static String currency;

    /**
     * Updates the widget with new values from the application
     *
     * @param context - context
     * @param appWidgetManager - widget manager
     * @param appWidgetIds - widget id
     */
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        db = new SQLiteDatabaseHelper(context);
        dbSize = db.getAllDetails().size();
        this.context = context;
        currency = FragmentUtilities.getCurrency(context);
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
            PendingIntent pendingSubmit = setupSubmitButton(context, views);

            //setup the image button for on click
            PendingIntent pendingApp = setupAppButton(context, views);

            views.setOnClickPendingIntent(R.id.widgetSubmit, pendingSubmit);
            views.setOnClickPendingIntent(R.id.logoWidgetButton, pendingApp);
            views.setOnClickPendingIntent(R.id.widgetAddButton, pendingIncrement);
            views.setOnClickPendingIntent(R.id.widgetMinusButton, pendingDecrement);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    /**
     * setup the submit button ready for seting a broadcast to the application when clicked and updating the balance
     *
     * @param context - context
     * @param views - submit button
     *
     * @return - pending intent
     */
    private PendingIntent setupSubmitButton(Context context, RemoteViews views) {
        PendingIntent pendingSubmit;
        Intent submitIntent;

        //if the database doesnt exist, open the application
        if(dbSize==0){
            submitIntent = new Intent(context, EnterActivity.class);
            pendingSubmit = PendingIntent.getActivity(context, 0, submitIntent, 0);
        }

        //else submit the micro transaction and update the balance
        else{
            submitIntent = new Intent(SUBMIT_BUTTON);
            pendingSubmit = PendingIntent.getBroadcast(context, 0,
                    submitIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            validateBalance(views);
        }
        return pendingSubmit;
    }

    /**
     * setup the app icon button to open the application
     *
     * @param context - context
     * @param views - submit button
     *
     * @return - pending intent
     */
    private PendingIntent setupAppButton(Context context, RemoteViews views){
        PendingIntent pendingApp;
        Intent appIntent;
        //if the database is empty, open the enter screen to begin initial budget setup
        if(dbSize==0){
            appIntent = new Intent(context, EnterActivity.class);
        }

        //else open the home screen of the application
        else{
            appIntent = new Intent(context, DetailActivity.class);
        }
        pendingApp = PendingIntent.getActivity(context, 0, appIntent, 0);
        return pendingApp;
    }

    /**
     * Recalculates balance when a transaction has occured and displays on the widget
     *
     * @param views - views to be updated
     */
    private void validateBalance(RemoteViews views) {
        Detail detail = db.getAllDetails().get(0);
        BalanceUtilities.recalculateBalance(detail, db);
        views.setTextViewText(R.id.widgetRemainingWeekly,
                context.getString(R.string.widget_text)+" "+currency+BalanceUtilities.getValueAs2dpString(detail.getWeeklyBalance()));
        views.setTextViewText(R.id.widgetValueField, currency+"-"+BalanceUtilities.getValueAs2dpString(value));
    }


    /**
     * Called when a broadcast is recieved
     * @param context - context
     * @param intent - intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_simple);
        db = new SQLiteDatabaseHelper(context);

        //if the increment button is clicked
        if(INCREMENT_BUTTON.equals(intent.getAction())){
            //increase the value by 0.5, but no higher than 3
            if(value<3){
                value += (float)0.5;
                views.setTextViewText(R.id.widgetValueField, currency+"-"+BalanceUtilities.getValueAs2dpString(value));
            }
        }
        //if the decrement button is clicked
        else if(DECREMENT_BUTTON.equals(intent.getAction())){
            //decrement the value by 0.5 but not lower than 0.5
            if(value>0.5){
                value -= (float)0.5;
                views.setTextViewText(R.id.widgetValueField, currency+"-"+BalanceUtilities.getValueAs2dpString(value));
            }
        }
        //if the submit button is clicked, submit the transaction with today's date as a micro transaction
        //and revalidate the balance
        else if(SUBMIT_BUTTON.equals(intent.getAction())){
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            String today = day+"/"+(month+1)+"/"+year;
            Transaction trans = new Transaction(value, "Micro transaction", "minus", 8, today);
            db.addTransaction(trans);
            validateBalance(views);
        }

        //update the widget
        pushUpdate(context, views);

    }

    /**
     * Updates the widgets views calling onUpdate()
     *
     * @param context - context
     * @param views - views to be updated
     */
    private void pushUpdate(Context context, RemoteViews views) {
        ComponentName widget =
                new ComponentName(context.getApplicationContext(), AppWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context.getApplicationContext());
        manager.updateAppWidget(widget, views);
    }
}



