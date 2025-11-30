# 🚀 Atlas Brawler - Deployment Guide

```
   ╔═══════════════════════════════════════╗
   ║    DEPLOY TO RENDER.COM               ║
   ║    Fast • Free Tier • Auto-Deploy    ║
   ╚═══════════════════════════════════════╝
```

## 📦 What You're Deploying

- **Frontend**: React PWA (mobile-ready)
- **Backend**: Spring Boot API + MySQL
- **Blockchain**: Celo Alfajores Testnet

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
```
SPRING_DATASOURCE_URL=jdbc:mysql://YOUR_DB_HOST:3306/atlas_brawler
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password
CELO_WALLET_PRIVATE_KEY=your_private_key
JWT_SECRET=your_super_secret_key
SPRING_PROFILES_ACTIVE=production
```

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

## 🗄️ Database Setup (MySQL)

### Option 1: Render MySQL (Recommended)
1. Create **MySQL** instance on Render
2. Copy connection string
3. Add to backend environment variables

### Option 2: External DB (Railway, PlanetScale)
1. Create database on your provider
2. Get connection string
3. Add to backend environment variables

---

## 🔧 Post-Deployment Checklist

- [ ] Backend health check passes: `/api/health`
- [ ] Frontend loads and connects to backend
- [ ] Wallet connection works (Celo testnet)
- [ ] Game sessions save properly
- [ ] PWA install prompt appears on mobile
- [ ] Audio plays correctly
- [ ] Obstacles and physics work

---

## 🐛 Troubleshooting

**Frontend can't connect to backend?**
- Check CORS settings in `application.properties`
- Verify `VITE_API_URL` is correct
- Check backend logs on Render dashboard

**Database connection fails?**
- Verify MySQL is running
- Check database credentials
- Ensure IP whitelist includes Render IPs

**PWA not installing?**
- Must use HTTPS (Render provides this)
- Check manifest.json generated in build
- Test on real mobile device

---

## 💰 Free Tier Limits (Render)

- **Backend**: 750 hours/month (sleeps after 15min idle)
- **Frontend**: Unlimited static hosting
- **Database**: 1GB storage (MySQL addon)

**Tip**: Backend sleeps when inactive. First request after sleep takes ~30s to wake up.

---

## 🎮 Ready to Play!

Share your deployed game URL with friends and let them skate to earn! 🛹💨

Built for **Celo Africa DAO Hackathon** 🌍
