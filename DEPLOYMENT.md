# 🚀 Atlas Brawler - Deployment Guide

```
   ╔═══════════════════════════════════════╗
   ║    DEPLOY TO RENDER.COM               ║
   ║    Fast • Free Tier • Auto-Deploy    ║
   ╚═══════════════════════════════════════╝
```

## 📦 What You're Deploying

- **Frontend**: React PWA (mobile-ready) 
- **Backend**: Spring Boot API + PostgreSQL
- **Blockchain**: **Celo Mainnet** (Chain ID: 42220)

**🏆 HackersDAO Winner:** This project placed in the top 30 at the Celo Africa DAO Hackathon!

**📚 For detailed mainnet migration instructions, see [MAINNET_MIGRATION.md](MAINNET_MIGRATION.md)**

---

## 🎯 Deploy Backend (Spring Boot)

### 1. Push to GitHub
```bash
cd 1.backend/AtlasBrawler_SpringBoot_Backend
git init
git add .
git commit -m "Ready for deployment"
git remote add origin YOUR_REPO_URL
git push -u origin main
```

### 2. Create Render Web Service
1. Go to [render.com](https://render.com)
2. Click **New** → **Web Service**
3. Connect your GitHub repo
4. Select `1.backend/AtlasBrawler_SpringBoot_Backend` directory

### 3. Configure Build Settings
- **Name**: `atlas-brawler-backend`
- **Environment**: `Java`
- **Build Command**: `mvn clean package -DskipTests`
- **Start Command**: `java -jar target/atlas-brawler-0.0.1-SNAPSHOT.jar`

### 4. Add Environment Variables

**⚠️ IMPORTANT: Use MAINNET configuration**

```
SPRING_DATASOURCE_URL=jdbc:postgresql://YOUR_DB_HOST:5432/atlas_brawler
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password
CELO_WALLET_PRIVATE_KEY=your_mainnet_private_key
JWT_SECRET=your_super_secret_key
SPRING_PROFILES_ACTIVE=production
```

**🔐 Security Notes:**
- Use a **NEW mainnet wallet** (never reuse testnet keys)
- Ensure wallet has **0.5-1 CELO** minimum for gas fees
- Store private keys securely (password manager, encrypted vault)

### 5. Deploy
Click **Create Web Service** and wait for build to complete.

Your backend will be live at: `https://atlas-brawler-backend.onrender.com`

---

## 🎨 Deploy Frontend (React PWA)

### 1. Update API URL
Edit `2.frontend/AtlasBrawler_JS_Frontend/.env`:
```
VITE_API_URL=https://atlas-brawler-backend.onrender.com/api
```

### 2. Build Locally (Test)
```bash
cd 2.frontend/AtlasBrawler_JS_Frontend
npm install
npm run build
```

### 3. Push to GitHub
```bash
git add .
git commit -m "PWA ready for deployment"
git push
```

### 4. Create Render Static Site
1. Go to [render.com](https://render.com)
2. Click **New** → **Static Site**
3. Connect your GitHub repo
4. Select `2.frontend/AtlasBrawler_JS_Frontend` directory

### 5. Configure Build Settings
- **Name**: `atlas-brawler-frontend`
- **Build Command**: `npm install && npm run build`
- **Publish Directory**: `dist`

### 6. Add Environment Variables
```
VITE_API_URL=https://atlas-brawler-backend.onrender.com/api
```

### 7. Deploy
Click **Create Static Site**.

Your game will be live at: `https://atlas-brawler-frontend.onrender.com`

---

## 📱 PWA Features

After deployment, users can:
- ✅ **Add to Home Screen** (iOS & Android)
- ✅ **Play Offline** (service worker caching)
- ✅ **Full-screen mode** (no browser bars)
- ✅ **Fast loading** (asset caching)

### iOS Installation
1. Open Safari → Visit your site
2. Tap Share button
3. Tap "Add to Home Screen"
4. Tap "Add"

### Android Installation
1. Open Chrome → Visit your site
2. Tap menu (⋮)
3. Tap "Install App"
4. Tap "Install"

---

## 🗄️ Database Setup (PostgreSQL)

### Option 1: Render PostgreSQL (Recommended)
1. Create **PostgreSQL** instance on Render
2. Copy connection string from Render dashboard
3. Add to backend environment variables

### Option 2: External DB (Railway, Supabase, Neon)
1. Create PostgreSQL database on your provider
2. Get connection string
3. Add to backend environment variables

---

## 🔧 Post-Deployment Checklist

- [ ] Backend health check passes: `/api/health`
- [ ] Frontend loads and connects to backend
- [ ] Wallet connection works (**Celo Mainnet** - Chain ID: 42220)
- [ ] Network switching prompts correctly for mainnet
- [ ] Game sessions save properly
- [ ] Rewards claim with real gas fees
- [ ] Transactions visible on [Celoscan](https://celoscan.io/)
- [ ] PWA install prompt appears on mobile
- [ ] MiniPay integration works on mobile
- [ ] Audio plays correctly
- [ ] Obstacles and physics work

---

## 🐛 Troubleshooting

**Frontend can't connect to backend?**
- Check CORS settings in `application.properties`
- Verify `VITE_API_URL` is correct
- Check backend logs on Render dashboard

**Database connection fails?**
- Verify PostgreSQL is running
- Check database credentials in Render environment variables
- Ensure IP whitelist includes Render IPs

**Wrong network / Can't connect wallet?**
- Verify wallet is on **Celo Mainnet (42220)**
- Check `useWallet.ts` has correct mainnet config
- Try manually adding Celo Mainnet in MetaMask

**Transaction failures / Gas errors?**
- Check deployment wallet has sufficient CELO
- Verify private key is correctly set in Render env vars
- Check transactions on [Celoscan](https://celoscan.io/)

**PWA not installing?**
- Must use HTTPS (Render provides this)
- Check manifest.json generated in build
- Test on real mobile device

---

## 💰 Free Tier Limits (Render)

- **Backend**: 750 hours/month (sleeps after 15min idle)
- **Frontend**: Unlimited static hosting
- **Database**: 1GB storage (PostgreSQL addon)

**Tip**: Backend sleeps when inactive. First request after sleep takes ~30s to wake up.

**💸 Gas Costs (Celo Mainnet):**
- Celo mainnet has very low gas fees (~$0.001-0.01 per transaction)
- Ensure deployment wallet has minimum **0.5-1 CELO** for operations
- Monitor wallet balance on [Celoscan](https://celoscan.io/)

---

## 🎮 Ready to Play!

Share your deployed game URL with friends and let them skate to earn **real CELO rewards**! 🛹💨

---

## 🏆 Hackathon Submission

**For HackersDAO prize winners deploying to mainnet:**

1. ✅ Deploy backend with mainnet configuration
2. ✅ Deploy frontend with mainnet wallet config
3. ✅ Test everything works on Celo Mainnet
4. ✅ Submit to [Karma Gap](https://www.karmahq.xyz/)
5. ✅ Reply to organizers with:
   - Mainnet deployment confirmation
   - Public URL
   - Karma Gap submission link

**See [MAINNET_MIGRATION.md](MAINNET_MIGRATION.md) for detailed migration guide.**

---

Built for **Celo Africa DAO Hackathon** 🌍  
**🏆 Top 30 Winner** 🎉
