package com.example.rsocketjson.ticker;

import com.example.rsocketjson.model.StockFilter;
import com.example.rsocketjson.model.StockTicker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Controller
@Slf4j
public class Ticker {

    private static final List<String> SYMBOLS = List.of("ADS", "AIR", "ALV");

    static Stream<StockTicker> createTicks() {
        return SYMBOLS.stream().map(symbol -> StockTicker.builder()
                .symbol(symbol)
                .time(LocalDateTime.now())
                // TODO generate random value
                .price(BigDecimal.TEN)
                .build());
    }

    private Flux<StockTicker> generateTicks() {
        return Flux.fromStream(createTicks());
    }

    static Predicate<StockTicker> createSymbolFilter(final StockFilter filter) {
        final var symbolFilter = filter.getMatchSymbol()
                .trim()
                .replaceAll("\"", "")
                .toUpperCase();
        return ticker -> {
            log.info("filter: {} symbol: {}", symbolFilter, ticker.getSymbol());
            final var res = ticker.getSymbol().contains(symbolFilter);
            log.info("res {}", res);
            return res;
        };
    }

    @MessageMapping("stocks")
    public Flux<StockTicker> stocks(StockFilter filter) {
        return Flux.interval(Duration.ofSeconds(2))
                .switchMap(e -> generateTicks())
                .log()
                .filter(createSymbolFilter(filter))
                .log();
    }
}
