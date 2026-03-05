*NOT DEPLOYED ANYWHERE, MORE LIKE A PROTOTYPE*

# KEYGEN FM — Chiptune & Cracktro Radio 🎵

A custom retro-styled web player for **Keygen FM**, a 24/7 chiptune, keygen music, cracktro & demoscene internet radio station powered by [AzuraCast](https://www.azuracast.com/).

> 🔊 **Live:** [https://keygen-fm.kodatek.app](https://keygen-fm.kodatek.app)

## Features

- **Cracktro aesthetic** — starfield background, CRT scanline overlay, pixel fonts, neon glow effects
- **Real-time now-playing** — polls AzuraCast API for current track, artist, listener count
- **Audio visualizer** — frequency bars driven by Web Audio API
- **Song history** — last 5 played tracks with timestamps
- **Keyboard controls** — Space to play/pause
- **Fully responsive** — works on mobile, tablet, desktop
- **Zero dependencies** — single HTML file, no build tools needed
- **SEO-ready** — Open Graph meta tags, semantic HTML

## Quick Start

1. Clone this repo
2. Edit `CONFIG` at the top of the `<script>` section in `index.html`:
   ```js
   const CONFIG = {
     azuracastUrl: 'https://keygen-fm.kodatek.app',
     stationId: '1',           // your station ID or shortcode
     pollInterval: 15000,      // API poll interval (ms)
     streamUrl: 'https://keygen-fm.kodatek.app/listen/keygen_fm/radio.mp3',
   };
   ```
3. Deploy — just serve `index.html` from any web server, or use it as your AzuraCast custom page

## Configuration

| Key | Description |
|-----|------------|
| `azuracastUrl` | Your AzuraCast installation URL (no trailing slash) |
| `stationId` | Station short name or numeric ID |
| `pollInterval` | How often to refresh now-playing data (default: 15s) |
| `streamUrl` | Direct mount point URL for the audio stream |

### Finding your stream URL

In AzuraCast, go to **Station → Mount Points**. Your stream URL typically follows one of these patterns:
- `https://your-domain.com/listen/station_name/radio.mp3`
- `https://your-domain.com/radio/8000/radio.mp3`

### CORS note

If the visualizer doesn't work, your AzuraCast instance needs to send proper CORS headers for the audio stream. The `crossOrigin = 'anonymous'` attribute is set on the audio element. If CORS isn't possible, the player will still work — just with an idle visualizer animation.

## Deployment Options

### Option 1: Standalone hosting (Nginx, Caddy, etc.)
Just serve the `index.html` file. No build step needed.

### Option 2: AzuraCast Custom Branding
Copy the CSS into **Admin → Custom Branding → Custom CSS for Public Pages** and the JS into **Custom JS for Public Pages**. This overrides the default AzuraCast player.

### Option 3: GitHub Pages
Enable GitHub Pages on this repo — it'll serve `index.html` directly.

## Tech Stack

- Vanilla HTML/CSS/JS (no frameworks, no build tools)
- Web Audio API for visualizer
- Canvas API for starfield + visualizer
- AzuraCast REST API (`/api/nowplaying/{station}`)
- Google Fonts: Press Start 2P, Share Tech Mono

## License

MIT
