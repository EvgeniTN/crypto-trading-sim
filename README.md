# Crypto Trading Simulator
A web application for simulating cryptocurrency trading using real-time data from the Kraken v2 websocket.

## Features

- Login and registration system
- Real-time cryptocurrency data from Kraken v2 websocket
- Trading simulation with buy/sell functionality
- Transaction history
- Account reset

## How to run the project

Dependencies: (Java 21 SDK, MySql, Maven, Node.js)

Install MySQL: https://dev.mysql.com/downloads/mysql/ (take note of the root password you enter)

1. Under src/main/resources/application.yaml change the following:
```password: InsertYourPasswordHere```

2. Maven must be installed: https://maven.apache.org/install.html

3. Add a new MySQL datasouce at localhost:3306 with the name `root` and the root password you set earlier.

4. Then run the following command to create the database and tables:
```.\mvnm flyway:migrate```

To run the backend:

1. Open a terminal in the root directory of the project and run: ```.\mvnw spring-boot:run```

To run the frontend:

1. Open a second terminal and navigate to the `frontend` directory and run:
```npm install```

2. Then start the frontend with:
```npm run dev```

### Known Issues
1. Users are able to register using only blank characters in the username and password fields.