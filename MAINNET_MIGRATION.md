# 🚀 Atlas Brawler - Celo Mainnet Migration Guide

```
   ╔═══════════════════════════════════════╗
   ║  TESTNET → MAINNET MIGRATION          ║
   ║  Celo Africa DAO Hackathon Winner     ║
   ╚═══════════════════════════════════════╝
```

## 📋 Overview

This guide walks you through migrating Atlas Brawler from **Celo Alfajores/Sepolia Testnet** to **Celo Mainnet** for production deployment.

---

## 🔍 Key Network Changes

| Component | Testnet | Mainnet |
|-----------|---------|---------|
| **Network Name** | Celo Alfajores/Sepolia | Celo Mainnet |
| **Chain ID (Decimal)** | 44787 | 42220 |
| **Chain ID (Hex)** | 0xaef3 | 0xa4ec |
| **RPC URL** | https://alfajores-forno.celo-testnet.org | https://forno.celo.org |
| **Block Explorer** | https://alfajores.celoscan.io/ | https://celoscan.io/ |
| **Currency Symbol** | CELO (testnet) | CELO |

---

## 📝 Step 1: Backend Configuration Changes

### File: `src/main/resources/application.properties`

**Current (Testnet):**
```properties
# Celo Blockchain Configuration
celo.network=alfajores
celo.rpc.url=https://alfajores-forno.celo-testnet.org
celo.chain.id=44787
celo.wallet.private.key=YOUR_PRIVATE_KEY_HERE
```

**New (Mainnet):**
```properties
# Celo Blockchain Configuration
celo.network=mainnet
celo.rpc.url=https://forno.celo.org
celo.chain.id=42220
celo.wallet.private.key=YOUR_MAINNET_PRIVATE_KEY_HERE
```

### Environment Variables (Render Dashboard)

Update these in your Render backend service:

```bash
CELO_WALLET_PRIVATE_KEY=<your_mainnet_wallet_private_key>
```

⚠️ **IMPORTANT:** 
- Use a **NEW mainnet wallet** with real CELO funds
- Never reuse your testnet private key on mainnet
- Ensure the wallet has sufficient CELO for gas fees (~0.5-1 CELO minimum)

---

## 🎨 Step 2: Frontend Configuration Changes

### File: `AtlasBrawler_JS_Frontend/src/hooks/useWallet.ts`

**Current (Testnet):**
```typescript
const CELO_SEPOLIA = {
  chainId: '0xaef3',
  chainName: 'Celo Sepolia',
  nativeCurrency: { 
    name: 'CELO', 
    symbol: 'CELO', 
    decimals: 18 
  },
  rpcUrls: ['https://alfajores-forno.celo-testnet.org'],
  blockExplorerUrls: ['https://alfajores.celoscan.io/'],
};
```

**New (Mainnet):**
```typescript
const CELO_MAINNET = {
  chainId: '0xa4ec',
  chainName: 'Celo Mainnet',
  nativeCurrency: { 
    name: 'CELO', 
    symbol: 'CELO', 
    decimals: 18 
  },
  rpcUrls: ['https://forno.celo.org'],
  blockExplorerUrls: ['https://celoscan.io/'],
};
```

**Also update chain ID checks:**

Find line ~121:
```typescript
if (Number(network.chainId) !== 44787) {
```

Change to:
```typescript
if (Number(network.chainId) !== 42220) {
```

Find line ~122:
```typescript
console.warn('Not on Celo Sepolia, current chain:', network.chainId);
```

Change to:
```typescript
console.warn('Not on Celo Mainnet, current chain:', network.chainId);
```

Find all references to `CELO_SEPOLIA` and rename to `CELO_MAINNET`.

---

## 💰 Step 3: Get Mainnet CELO (Gas Fees)

### Option 1: Request from Hackathon Organizers
As mentioned in your prize email:
> "If you need some Celo to deploy any contracts, please let us know."

Email the organizers and request mainnet CELO for gas fees.

### Option 2: Purchase CELO
- Buy CELO on exchanges (Coinbase, Binance, etc.)
- Transfer to your deployment wallet
- Minimum recommended: **0.5-1 CELO** for gas fees

### Option 3: Bridge from Other Networks
- Use [Portal Bridge](https://www.portalbridge.com/)
- Bridge from Ethereum, Polygon, etc. to Celo

---

## 🔐 Step 4: Security Checklist

Before deploying to mainnet:

- [ ] **New Wallet Created** - Never reuse testnet wallets
- [ ] **Private Key Secured** - Store in password manager, not in code
- [ ] **Backup Created** - Save seed phrase in multiple secure locations
- [ ] **Gas Funds Verified** - Wallet has sufficient CELO for operations
- [ ] **Test Transaction** - Send small amount first to verify
- [ ] **Environment Variables Set** - Render dashboard updated with mainnet config
- [ ] **CORS Updated** - `application.properties` allows your frontend domain
- [ ] **Contract Addresses Updated** - If using custom smart contracts

---

## 🚀 Step 5: Deployment Process

### Backend Deployment

1. **Update `application.properties` locally** (or use env vars only)
2. **Push changes to GitHub:**
   ```bash
   git add .
   git commit -m "Update configuration for Celo mainnet deployment"
   git push origin main
   ```

3. **Update Render Environment Variables:**
   - Go to Render Dashboard
   - Select your backend service
   - Navigate to **Environment** tab
   - Update `CELO_WALLET_PRIVATE_KEY` with mainnet wallet
   - Click **Save Changes**
   - Render will auto-redeploy

4. **Verify Backend:**
   ```bash
   curl https://your-backend.onrender.com/api/health
   ```

### Frontend Deployment

1. **Update `useWallet.ts`** with mainnet config
2. **Build locally to test:**
   ```bash
   cd AtlasBrawler_JS_Frontend
   npm install
   npm run build
   ```

3. **Push to GitHub:**
   ```bash
   git add .
   git commit -m "Configure frontend for Celo mainnet"
   git push origin main
   ```

4. **Render Auto-Deploy:**
   - Frontend will automatically rebuild and deploy
   - Monitor build logs in Render dashboard

5. **Verify Frontend:**
   - Visit your deployed URL
   - Open MetaMask/MiniPay
   - Should prompt to switch to Celo Mainnet
   - Connect wallet and verify network

---

## 🧪 Step 6: Testing Checklist

After mainnet deployment:

- [ ] **Wallet Connection** - MetaMask/MiniPay connects to Celo Mainnet
- [ ] **Network Switch** - App prompts to switch if on wrong network
- [ ] **Player Registration** - Can register new players
- [ ] **Game Session** - Can play and complete game sessions
- [ ] **Reward Claiming** - Can claim rewards (uses real CELO gas!)
- [ ] **Balance Updates** - Player balances update correctly
- [ ] **Transaction Verification** - Check transactions on [Celoscan](https://celoscan.io/)
- [ ] **Mobile MiniPay** - Test on mobile MetaMask/MiniPay browser

---

## 📊 Post-Deployment Monitoring

### Check Backend Logs
```bash
# In Render Dashboard:
# Backend Service → Logs tab
# Monitor for any errors related to blockchain transactions
```

### Monitor Wallet Balance
- Check deployment wallet on [Celoscan](https://celoscan.io/)
- Ensure sufficient CELO remains for gas fees
- Set up alerts for low balance

### Track Transactions
All reward transactions will be visible on Celoscan:
```
https://celoscan.io/address/YOUR_WALLET_ADDRESS
```

---

## 🎯 Hackathon Submission Checklist

Once mainnet deployment is complete, reply to organizers with:

✅ **Mainnet Confirmation:**
> "Atlas Brawler is now live on Celo Mainnet"

✅ **Public URL:**
```
Frontend: https://atlasbrawler-js-frontend.onrender.com
Backend API: https://your-backend.onrender.com/api
```

✅ **Wallet Address (for verification):**
```
Deployment Wallet: 0xYOUR_WALLET_ADDRESS
Celoscan: https://celoscan.io/address/0xYOUR_WALLET_ADDRESS
```

✅ **Karma Gap Submission:**
> "Submitted to Karma Gap: [your_karma_gap_link]"

---

## 🆘 Troubleshooting

### Issue: "Insufficient funds for gas"
**Solution:** Add more CELO to your deployment wallet

### Issue: "Wrong network" warning in frontend
**Solution:** 
- Clear browser cache
- Verify `chainId` in `useWallet.ts` is `0xa4ec`
- Check MetaMask is on Celo Mainnet

### Issue: Backend blockchain transactions failing
**Solution:**
- Verify RPC URL: `https://forno.celo.org`
- Check private key is correctly set in Render env vars
- Ensure wallet has CELO for gas

### Issue: MiniPay not connecting
**Solution:**
- Test on mobile MetaMask browser
- Verify network config in `useWallet.ts`
- Check CORS settings allow your frontend domain

---

## 📞 Support

**Need Mainnet CELO?**
Email hackathon organizers as mentioned in prize announcement.

**Technical Issues?**
- Check [Celo Docs](https://docs.celo.org/)
- Visit [Celo Discord](https://discord.gg/celo)
- Review transaction logs on [Celoscan](https://celoscan.io/)

---

## 🎉 Congratulations!

You're deploying a **top 30 HackersDAO project** to Celo Mainnet! 🏆

Built with ❤️ for **Celo Africa DAO**  
Skateboarding into the future of Web3! 🛹⛓️
