package com.sam_chordas.android.stockhawk.ui;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.sam_chordas.android.stockhawk.R;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (lineset != null)
        {

        }

    }

    private LineChartView lineChartView;
     private String stockHistory;
    private LineSet lineset;
    private TextView stockName;
    DateFormatSymbols dateformate = new DateFormatSymbols();
    String[] month  = new String[14];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_main, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();
            stockHistory = bundle.getString("STOCK");
        Log.v("STOCK",bundle.getString("STOCK"));

//
        lineChartView = (LineChartView) view.findViewById(R.id.linechart);
        stockName = (TextView) view.findViewById(R.id.stockName);
        stockName.setText(stockHistory);
        month = dateformate.getShortMonths();

        new QuotesTask().execute(stockHistory);

        return view;
    }

    public class QuotesTask extends AsyncTask<String ,Void,List<HistoricalQuote>>
    {

        @Override
        protected List<HistoricalQuote> doInBackground(String... params) {

            String stockString = params[0];
            List<HistoricalQuote> stockHistQuotes = null;

            try {
                Stock stock = YahooFinance.get(stockString);
                Calendar from = Calendar.getInstance();
                Calendar to = Calendar.getInstance();
                from.add(Calendar.YEAR ,-1);
                 stockHistQuotes = stock.getHistory(from, to, Interval.DAILY);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stockHistQuotes;
        }

        @Override
        protected void onPostExecute(List<HistoricalQuote> historicalQuotes) {
            super.onPostExecute(historicalQuotes);
            lineset = new LineSet();
            int i = 0;

            List<HistoricalQuote> historic = historicalQuotes;
            Collections.reverse(historic);
            for (HistoricalQuote historicalQoute : historic)
            {


                i++;
                if(i%20 == 0 || i == 1)
                {
                    int m = historicalQoute.getDate().get(Calendar.MONTH);
                    String mon = month[m];

                    lineset.addPoint(mon,historicalQoute.getClose().floatValue());
                }
                else
                {
                    lineset.addPoint("",historicalQoute.getClose().floatValue());
                }

            }

           lineChartView.addData(lineset);
            lineChartView.show();
        }
    }





}
