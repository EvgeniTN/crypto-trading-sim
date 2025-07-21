package com.evgeni.cryptotradingsim.dto;

public record CoinDto (
        String symbol,
        String name,
        String krakenSymbol,
        String coinGeckoId
){}
