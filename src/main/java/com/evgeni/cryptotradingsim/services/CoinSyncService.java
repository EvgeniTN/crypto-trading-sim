package com.evgeni.cryptotradingsim.services;

import com.evgeni.cryptotradingsim.dto.CoinDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoinSyncService {
    private final RestTemplate restTemplate = new RestTemplate();

    private final Map<String, String> krakenAliasMap = Map.of(
            "btc", "XBT",
            "eth", "ETH",
            "doge", "XDG"
    );

    private volatile List<CoinDto> cachedTop20 = List.of();

    public List<CoinDto> getTop20Coins() {
        return cachedTop20;
    }

//    @PostConstruct
    @Scheduled(fixedRate = 15 * 60 * 1000) // 15 minutes
    public void syncTop20Coins() {
        try {
            List<CoinDto> topCoins = fetchTop20Coins();
            this.cachedTop20 = topCoins;
            System.out.println("Top 20 coins synced successfully: " + topCoins.size());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private List<CoinDto> fetchTop20Coins() {
        String cgUrl = "https://api.coingecko.com/api/v3/coins/markets"+"" +
                "?vs_currency=usd&order=market_cap_desc&per_page=30&page=1&sparkline=false";

        List<Map<String, Object>> cgList = Arrays.asList(restTemplate.getForObject(cgUrl, Map[].class));
        System.out.println("[DEBUG] CoinGecko Top List fetched: " + cgList.size());

        Map<String, Object> krakenResponse = restTemplate.getForObject(
                "https://api.kraken.com/0/public/AssetPairs", Map.class);

        Map<String, Map<String, Object>> krakenPairs
                = (Map<String, Map<String, Object>>) krakenResponse.get("result");
        System.out.println("[DEBUG] Kraken AssetPairs found: " + krakenPairs.size());

        Set<String> wsPairs = krakenPairs.values().stream()
                .map(p -> (String) p.get("wsname"))
                .filter(Objects::nonNull)
                .filter(name -> name != null && name.endsWith("/USD"))
                .collect(Collectors.toSet());
        System.out.println("[DEBUG] Kraken USD pairs: " + wsPairs.size());
        System.out.println("[DEBUG] First 5 Kraken USD pairs: " +
                wsPairs.stream().limit(5).collect(Collectors.toList()));


        List<CoinDto> result = new ArrayList<>(20);

        for (Map<String, Object> coin : cgList) {
            String id = (String) coin.get("id");
            String name = (String) coin.get("name");
            String symbol = ((String) coin.get("symbol")).toLowerCase();
            String mapped = krakenAliasMap.getOrDefault(symbol, symbol).toUpperCase();
            String krakenSymbol = mapped + "/USD";

            System.out.println("[DEBUG] Trying symbol: " + symbol + " → Kraken pair: " + krakenSymbol);

            if (wsPairs.contains(krakenSymbol)) {
                System.out.println("[MATCH] Found valid pair: " + krakenSymbol + " for " + coin.get("name"));
                result.add(new CoinDto(
                        symbol.toUpperCase(), name, krakenSymbol, id
                ));
                if (result.size() == 20) break;
            }
            System.out.println("[DEBUG] Final matched coins: " + result.size());
        }
        return result;
    }
}
