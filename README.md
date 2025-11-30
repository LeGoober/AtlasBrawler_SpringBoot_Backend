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
- ✅ Your MySQL database credentials
- ✅ Your Celo wallet private key (Alfajores testnet)
- ✅ A secure JWT secret
- ✅ A secure admin password

### 2. Install MySQL (or use H2)

**Option A: MySQL** (Production)
```bash
# Install MySQL and create database
mysql -u root -p
CREATE DATABASE atlas_brawler;
```

**Option B: H2 Database** (Quick Testing)  
Uncomment the H2 section in `application.properties`

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

### Player Management
- 👤 Register players with Celo wallet signatures
- 💰 Track soft token balances
- 📊 Store game stats (wins, high scores, games played)

### Game Sessions
- 🎯 Save completed game results
- 🏆 Calculate rewards based on performance
- 📈 Update player stats automatically

### Rewards System
- 🎁 Create claimable blockchain rewards
- ✅ Verify wallet signatures
- 💸 Integrate with Celo smart contracts

---

## 📡 API Endpoints

### Players
- `POST /players/register` - Register new player
- `GET /players/{walletAddress}/balance` - Get player balance & stats

### Game Sessions
- `POST /game/session/complete` - Save completed game

### Rewards
- `GET /rewards/pending/{walletAddress}` - Get pending rewards
- `POST /rewards/claim` - Claim blockchain rewards

### Health
- `GET /health` - Service health check

---

## 🔐 Security Notes

**⚠️ NEVER commit sensitive data!**

The `.gitignore` is set to exclude `application.properties`. Always use the `.example` file as a template.

**For Production:**
- Use environment variables for secrets
- Change all default passwords
- Use a secure JWT secret (64+ characters)
- Never expose private keys in logs

---

## 🛠️ Tech Stack

- **Spring Boot 3.x** - Modern Java framework
- **Spring Data JPA** - Database management
- **MySQL** - Production database
- **Web3j** - Celo blockchain integration
- **Spring Security** - Auth & CORS

---

## 📦 Database Schema

### Player
- Wallet address (primary key)
- Username
- Soft token balance
- cUSD balance
- Stats (games played, wins, high score)

### GameSession
- Session ID
- Player wallet
- Score, waves survived, win status
- Timestamp

### Reward
- Reward ID
- Player wallet
- Amount, transaction hash, claim status
- Created/claimed timestamps

---

## 🎯 Environment Variables (Alternative Config)

Instead of editing `application.properties`, you can use env vars:

```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/atlas_brawler
export SPRING_DATASOURCE_USERNAME=root
export SPRING_DATASOURCE_PASSWORD=yourpassword
export CELO_WALLET_PRIVATE_KEY=your_private_key
export JWT_SECRET=your_jwt_secret

mvn spring-boot:run
```

---

Built with ❤️ for **Celo Africa DAO**  
Powering crypto rewards for skaters worldwide! 🛹⛓️
