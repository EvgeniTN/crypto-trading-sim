package com.evgeni.cryptotradingsim.controllers;

import com.evgeni.cryptotradingsim.dto.CoinDto;
import com.evgeni.cryptotradingsim.services.CoinSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/coins")
@RequiredArgsConstructor
public class CoinController {
    private final CoinSyncService coinSyncService;

    @GetMapping("/test")
    public String test(){
        return "CoinController is working";
    }

    @GetMapping("/top20")
    public ResponseEntity<List<CoinDto>> getTop20Coins() throws SQLException {
        return ResponseEntity.ok(coinSyncService.getTop20Coins());
    }
}
