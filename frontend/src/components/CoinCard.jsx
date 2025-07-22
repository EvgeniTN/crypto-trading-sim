import React, { useState } from "react";
import "./component_css/coin_card.css";

function CoinCard({ name, symbol, price, holdings }) {
	const [dropdownOpen, setDropdownOpen] = useState(false);
	const [isBuying, setIsBuying] = useState(true);

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
						<form action="" className="transaction-form">
							<label htmlFor="amount">Amount {symbol}</label>
							<input type="number" name="" id="" />
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
