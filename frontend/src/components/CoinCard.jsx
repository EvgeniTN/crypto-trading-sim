import React, { useState } from "react";
import "./component_css/coin_card.css";

function CoinCard({ name, symbol, price, holdings }) {
	const [dropdownOpen, setDropdownOpen] = useState(false);
	const [isBuying, setIsBuying] = useState(true);
	const [quantity, setQuantity] = useState(0);
	const handleBuy = async (e) => {
		e.preventDefault();
		const user = JSON.parse(localStorage.getItem("user"));
		console.log(user)
		const numericPrice = parseFloat(price.replace(/[$,]/g, ""));
		const buyQuantity = parseFloat(quantity);
		const payload = {
			transaction:{
				user: user,
				symbol: symbol,
				price: numericPrice,
				quantity: buyQuantity,
				buy: true,
				total: numericPrice * buyQuantity,
				profit_loss: 0,
			},
			holding:{
				user: user,
				symbol: symbol,
				quantity: (parseFloat(holdings) || 0) + buyQuantity,
			}
		};
		await fetch("http://localhost:8080/api/trade/buy", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(payload)
		});
	}

	return (
		<>
			<div className="coincard">
				<div className="coincard-container">
					<div className="coincard-content">
						<p>{name}</p>
						<p>{symbol}</p>
						<p>{price}</p>
					</div>
					<div className="coincard-actions">
						<p>
							{holdings} {symbol}
						</p>
						<p>
							{price
								? `$${(
										parseFloat(price.replace(/[$,]/g, "")) * holdings
								  ).toFixed(2)}`
								: "Loading..."}
						</p>
						<button
							className="dropdown-button"
							onClick={() => setDropdownOpen(!dropdownOpen)}
						>
							{dropdownOpen ? "▲" : "▼"}
						</button>
					</div>
				</div>
				{dropdownOpen && (
					<div className="dropdown-menu">
						<div className="dropdown-header">
							<button
								className={`${isBuying ? " active" : ""}`}
								onClick={() => setIsBuying(true)}
							>
								Buy
							</button>
							<button
								className={`${!isBuying ? " active" : ""}`}
								onClick={() => setIsBuying(false)}
							>
								Sell
							</button>
						</div>
						<form className="transaction-form" onSubmit={isBuying ? handleBuy : undefined}>
							<label htmlFor="amount">Amount {symbol}</label>
							<input
								type="number"
								name=""
								id=""
								value={quantity}
								onChange={(e) => setQuantity(e.target.value)}
							/>
							<label htmlFor="">Amount USD</label>
							<input type="number" name="" id="" />
							{isBuying ? (
								<button type="submit" className="buy-button">
									Buy
								</button>
							) : (
								<button type="submit" className="sell-button">
									Sell
								</button>
							)}
						</form>
					</div>
				)}
			</div>
		</>
	);
}

export default CoinCard;
