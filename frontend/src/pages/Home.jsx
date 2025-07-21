import React from "react";

function Home() {
    const user = JSON.parse(localStorage.getItem("user"));
    return (
        <div>
            Home<br />
            {user && (
                <div>
                    Welcome, {user.username}!<br />
                    Balance: {user.balance}
                </div>
            )}
        </div>
    );
}

export default Home;