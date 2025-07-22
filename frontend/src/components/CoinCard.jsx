import React, { useState } from "react";
import "./component_css/coin_card.css";

function CoinCard({ name, symbol, price, holdings, onTransaction }) {
	const [dropdownOpen, setDropdownOpen] = useState(false);
	const [isBuying, setIsBuying] = useState(true);
	const [quantity, setQuantity] = useState(0);
	const [usdAmount, setUsdAmount] = useState(0);
	const [message, setMessage] = useState("");

	const numericPrice = parseFloat(price.replace(/[$,]/g, ""));

	const handleQuantityChange = (e) => {
		const qty = e.target.value;
		setQuantity(qty);
		setUsdAmount(qty * numericPrice);
	};

	const handleUsdAmountChange = (e) => {
		const usd = e.target.value;
		setUsdAmount(usd);
		setQuantity(numericPrice ? usd / numericPrice : 0);
	};

	const handleBuy = async (e) => {
		e.preventDefault();
		try {
			const user = JSON.parse(localStorage.getItem("user"));
			const buyUsdAmount = parseFloat(usdAmount);
			const latestPrice = numericPrice;
			const buyQuantity = buyUsdAmount / latestPrice;

			if (user.balance < buyUsdAmount) {
				setMessage("Insufficient balance for this transaction.");
				return;
			}

			if (buyQuantity <= 0 || buyUsdAmount <= 0) {
				setMessage("Please enter a valid quantity.");
				return;
			}

			const payload = {
				transaction: {
					user: user,
					symbol: symbol,
					price: numericPrice,
					quantity: buyQuantity,
					buy: true,
					total: numericPrice * buyQuantity,
					profit_loss: 0,
				},
				holding: {
					user: user,
					symbol: symbol,
					quantity: (parseFloat(holdings) || 0) + buyQuantity,
				},
			};
			await fetch("http://localhost:8080/api/trade/buy", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(payload),
			});
			if (onTransaction) onTransaction();
			setMessage("Transaction successful!");
		} catch (error) {
			console.error("Error during buy transaction:", error);
		}
	};

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
							{parseFloat(holdings).toPrecision(6)} {symbol}
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
						<form
							className="transaction-form"
							onSubmit={isBuying ? handleBuy : undefined}
						>
							<label htmlFor="amount">Amount {symbol}</label>
							<input
								type="number"
								value={quantity}
								onChange={handleQuantityChange}
							/>
							<label htmlFor="">Amount USD</label>
							<input
								type="number"
								value={usdAmount}
								onChange={handleUsdAmountChange}
							/>
							{isBuying ? (
								<button type="submit" className="buy-button">
									Buy
								</button>
							) : (
								<button type="submit" className="sell-button">
									Sell
								</button>
							)}
							{message && <p className="transaction-message">{message}</p>}
						</form>
					</div>
				)}
			</div>
		</>
	);
}

export default CoinCard;
