import React, { useState } from "react";
import "./component_css/transaction_card.css";

function TransactionCard({
	buy,
	price,
	quantity,
	symbol,
	name,
	total,
	averagePrice,
	timestamp,
}) {
	const [dropdownOpen, setDropdownOpen] = useState(false);
	const profit_loss = (parseFloat(price) - parseFloat (averagePrice)) * quantity;

	return (
		<>
			<div className="small-card">
				<p>{name}</p>
				<p>{symbol}</p>
				<p>{buy ? "Buy" : "Sell"}</p>
				<p>{total}</p>
				<button
					className="dropdown-button"
					onClick={() => setDropdownOpen(!dropdownOpen)}
				>
					{dropdownOpen ? "▲" : "▼"}
				</button>
			</div>
			{dropdownOpen && (
				<div className="transaction-card">
					<div className="transaction-card-content">
						<div className="info-row">
							<p>Type</p>
							<p>{buy ? "Buy" : "Sell"}</p>
						</div>
						<hr />
						<div className="info-row">
							<p>Name</p>
							<p>{name}</p>
						</div>
						<hr />
						<div className="info-row">
							<p>Symbol</p>
							<p>{symbol}</p>
						</div>
						<hr />
						<div className="info-row">
							<p>Price</p>
							<p>{price}</p>
						</div>
						<hr />
						<div className="info-row">
							<p>Quantity</p>
							<p>{quantity}</p>
						</div>
						<hr />
						<div className="info-row">
							<p>Total</p>
							<p>{total}</p>
						</div>
						<hr />
						<div className="info-row">
							<p>Profit/Loss</p>
							<p>{buy ? "N/A" : "$ "+parseFloat(profit_loss).toFixed(6) }</p>
						</div>
						<hr />
						<div className="info-row">
							<p>Filled on</p>
							<p>{new Date(timestamp).toLocaleString()}</p>
						</div>
					</div>
				</div>
			)}
		</>
	);
}

export default TransactionCard;
