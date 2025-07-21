import React, {useState} from "react";
import "./pages_css/login.css";

function Login() {
	const [username, setUsername] = useState("");
	const [password, setPassword] = useState("")
	const [message, setMessage] = useState("");

	const handleRegister = async (e) => {
		e.preventDefault();
		try {
			const response = await fetch("http://localhost:8080/api/users/register", {
				method: "POST",
				headers: { "Content-type": "application/json" },
				body: JSON.stringify({ username, password }),
			});
			const text = await response.text();
			if (response.ok) {
				setMessage("Registration success");
			} else {
				setMessage(text);
			}
		} catch (err) {
			setMessage("Registration failed: " + err.message);
		}
	};

	return (
		<>
			<div className="login-container">
				<h1>Crypto Trading Sim</h1>
				<form className="login-form">
					<h2>Login</h2>
					<input type="text"
						   placeholder="Username" value={username}
						   onChange={e => setUsername(e.target.value)}
					/>
					<input type="password"
						   placeholder="Password"
						   value={password}
						   onChange={e => setPassword(e.target.value)}
					/>
					<div className="btn-container">
						<button type="submit">Login</button>
						<button type="button" onClick={handleRegister}>
							Register
						</button>
					</div>
					{message && <p className="message">{message}</p>}
				</form>
			</div>
		</>
	);
}

export default Login;
