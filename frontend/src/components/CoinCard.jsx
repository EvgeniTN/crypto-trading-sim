import React from "react";
import "./component_css/coin_card.css";

function CoinCard({name, symbol, price}) {
	return (
		<>
			<div className="coincard-container">
				<div className="coincard-content">
					<p>{name}</p>
					<p>{symbol}</p>
					<p>{price}</p>
				</div>
				<div className="coincard-actions">
					<button className="buy-button">Buy</button>
					<button className="sell-button">Sell</button>
				</div>
			</div>
		</>
	);
}

export default CoinCard;
