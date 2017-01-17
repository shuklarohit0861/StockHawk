package com.sam_chordas.android.stockhawk.service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

/**
 * Created by thero on 14-01-2017.
 */

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {

    private Context context = null;
    private int appWidgetId;
    private Cursor StockCursor;
    private Boolean isValidate;
    private int rowID;

    public ListProvider (Context context , Intent intent)
    {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
            StockCursor = context.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                    new String[]{ QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                            QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                    QuoteColumns.ISCURRENT + " = ?",
                    new String[]{"1"},
                    null);

        isValidate = StockCursor != null;
        rowID = isValidate ? StockCursor.getColumnIndex("_id") : -1;
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {

        if(isValidate && StockCursor != null)
        return StockCursor.getCount();
        else
            return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.list_item_quote);

        if(StockCursor.moveToPosition(position)) {

            remoteViews.setTextViewText(R.id.stock_symbol, StockCursor.getString(1));
            remoteViews.setTextViewText(R.id.bid_price,StockCursor.getString(2));
            remoteViews.setTextViewText(R.id.change,StockCursor.getString(3));
            return remoteViews;
        }
        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public long getItemId(int position) {
        if(isValidate && StockCursor != null && StockCursor.moveToPosition(position))
        return StockCursor.getLong(rowID);
        else
            return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


}
