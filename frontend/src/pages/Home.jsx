import React, { useEffect, useRef, useState } from "react";
import CoinCard from "../components/CoinCard";
import "./pages_css/home.css";

function Home() {
	const user = JSON.parse(localStorage.getItem("user"));
	const coinApiUrl = "http://localhost:8080/api/coins/top20";
	const krakenWsUrl = "wss://ws.kraken.com/v2";
	const [coins, setCoins] = useState([]);
	const [prices, setPrices] = useState({});
	const [holdings, setHoldings] = useState({});

	const wsRef = useRef(null);

	const wsnameToKrakenKey = {
		"XBT/USD": "BTC/USD",
		"XDG/USD": "DOGE/USD",
	};

	const handleTransactionComplete = async () => {
		const res = await fetch(`http://localhost:8080/api/users/${user.id}`);
		const updatedUser = await res.json();
		localStorage.setItem("user", JSON.stringify(updatedUser));
		await fetchHoldings()
	};

	const fetchHoldings = async () => {
		fetch('http://localhost:8080/api/users/holdings', {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(user)
		})
			.then(res => res.json())
			.then(data => setHoldings(data))
			.catch(err => console.error("Error fetching holdings:", err));
	}

	const fetchCoins = async () => {
		try {
			const response = await fetch(coinApiUrl);
			const data = await response.json();
			setCoins(data);
			const krakenSymbols = data.map(
				(coin) => wsnameToKrakenKey[coin.krakenSymbol] || coin.krakenSymbol
			);
			setupWebSocket(krakenSymbols);
		} catch (error) {
			console.error("Error fetching coins:", error);
		}
	};

	const setupWebSocket = (krakenSymbols) => {
		const ws = new WebSocket(krakenWsUrl);
		wsRef.current = ws;

		ws.onopen = () => {
			ws.send(JSON.stringify({
				method: "subscribe",
				params: { channel: "ticker", symbol: krakenSymbols }
			}));
		};

		ws.onmessage = (event) => {
			const msg = JSON.parse(event.data);
			if (msg.channel === "ticker" && msg.data) {
				setPrices((prev) => ({
					...prev,
					[msg.data[0].symbol]: parseFloat(msg.data[0].last),
				}));
			}
		};

		ws.onerror = (e) => console.error("WebSocket error:", e);
		ws.onclose = () => console.log("WebSocket closed");
	};

	useEffect(() => {
		fetchHoldings();
		fetchCoins();

		return () => {
			if (wsRef.current) wsRef.current.close();
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
					<div className="coinlist-header-labels">
						<p>Name</p>
						<p>Symbol</p>
						<p>Price</p>
					</div>
					<div className="coinlist-header-labels">
						<p>Holdings</p>
						<p>Value</p>
						<p></p>
					</div>
				</div>
				{coins.map((coin) => {
					const key = wsnameToKrakenKey[coin.krakenSymbol] || coin.krakenSymbol;
					const price = prices[key];
					const holding = holdings[coin.symbol] || 0;
					return (
						<CoinCard
							key={coin.krakenSymbol}
							name={coin.name}
							symbol={coin.symbol}
							price={price ? `$${price}` : "Loading..."}
							holdings={holding}
							onTransaction={handleTransactionComplete}
						/>
					);
				})}
				<CoinCard
					symbol="BTC"
					name="Bitcoin"
					price="$101203.31"
					holdings="2.342"
				/>
			</div>
		</div>
	);
}

export default Home;
