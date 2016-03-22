package com.example.khanh.marketaccess;

/**
 * Created by khanh on 3/21/2016.
 */
public class StockInfo {
    private String name = "", yearLow = "", yearHigh = "", daysLow = "", daysHigh = "", change = "";
    private String averageDailyVolume = "", marketCap = "", daysRange = "", lastTradePrce = "";
    private String stockExchange = "";

    public StockInfo(String name, String yearLow, String yearHigh, String daysLow, String daysHigh,
                     String change, String averageDailyVolume, String marketCap, String daysRange,
                     String lastTradePrce, String stockExchange) {
        this.name = name;
        this.yearLow = yearLow;
        this.yearHigh = yearHigh;
        this.daysLow = daysLow;
        this.daysHigh = daysHigh;
        this.change = change;
        this.averageDailyVolume = averageDailyVolume;
        this.marketCap = marketCap;
        this.daysRange = daysRange;
        this.lastTradePrce = lastTradePrce;
        this.stockExchange = stockExchange;
    }

    public String getName() {
        return name;
    }

    public String getYearLow() {
        return yearLow;
    }

    public String getYearHigh() {
        return yearHigh;
    }

    public String getDaysLow() {
        return daysLow;
    }

    public String getDaysHigh() {
        return daysHigh;
    }

    public String getChange() {
        return change;
    }

    public String getAverageDailyVolume() {
        return averageDailyVolume;
    }

    public String getMarketCap() {
        return marketCap;
    }

    public String getDaysRange() {
        return daysRange;
    }

    public String getLastTradePrce() {
        return lastTradePrce;
    }

    public String getStockExchange() {
        return stockExchange;
    }
}
