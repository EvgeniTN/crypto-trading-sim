import React, { useEffect, useRef, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import CoinCard from "../components/CoinCard";
import "./pages_css/home.css";

function Home() {
	const user = JSON.parse(localStorage.getItem("user"));

	const coinApiUrl = "http://localhost:8080/api/coins/top20";
	const krakenWsUrl = "wss://ws.kraken.com/v2";

	const [coins, setCoins] = useState([]);
	const [prices, setPrices] = useState({});
	const [holdings, setHoldings] = useState({});

	const navigate = useNavigate();

	const wsRef = useRef(null);

	const wsnameToKrakenKey = {
		"XBT/USD": "BTC/USD",
		"XDG/USD": "DOGE/USD",
	};

	const handleLogout = () => {
		localStorage.removeItem("user");
		navigate("/");
	};

	const handleTransactionComplete = async () => {
		const res = await fetch(`http://localhost:8080/api/users/${user.id}`);
		const updatedUser = await res.json();
		localStorage.setItem("user", JSON.stringify(updatedUser));
		await fetchHoldings();
	};

	const handleResetAccount = async () => {
		try {
			const res = await fetch(
				`http://localhost:8080/api/users/${user.id}/reset`,
				{
					method: "POST",
					headers: { "Content-Type": "application/json" },
				}
			);
			if (res.ok) {
				const updatedUser = await res.json();
				localStorage.setItem("user", JSON.stringify(updatedUser));
				await fetchHoldings();
			} else {
				console.error("Failed to reset account");
			}
		} catch (error) {
			console.error("Error resetting account:", error);
		}
	};

	const fetchHoldings = async () => {
		fetch("http://localhost:8080/api/users/holdings", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(user),
		})
			.then((res) => res.json())
			.then((data) => setHoldings(data))
			.catch((err) => console.error("Error fetching holdings:", err));
	};

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
			ws.send(
				JSON.stringify({
					method: "subscribe",
					params: { channel: "ticker", symbol: krakenSymbols },
				})
			);
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
			{user && (
				<div className="navbar">
					<div className="user-info">
						<p>
							Welcome, <span>{user.username}</span>
						</p>
						<p>Balance: ${user.balance}</p>
					</div>
					<div className="navbar-actions">
						<Link to="/transactions" className="transaction-btn">
							View Transactions
						</Link>
						<button className="reset-btn" onClick={handleResetAccount}>
							Reset Account
						</button>
						<button className="logout-btn" onClick={handleLogout}>
							Logout
						</button>
					</div>
				</div>
			)}
			<div className="coinlist">
				<h2>Coin list</h2>
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
			</div>
		</div>
	);
}

export default Home;
