import React, {useEffect, useState} from "react";
import CoinCard from "../components/CoinCard";
import "./pages_css/home.css";

function Home() {
	const user = JSON.parse(localStorage.getItem("user"));
	const coinApiUrl = "http://localhost:8080/api/coins/top20";
	const krakenWsUrl = "wss://ws.kraken.com/v2";
	const [coins, setCoins] = useState([]);

	useEffect(() => {
		fetch(coinApiUrl)
			.then((response) => response.json())
			.then((data) => {
				setCoins(data);
				console.log("Fetched coins:", coins);
			})
			.catch((error) => {
				console.error("Error fetching coins:", error);
			});
		//TODO: implement real-time price updates with WebSocket
	}, []);

	return (
		<div className="home-container">
			Home
			<br />
			{user && (
				<div>
					Welcome, {user.username}!<br />
					Balance: {user.balance}
				</div>
			)}
			<div className="coinlist">
				<div className="coinlist-header">
					<div>name, symbol, price</div>
					<div>Actions</div>
				</div>
				{coins.map((coin) => (
					<CoinCard
						name={coin.name}
						symbol={coin.symbol}
						price={"3000"}
					/>
				))}
			</div>
		</div>
	);
}

export default Home;
