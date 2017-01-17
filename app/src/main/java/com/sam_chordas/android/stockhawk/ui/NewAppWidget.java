package com.sam_chordas.android.stockhawk.ui;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.service.WidgetService;


/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        Intent intentW = new Intent(context, WidgetService.class);

        intentW.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);

        intentW.setData(Uri.parse(
                intentW.toUri(Intent.URI_INTENT_SCHEME)));

        views.setRemoteAdapter(appWidgetId,R.id.widgetListView,intentW);
        views.setEmptyView(R.id.widgetListView,R.id.emptyView);

//        Intent click = new Intent(context,MyStocksActivity.class);
//
//        PendingIntent clickPI=PendingIntent
//                .getActivity(context, 0,
//                        click,
//                        PendingIntent.FLAG_UPDATE_CURRENT);
//
//        views.setOnClickPendingIntent(R.id.widgetListView,clickPI);

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
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

