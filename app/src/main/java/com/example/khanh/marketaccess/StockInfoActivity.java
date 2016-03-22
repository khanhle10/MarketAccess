package com.example.khanh.marketaccess;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.HttpResponse;
import org.apache.http.client
import org.apache.http.client.ClientProtocolException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;



/**
 * Created by khanh on 3/21/2016.
 */
public class StockInfoActivity extends Activity{

    private static final String TAG = "STOCKQUOTE";
    TextView companyNameTV, yearLowTV, yearHighTV, dayLowTV, dayHighTV,lastTradePriceTV;
    TextView dailyChangeTV, dailyRangeTV, dailyAverageVolume, marketCap, stockExchangeName;
    TextView volume;
    //XML nde keys
    static final String KEY_QUOTE = "quote;", KEY_NAME = "Name", KEY_DAILY_AVE_VOL = "AverageDailyVolume";
    static final String KEY_CHANGE = "Change", KEY_DAYS_LOW = "DaysLow", KEY_DAYS_HIGH = "DaysHigh";
    static final String KEY_YEAR_LOW = "YearLow", KEY_YEAR_HIGH = "YearHigh";
    static final String KEY_MARKET_CAP = "MarketCapitalization";
    static final String KEY_LAST_TRACE_PRICE = "LastTradePriceOnly";
    static final String KEY_DAYS_RANGE = "DaysRange", KEY_VOLUME = "Volume";
    static final String KEY_STOCK_EXCHANGE = "StockExchange";

    //XML Data retrieved

    String name = "", yearLow = "", yearHigh = "", daysLow = "", daysHigh = "", change = "";
    String averageDailyVolume = "", marketCap = "", daysRange = "", lastTradePrce = "";
    String stockExchange = "";
    String yahooURLQueryOne = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20" +
            "yahoo.finance.quote%20where%20symbol%20in%20(%22";
    String yahooURLQueryTwo =  "%22)&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    String [][] pullXMLParserArray = {{"AverageDailyVolume", "0"},{"Change","0"},{"DaysLow","0"},
            {"DaysHigh","0"},{"YearLow","0"},{"YearHigh","0"},{"MarketCapitalization","0"},
            {"LastTradePriceOnly","0"},{"DaysRange","0"},{"Name","0"},{"Symbol","0"},{"Volume","0"},
            {"StockExchange","0"},};
    private int parserArrayIncrement = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stock_info);

        Intent intent = getIntent();
        String stockSymbol = intent.getStringExtra(MainActivity.STOCK_SYMBOL);

        companyNameTV = (TextView) findViewById(R.id.companyNameTextView);
        yearLowTV = (TextView) findViewById(R.id.yearLowTextView);
        yearHighTV = (TextView) findViewById(R.id.yearHighTextView);
        dayLowTV = (TextView) findViewById(R.id.dailyLowTextView);
        dayHighTV = (TextView) findViewById(R.id.dailyHighTextView);
        lastTradePriceTV = (TextView) findViewById(R.id.lastTradePriceTextView);
        dailyChangeTV = (TextView) findViewById(R.id.changeTextView);
        dailyRangeTV =(TextView) findViewById(R.id.dailyRangeTextView);
        dailyAverageVolume = (TextView) findViewById(R.id.dailyAveVolumeTextView);
        marketCap = (TextView) findViewById(R.id.marketCapTextView stockExchangeName);
        volume = (TextView) findViewById(R.id.totalVolumeTextView);
        Log.d(TAG, "Before Accessing URL For " +stockSymbol);

        final String yqlURL = yahooURLQueryOne + stockSymbol + yahooURLQueryTwo;

        new MyAsyncTask().execute(yqlURL);
    }

    private class MyAsyncTask extends AsyncTask<String, String, String>{

        protected String doInBackground(String... args)throws IOException, MalformedURLException,
        ParserConfigurationException, SAXException, ClientProtocolException{
            try {
                URL url = new URL(args[0]);
                URLConnection connection;
                connection = url.openConnection();

                HttpURLConnection httpConnection = (HttpURLConnection) connection;

                int respondeCode = httpConnection.getResponseCode();

                if(respondeCode == HttpURLConnection.HTTP_OK){
                    InputStream input = httpConnection.getInputStream();
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document dom = db.parse(input);
                    Element docElem = dom.getDocumentElement();
                    NodeList nodeList = docElem.getElementsByTagName("quote");
                    if(nodeList != null && nodeList.getLength() > 0){
                        for(int i = 0; i < nodeList.getLength();i++){
                            StockInfo stock = getStockInfo(docElem);
                            daysLow = stock.getDaysLow();
                            daysHigh = stock.getDaysHigh();
                            yearLow = stock.getYearLow();
                            yearHigh = stock.getYearHigh();
                            name = stock.getName();
                            change = stock.getChange();
                            averageDailyVolume = stock.getAverageDailyVolume();
                            marketCap = stock.getMarketCap();
                            daysRange = stock.getDaysRange();
                            lastTradePrce = stock.getLastTradePrce();
                            stockExchange = stock.getStockExchange();
                        }
                    }
                } // end of if check statement
            }catch(MalformedURLException ex){
                Log.d(TAG, "MalformException", ex);
            }catch (IOException ex){
                Log.d(TAG,"IOException" , ex);
            }catch (ParserConfigurationException ex){
                Log.d(TAG,"ParserConfigurationException" , ex);
            }catch (SAXException ex){
                Log.d(TAG,"SAXException" , ex);
            }finally{
                System.out.println("No Exception found");
            }
            try{
                Log.d(TAG, "Test pullXMLParser");

                XmlPullParserFactory xmlFactory = XmlPullParserFactory.newInstance();
                xmlFactory.setNamespaceAware(true);
                XmlPullParser parser = xmlFactory.newPullParser();

                parser.setInput(new InputStreamReader(getUrlData(args[0])));
                beginDocument(parser,"query");
                int eventType = parser.getEventType();

                do{
                    nextElement(parser);
                    parser.next();
                    eventType = parser.getEventType();
                    if(eventType == XmlPullParser.TEXT){
                        String valueFromXML = parser.getText();
                        pullXMLParserArray[parserArrayIncrement][1] = valueFromXML;
                        Log.d("Test", "Value: "+pullXMLParserArray[parserArrayIncrement++][1])
                    } // end if
                }while(eventType != XmlPullParser.END_DOCUMENT);
            }catch (ClientProtocolException ex){
                ex.printStackTrace;
            }catch (URISyntaxException ex){
                ex.printStackTrace();
            }catch(IOException ex){
                ex.printStackTrace();
            }finally {

            }
            for(int i = 0; i < pullXMLParserArray.length; i++){
                Log.d("test", pullXMLParserArray[i][0] + " : "  + pullXMLParserArray[i][1]);
            }
            return null;
        }
        public InputStream getUrlData(String url) throws URISyntaxException,
                ClientProtocolException, IOException{
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet method = new HttpGet(new URI(url));
            HttpResponse httpResp = client.execute(method);
            public final void beginDocument(XmlPullParser)
        }
    }

}
