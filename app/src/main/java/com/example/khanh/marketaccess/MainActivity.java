package com.example.khanh.marketaccess;

import java.util.Arrays;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    public final static String STOCK_SYMBOL = "com.example.khanh.marketaccess";
    private SharedPreferences stockSymbolsEntered;
    private EditText stockSymbolEditText;
    private  TableLayout stockTableScrollView;
    Button enterStockSymbolButton;
    Button deleteStockButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stockSymbolsEntered = getSharedPreferences("stockList", MODE_PRIVATE);

        stockTableScrollView = (TableLayout) findViewById(R.id.stockTableScrollView);
        stockSymbolEditText = (EditText) findViewById(R.id.stockSymbolEditText);
        enterStockSymbolButton = (Button) findViewById(R.id.enterStockSymbolButton);
        deleteStockButton = (Button) findViewById(findViewById(R.id.deleteStockButton);

        updateSaveStockList(null);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    private void updateSaveStockList(String newStockSymbol){
        String[] stocks = stockSymbolsEntered.getAll().keySet().toArray(new String[0]);
        Arrays.sort(stocks, String.CASE_INSENSITIVE_ORDER);
        if(newStockSymbol != null){
            insertStockInScrollView(newStockSymbol, Arrays.binarySearch(stocks, newStockSymbol));
        }else {
            for(int i = 0; i < stocks.length; i++){
                insertStockInScrollView(stocks[i], i);
            }
        }
    }

    private void saveStockSymbol(String newStock){
        String isTheStockNew = stockSymbolsEntered.getString(newStock, null);

        SharedPreferences.Editor preferencesEditor = stockSymbolsEntered.edit();
        preferencesEditor.putString(newStock, newStock);
        preferencesEditor.apply();

        if(isTheStockNew ==null){
            updateSaveStockList(newStock);
        }
    }

    private void insertStockInScrollView(String stock, int array_index){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View newStockRow = inflater.inflate(R.layout.stock_quote_row, null);

        TextView newStockTextView = (TextView) newStockRow.findViewById(R.id.stockSymbolTextView);
        newStockTextView.setText(stock);
        // button listener for scrolls
        Button stock_quote_button = (Button) newStockRow.findViewById(R.id.stockQuoteButton);
        stock_quote_button.setOnClickListener(getStockActivityListener);
        /**
        Button quote_from_web_button = (Button) new_stock_row.findViewById(R.id.quote_from_web_button);
         quote_from_web_button.setOnClickListener(getStockFromWebsiteListener);
         **/
    }
    public OnClickListener enterStockButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(stockSymbolEditText.getText().length() > 0){
                saveStockSymbol(stockSymbolEditText.getText().toString());
                stockSymbolEditText.setText("");
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(stockSymbolEditText.getWindowToken(), 0);
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle(R.string.invalid_stock_symbol);
                builder.setPositiveButton(R.string.ok, null);
                builder.setMessage(R.string.missing_stock_symbol);
                AlertDialog displayAlertDialog = builder.create();
                displayAlertDialog.show();

            }

        }
    };
    private void deleteAllStocks(){
        stockTableScrollView.removeAllViews();
    }
    public OnClickListener deleteStocksButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            deleteAllStocks();
            SharedPreferences.Editor preferencesEditor = stockSymbolsEntered.edit();
            preferencesEditor.clear();
            preferencesEditor.apply();
        }
    };
    public OnClickListener getStockActivityListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            TableRow tableRow = (TableRow) v.getParent();
            TextView stockTextView  = (TextView) tableRow.findViewById(R.id.stockSymbolTextView);
            String local_stock_symbol = stockTextView.getText().toString();

            Intent intent = new Intent(MainActivity.this, StockInfoActivity.class);
            intent.putExtra(STOCK_SYMBOL, local_stock_symbol);
            startActivity(intent);
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
