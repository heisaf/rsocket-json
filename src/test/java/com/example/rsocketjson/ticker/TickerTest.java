package com.example.rsocketjson.ticker;

import com.example.rsocketjson.model.StockFilter;
import com.example.rsocketjson.model.StockTicker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TickerTest {

    @Test
    public void testFilter() {
        final var predicate = Ticker.createSymbolFilter(StockFilter.builder()
                .matchSymbol("a")
                .build());
        assertTrue(predicate.test(StockTicker.builder().symbol("ADS").build()));
        assertTrue(Ticker.createTicks().allMatch(predicate));
    }
}
