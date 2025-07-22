import React, {useEffect, useRef, useState} from "react";
import CoinCard from "../components/CoinCard";
import "./pages_css/home.css";

function Home() {
	const user = JSON.parse(localStorage.getItem("user"));
	const coinApiUrl = "http://localhost:8080/api/coins/top20";
	const krakenWsUrl = "wss://ws.kraken.com/v2";
	const [coins, setCoins] = useState([]);
	const [prices, setPrices] = useState({});

	const wsRef = useRef(null);

	// const normalizeSymbol = (rawSymbol) => {
	// 	return rawSymbol
	// 		.replace(/^XBT/, "BTC")
	// 		.replace(/^XDG/, "DOGE");
	// }

	const wsnameToKrakenKey = {
		"XBT/USD": "XXBTZUSD",
		"DOGE/USD": "XDGUSD",
	}

	useEffect(() => {
		fetch(coinApiUrl)
			.then((response) => response.json())
			.then((data) => {
				setCoins(data);
				const krakenSymbols = data.map((coin) => wsnameToKrakenKey[coin.krakenSymbol] || coin.krakenSymbol);
				console.log("[SUBSCRIBE] Symbols:", krakenSymbols);

				const ws = new WebSocket(krakenWsUrl);
				wsRef.current = ws;

				ws.onopen = () => {
					ws.send(
						JSON.stringify({
							method: "subscribe",
							params: {
								channel: "ticker",
								symbol: krakenSymbols,
							},
						})
					);
				};

				ws.onmessage = (event) => {
					const msg = JSON.parse(event.data);
					if (msg.channel === "ticker" && msg.data){
						const t = msg.data[0];
						const symbol = t.symbol;
						// const normalizedSymbol = normalizeSymbol(symbol);
						const lastPrice = parseFloat(t.last);
						// console.log(
						// 	"[WS]",
						// 	"rawSymbol:", t.symbol,
						// 	"normalized:", normalizeSymbol(t.symbol),
						// 	"last:", t.last
						// );

						setPrices((prev) => ({
							...prev,
							[msg.data[0].symbol]: parseFloat(msg.data[0].last),
						}));
					}
				};
				ws.onerror = (e) => {
					console.error("WebSocket error:", e);
				};

				ws.onclose = () => {
					console.log("WebSocket closed");
				};

			})
			.catch((error) => {
				console.error("Error fetching coins:", error);
			});

		return() => {
			if (wsRef.current) {
				wsRef.current.close();
			}
		};
	}, []);

	return (
		<div className="home-container">
			Home
			<br />
			{user && (
				<div>
					Welcome, {user.username}!<br />
					Balance: ${user.balance}
				</div>
			)}
			<div className="coinlist">
				<div className="coinlist-header">
					<div>name, symbol, price</div>
					<div>Actions</div>
				</div>
				{coins.map((coin) => {
					const key = wsnameToKrakenKey[coin.krakenSymbol] || coin.krakenSymbol;
					const price = prices[key];


					// console.log("[RENDER]", coin.symbol, "→", coin.krakenSymbol, "→", displayKey, "→", price);

					return (
						<CoinCard
							key={coin.krakenSymbol}
							name={coin.name}
							symbol={coin.symbol}
							price={price ? `$${price.toFixed(2)}` : "Loading..."}
						/>
					);
				})}
			</div>
		</div>
	);
}

export default Home;
