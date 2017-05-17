package com.mfolivas.finance;

/**
 * Stock information to store the ticker and the amount.
 */
public class StockInfo {
    private String symbol;
    private double price;

    public StockInfo(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    @Override
    public String toString() {
        return symbol + ":" + price;
    }
}