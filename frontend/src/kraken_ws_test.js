// Save as kraken_ws_test.js and run with: node kraken_ws_test.js
import WebSocket from 'ws';

const ws = new WebSocket('wss://ws.kraken.com/v2');

ws.on('open', () => {
    ws.send(JSON.stringify({
        method: "subscribe",
        params: {
            channel: "ticker",
            symbol: ["BTC/USD", "ETH/USD"] // Add more pairs as needed
        }
    }));
});

ws.on('message', (data) => {
    console.log('Received:', data.toString());
});

ws.on('error', (err) => {
    console.error('WebSocket error:', err);
});

ws.on('close', () => {
    console.log('WebSocket closed');
});