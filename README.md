# 🛹 ATLAS BRAWLER - Backend API
```
   ╔═══════════════════════════════════════╗
   ║                                       ║
   ║     ▄▀█ ▀█▀ █░░ ▄▀█ █▀               ║
   ║     █▀█ ░█░ █▄▄ █▀█ ▄█               ║
   ║                                       ║
   ║     █▄▄ █▀█ ▄▀█ █░█░█ █░░ █▀▀ █▀█    ║
   ║     █▄█ █▀▄ █▀█ ▀▄▀▄▀ █▄▄ ██▄ █▀▄    ║
   ║                                       ║
   ║      🔧 Backend API • Celo Ready      ║
   ╚═══════════════════════════════════════╝
```

Spring Boot backend powering Atlas Brawler's player profiles, game sessions, and Celo blockchain rewards.

---

## 🚀 Quick Start

### 1. Setup Configuration

Copy the example config file:
```bash
cd src/main/resources
cp application.properties.example application.properties
```

Edit `application.properties` and fill in:
- ✅ Your PostgreSQL database credentials (Render provides these automatically)
- ✅ Your Celo wallet private key (Alfajores or Sepolia testnet)
- ✅ A secure JWT secret
- ✅ A secure admin password

**Note:**  
`application.properties` has been updated to accommodate the Render environment. Instead of hardcoding local MySQL, the backend now uses environment variables for PostgreSQL (`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`). This ensures smooth deployment on Render.

### 2. Database Options

**Option A: PostgreSQL (Production on Render)**  
Provision a Postgres instance in Render and use the provided credentials.

**Option B: H2 Database (Quick Testing)**  
Uncomment the H2 section in `application.properties`.

### 3. Run the Backend
```bash
mvn spring-boot:run
```

Backend starts on: `http://localhost:8080/api`

### 4. Test It's Working
Visit: `http://localhost:8080/api/health`

You should see:
```json
{"status": "UP"}
```

---

## 🎮 What This Backend Does
- 👤 Player management (register, balances, stats)
- 🎯 Game session tracking
- 🎁 Blockchain rewards integration with Celo
- 📊 Stats and leaderboard updates

---

## 📡 API Endpoints
- `POST /players/register`
- `GET /players/{walletAddress}/balance`
- `POST /game/session/complete`
- `GET /rewards/pending/{walletAddress}`
- `POST /rewards/claim`
- `GET /health`

---

## 🔐 Security Notes
- Never commit secrets
- Use env vars in production
- Rotate JWT secrets and admin passwords
- Keep private keys safe

---

## 🛠️ Tech Stack
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL (Render)
- Web3j (Celo)
- Spring Security

---

## 📦 Database Schema
- **Player**: wallet, username, balances, stats
- **GameSession**: session ID, score, timestamp
- **Reward**: reward ID, amount, claim status

---

## 🎯 Environment Variables (Alternative Config)
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/atlas_brawler
export SPRING_DATASOURCE_USERNAME=youruser
export SPRING_DATASOURCE_PASSWORD=yourpassword
export CELO_WALLET_PRIVATE_KEY=your_private_key
export JWT_SECRET=your_jwt_secret

mvn spring-boot:run
```

---

## 🎨 Frontend Dev Server (Located in Backend Repo)

A copy of the frontend lives inside the backend repo under `AtlasBrawler_JS_Frontend`.

To run it locally in dev mode:

```bash
cd AtlasBrawler_JS_Frontend
npm install
npm run dev
```

Frontend starts on: `http://localhost:5173`

---

## 🌐 Frontend Deployment on Render

The production frontend is deployed at:  
👉 **https://atlasbrawler-js-frontend.onrender.com**

### 🔑 Connecting with MetaMask Mobile
- Open the **MetaMask mobile app**  
- Go to the **Browser** tab  
- Paste the frontend URL:  
  `https://atlasbrawler-js-frontend.onrender.com`  
- Connect your wallet to start playing and earning rewards

**Preferred usage:**  
Run the app on **mobile inside MetaMask’s in‑app browser**. This ensures wallet injection (`window.ethereum`) works correctly, since MetaMask extensions are only available on desktop browsers.

---

Built with ❤️ for **Celo Africa DAO**  
Powering crypto rewards for skaters worldwide! 🛹⛓️

---
