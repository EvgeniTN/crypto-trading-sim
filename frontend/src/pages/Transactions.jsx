import React from "react";
import TransactionCard from "../components/TransactionCard";
import "./pages_css/transactions.css";
import { Link } from "react-router-dom";

function Transactions() {
	const user = JSON.parse(localStorage.getItem("user"));

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
					<TransactionCard
						buy={true}
						price="$50000"
						quantity="0.1"
						symbol="BTC"
						name="Bitcoin"
						total="$5000"
						profit_loss="0"
						timestamp={Date.now()}
					/>
				</div>
			</div>
		</>
	);
}

export default Transactions;
