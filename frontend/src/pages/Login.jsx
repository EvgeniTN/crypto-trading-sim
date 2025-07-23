import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import "./pages_css/login.css";

function Login() {
	const [username, setUsername] = useState("");
	const [password, setPassword] = useState("")
	const [message, setMessage] = useState("");
	const navigate = useNavigate();

	const handleRegister = async (e) => {
		if (!username || !password) {
			setMessage("Username and password are required");
			return;
		}
		if (username.length < 3 || username.length > 20) {
			setMessage("Username must be between 3 and 20 characters");
			return;
		}
		if (password.length < 4 || password.length > 30) {
			setMessage("Password must be between 4 and 30 characters");
			return;
		}
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

	const handleLogin = async (e) => {
		e.preventDefault();
		try {
			const response = await fetch("http://localhost:8080/api/users/login", {
				method: "POST",
				headers: { "Content-type": "application/json" },
				body: JSON.stringify({ username, password }),
			});
			if (response.ok) {
				const user = await response.json();
				localStorage.setItem("user", JSON.stringify(user))
				setMessage("login success");
				navigate("/home")
			} else {
				const text = await response.text()
				setMessage(text);
			}
		} catch (err) {
			setMessage("login failed: " + err);
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
						   minLength="3"
						   maxLength="20"
						   required
					/>
					<input type="password"
						   placeholder="Password"
						   value={password}
						   onChange={e => setPassword(e.target.value)}
						   minLength="4"
						   maxLength="30"
						   required
					/>
					<div className="btn-container">
						<button type="submit" onClick={handleLogin}>Login</button>
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
