import React, {useEffect, useState} from "react";
import TransactionCard from "../components/TransactionCard";
import "./pages_css/transactions.css";
import { Link } from "react-router-dom";

function Transactions() {
	const user = JSON.parse(localStorage.getItem("user"));
	const [transactions, setTransactions] = useState([]);
	const [averagePrices, setAveragePrices] = useState({});

	const fetchTransactions = async () => {
		try {
			const txRes = await fetch("http://localhost:8080/api/users/transactions", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(user),
			});
			const txData = await txRes.json();
			setTransactions(txData);

			const avgPricePromises = txData.map(async (tx) => {
				const res = await fetch("http://localhost:8080/api/users/average-price", {
					method: "POST",
					headers: { "Content-Type": "application/json" },
					body: JSON.stringify({ user, symbol: tx.symbol }),
				});
				const avgPrice = await res.json();
				return { symbol: tx.symbol, averagePrice: avgPrice };
			});

			const avgPricesArr = await Promise.all(avgPricePromises);
			const avgPricesObj = {};
			avgPricesArr.forEach(({ symbol, averagePrice }) => {
				avgPricesObj[symbol] = averagePrice;
			});
			setAveragePrices(avgPricesObj);
		} catch (err) {
			console.error("Error fetching data:", err);
		}
	}

	useEffect(() => {
		if (user) {
			fetchTransactions()
			}
	}, []);

	return (
		<>
			<div className="transaction-container">
				{user && (
					<div className="navbar">
						<div className="user-info">
							<p>
								Welcome, <span>{user.username}</span>
							</p>
							<p>Balance: ${user.balance}</p>
						</div>

						<Link to="/home" className="home-btn">
							Go to Home
						</Link>
					</div>
				)}
				<div className="transaction-list">
					<h2>Transactions</h2>
					{Array.isArray(transactions) && transactions.map((tx, index) => (
						<TransactionCard
							key={index}
							buy={tx.buy}
							price={tx.price}
							quantity={tx.quantity}
							symbol={tx.symbol}
							name={tx.name}
							total={tx.total}
							averagePrice={averagePrices[tx.symbol]}
							timestamp={tx.timestamp}
						/>
					))}
				</div>
			</div>
		</>
	);
}

export default Transactions;
