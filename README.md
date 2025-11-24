# Atlas Brawler Backend - Spring Boot Integration

## Overview
Spring Boot backend for the Atlas Brawler P2E skateboarding game with Celo blockchain integration. Handles player authentication, game sessions, rewards, and MiniPay wallet interactions.

## Tech Stack
- **Framework**: Spring Boot 3.2.0
- **Java Version**: 17
- **Database**: MySQL (Production), H2 (Development)
- **Blockchain**: Web3j 4.10.3 for Celo integration
- **Security**: Spring Security with signature verification
- **Build Tool**: Maven

## Project Structure
```
src/main/java/com/atlasbrawler/backend/
├── config/                 # Configuration classes
│   ├── AsyncConfig.java
│   ├── SecurityConfig.java
│   └── Web3jConfig.java
├── controller/             # REST API controllers
│   ├── GameController.java
│   ├── HealthController.java
│   ├── PlayerController.java
│   └── RewardController.java
├── domain/                 # Entity models
│   ├── enums/
│   ├── CashPool.java
│   ├── CashPoolContribution.java
│   ├── Player.java
│   ├── Reward.java
│   ├── SkaterCard.java
│   └── Transaction.java
├── dto/                    # Data Transfer Objects
│   ├── GameSessionRequest.java
│   ├── PlayerRegistrationRequest.java
│   ├── PlayerResponse.java
│   └── RewardClaimRequest.java
├── factory/                # Object factories
│   ├── PlayerFactory.java
│   └── RewardFactory.java
├── repository/             # JPA repositories
│   ├── CashPoolContributionRepository.java
│   ├── CashPoolRepository.java
│   ├── PlayerRepository.java
│   ├── RewardRepository.java
│   ├── SkaterCardRepository.java
│   └── TransactionRepository.java
├── service/                # Business logic
│   ├── BlockchainService.java
│   ├── PlayerService.java
│   └── RewardService.java
└── util/                   # Utility classes
    ├── BlockchainUtil.java
    └── SignatureUtil.java
```

## Dependencies

### Core Web/API
- `spring-boot-starter-web`: Handles HTTP requests from LibGDX client (e.g., POST /shop/purchase)

### Security
- `spring-boot-starter-security`: Verifies MiniPay signatures and handles role-based access

### Database
- `spring-boot-starter-data-jpa`: JPA for object-relational mapping
- `mysql-connector-j`: MySQL driver for off-chain storage (player profiles, tokens, sessions)
- `h2`: In-memory database for development/testing

### Blockchain
- `web3j`: Interacts with Celo blockchain (contract calls, event listening, minting)
- `bcprov-jdk15on`: Bouncy Castle for cryptographic operations

### Validation/Testing
- `spring-boot-starter-validation`: Input validation (wallet addresses, amounts)
- `spring-boot-starter-test`: Unit and integration testing

### Optional
- `spring-boot-starter-amqp`: RabbitMQ for reliability (offline sync)
- `spring-boot-starter-actuator`: Monitoring and health checks

## Configuration

### Environment Variables
Set these before running:
```bash
export CELO_PRIVATE_KEY=your_private_key_here
export JWT_SECRET=your_jwt_secret_here
```

### Application Properties
Edit `src/main/resources/application.properties`:

**Database** (Switch between H2 and MySQL)
```properties
# MySQL (Production)
spring.datasource.url=jdbc:mysql://localhost:3306/atlas_brawler
spring.datasource.username=root
spring.datasource.password=yourpassword

# H2 (Development)
#spring.datasource.url=jdbc:h2:mem:testdb
#spring.h2.console.enabled=true
```

**Celo Network** (Alfajores testnet)
```properties
celo.network=sepolia
celo.rpc.url=https://forno.celo-sepolia.celo-testnet.org
celo.chain.id=11142220
```

## Getting Started

### 1. Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8.0+ (or use H2 for dev)
- Celo wallet with testnet funds

### 2. Database Setup
```bash
# For MySQL
mysql -u root -p
CREATE DATABASE atlas_brawler;
```

### 3. Build and Run
```bash
# Build
mvn clean install

# Run
mvn spring-boot:run

# Or with environment variables
CELO_PRIVATE_KEY=your_key mvn spring-boot:run
```

### 4. Verify
```bash
curl http://localhost:8080/api/health
# Response: {"status":"UP","service":"Atlas Brawler Backend","version":"1.0.0"}
```

## API Endpoints

### Health
- `GET /api/health` - Service health check

### Players
- `POST /api/players/register` - Register new player with wallet signature
- `GET /api/players/{walletAddress}` - Get player profile
- `GET /api/players/{walletAddress}/balance` - Get player balances

### Game
- `POST /api/game/session/complete` - Complete game session and earn rewards

### Rewards
- `POST /api/rewards/claim` - Claim pending cUSD rewards
- `GET /api/rewards/pending/{walletAddress}` - Get pending rewards

## Example API Calls

### Register Player
```bash
curl -X POST http://localhost:8080/api/players/register \
  -H "Content-Type: application/json" \
  -d '{
    "walletAddress": "0x742d35Cc6634C0532925a3b844Bc9e7595f0bEb",
    "username": "Atlas",
    "signature": "0x...",
    "message": "Register Atlas Brawler account"
  }'
```

### Complete Game Session
```bash
curl -X POST http://localhost:8080/api/game/session/complete \
  -H "Content-Type: application/json" \
  -d '{
    "walletAddress": "0x742d35Cc6634C0532925a3b844Bc9e7595f0bEb",
    "score": 1250,
    "wavesSurvived": 8,
    "isWin": true
  }'
```

### Claim Reward
```bash
curl -X POST http://localhost:8080/api/rewards/claim \
  -H "Content-Type: application/json" \
  -d '{
    "rewardId": 1,
    "walletAddress": "0x742d35Cc6634C0532925a3b844Bc9e7595f0bEb",
    "signature": "0x..."
  }'
```

## Testing
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=PlayerServiceTest
```

## Smart Contract Integration
Place compiled Solidity contracts in `../4.hardhat_smart_contracts_for_celo_js_backend/contracts/`, then:
```bash
# Generate Web3j wrappers
mvn web3j:generate-sources
```

## Deployment
```bash
# Build production JAR
mvn clean package -DskipTests

# Run JAR
java -jar target/backend-1.0.0-SNAPSHOT.jar
```

## Monitoring
Access actuator endpoints:
- Health: http://localhost:8080/api/actuator/health
- Metrics: http://localhost:8080/api/actuator/metrics

## Troubleshooting

### Database Connection Issues
- Verify MySQL is running: `mysql -u root -p`
- Check credentials in application.properties
- For dev, switch to H2 by uncommenting H2 config

### Blockchain Connection Issues
- Check Celo RPC URL is accessible
- Verify private key is set: `echo $CELO_PRIVATE_KEY`
- Test with Alfajores faucet: https://faucet.celo.org

### Signature Verification Fails
- Ensure message format matches client-side
- Check wallet address is checksummed
- Verify signature is 0x-prefixed hex

## Contributing
1. Follow Spring Boot best practices
2. Use Lombok for boilerplate reduction
3. Add validation to DTOs
4. Write tests for services
5. Document API changes

## License
MIT

## Contact
For questions, reach out on the Celo Africa DAO Discord.
