import React from "react";
import "./pages_css/login.css";

function Login() {
	return (
		<>
			<div className="login-container">
				<h1>Crypto Trading Sim</h1>
				<form className="login-form">
					<h2>Login</h2>
					<input type="text" placeholder="Username" />
					<input type="password" placeholder="Password" />
					<div className="btn-container">
						<button type="submit">Login</button>
						<button type="button">Register</button>
					</div>
				</form>
			</div>
		</>
	);
}

export default Login;
