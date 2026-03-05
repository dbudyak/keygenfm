/* ═══════════════════════════════════════════════════════════
   KEYGEN FM — ZX SPECTRUM THEME
   AzuraCast Custom JS for Public Pages
   Paste into: Admin → Custom Branding → Custom JS for Public Pages
   ═══════════════════════════════════════════════════════════ */

// Replace Playlist button link with direct stream
var checkPlaylist = setInterval(function() {
  var links = document.querySelectorAll('a');
  for (var i = 0; i < links.length; i++) {
    if (links[i].textContent.includes('Playlist') || links[i].href.includes('playlist')) {
links[i].href = 'data:audio/x-mpegurl,' + encodeURIComponent('#EXTM3U\n#EXTINF:-1,Keygen FM\nhttp://radio.kodatek.fi:8880/listen/keygen-fm/radio.mp3');
links[i].download = 'keygen-fm.m3u';
links[i].textContent = 'DOWNLOAD M3U';
      links[i].style.visibility = 'visible';
      clearInterval(checkPlaylist);
      break;
    }
  }
}, 100);


document.title = 'Keygen FM — 24/7 Chiptune, Cracktro & Demoscene Radio';
var meta = document.createElement('meta');
meta.name = 'description';
meta.content = 'Keygen FM is a 24/7 internet radio station playing chiptune, keygen, cracktro and demoscene music. Tracker modules, 8-bit melodies, and digital underground sounds from Finland.';
document.head.appendChild(meta);


(function () {
  'use strict';

  // ── CONFIG ──
  const ZX = {
    colors: ['#d70000', '#0000d7', '#d700d7', '#00d700', '#00d7d7', '#d7d700'],
    pixelSize: 4,
    rainSpeed: 0.4,
    maxDrops: 35,
  };

  // ── WAIT FOR DOM ──
  function onReady(fn) {
    if (document.readyState !== 'loading') fn();
    else document.addEventListener('DOMContentLoaded', fn);
  }

  onReady(function () {

    // ══════════════════════════════════════
    // 1. PIXEL RAIN BACKGROUND ANIMATION
    //    Subtle falling pixels behind the card
    // ══════════════════════════════════════
    const canvas = document.createElement('canvas');
    canvas.id = 'zx-rain';
    Object.assign(canvas.style, {
      position: 'fixed',
      top: '0',
      left: '0',
      width: '100%',
      height: '100%',
      zIndex: '0',
      pointerEvents: 'none',
      opacity: '0.12',
    });
    document.body.prepend(canvas);

    const ctx = canvas.getContext('2d');
    let drops = [];

    function resizeCanvas() {
      canvas.width = window.innerWidth;
      canvas.height = window.innerHeight;
    }
    resizeCanvas();
    window.addEventListener('resize', resizeCanvas);

    function spawnDrop() {
      drops.push({
        x: Math.floor(Math.random() * canvas.width / ZX.pixelSize) * ZX.pixelSize,
        y: -ZX.pixelSize,
        color: ZX.colors[Math.floor(Math.random() * ZX.colors.length)],
        speed: ZX.rainSpeed + Math.random() * 0.6,
        size: ZX.pixelSize,
      });
    }

    function animateRain() {
      ctx.clearRect(0, 0, canvas.width, canvas.height);

      if (drops.length < ZX.maxDrops && Math.random() > 0.85) {
        spawnDrop();
      }

      for (let i = drops.length - 1; i >= 0; i--) {
        const d = drops[i];
        d.y += d.speed;
        ctx.fillStyle = d.color;
        ctx.fillRect(d.x, d.y, d.size, d.size);

        // leave a fading trail pixel
        ctx.fillStyle = d.color;
        ctx.globalAlpha = 0.3;
        ctx.fillRect(d.x, d.y - d.size, d.size, d.size);
        ctx.globalAlpha = 0.1;
        ctx.fillRect(d.x, d.y - d.size * 2, d.size, d.size);
        ctx.globalAlpha = 1;

        if (d.y > canvas.height) {
          drops.splice(i, 1);
        }
      }
      requestAnimationFrame(animateRain);
    }
    animateRain();

    // ══════════════════════════════════════
    // 2. BLINKING CURSOR ON NOW-PLAYING
    // ══════════════════════════════════════
    function addCursor() {
      const titleEl = document.querySelector(
        '.now-playing-title, .now-playing-details .now-playing-title'
      );
      if (titleEl && !titleEl.classList.contains('zx-cursor')) {
        titleEl.classList.add('zx-cursor');
      }
    }
    addCursor();

    // Re-apply cursor when now-playing updates (Vue re-renders the element)
    const observer = new MutationObserver(function () {
      addCursor();
    });
    const npContainer = document.querySelector(
      '.now-playing-details, .radio-player-widget, .card-body, .card'
    );
    if (npContainer) {
      observer.observe(npContainer, { childList: true, subtree: true });
    }

    // ══════════════════════════════════════
    // 3. ZX SPECTRUM "LOADING" FLASH
    //    Brief color flash on song change
    // ══════════════════════════════════════
    let lastSongText = '';

    document.addEventListener('now-playing', function (np_new) {
      // np_new might be the event data or we read from DOM
      const titleEl = document.querySelector(
        '.now-playing-title, .now-playing-details .now-playing-title'
      );
      if (!titleEl) return;
      const currentText = titleEl.textContent.trim();

      if (currentText && currentText !== lastSongText) {
        lastSongText = currentText;

        // Flash the card border with a random ZX color
        const card = document.querySelector('.public-page .card');
        if (card) {
          const flashColor = ZX.colors[Math.floor(Math.random() * ZX.colors.length)];
          card.style.borderColor = flashColor;
          card.style.boxShadow = '4px 4px 0 ' + flashColor + ', inset 0 0 0 1px #fff, inset 0 0 0 2px #a0a0a0';
          setTimeout(function () {
            card.style.borderColor = '#000';
            card.style.boxShadow = '4px 4px 0 #000, inset 0 0 0 1px #fff, inset 0 0 0 2px #a0a0a0';
          }, 800);
        }

        // Re-apply cursor class after Vue re-render
        addCursor();
      }
    });

    // Also poll for changes if the event doesn't fire (fallback)
    setInterval(function () {
      const titleEl = document.querySelector(
        '.now-playing-title, .now-playing-details .now-playing-title'
      );
      if (!titleEl) return;
      const currentText = titleEl.textContent.trim();
      if (currentText && currentText !== lastSongText) {
        lastSongText = currentText;
        addCursor();
      }
    }, 5000);

    // ══════════════════════════════════════
    // 4. ZX SPECTRUM FOOTER STATUS LINE
    // ══════════════════════════════════════
    const footer = document.createElement('div');
    Object.assign(footer.style, {
      position: 'fixed',
      bottom: '8px',
      left: '0',
      right: '0',
      textAlign: 'center',
      fontFamily: "'Press Start 2P', monospace",
      fontSize: '7px',
      color: '#606060',
      letterSpacing: '1px',
      zIndex: '10',
      pointerEvents: 'none',
    });
    footer.textContent = '© KEYGEN FM — 128K — CHIPTUNE/CRACKTRO/DEMOSCENE';
    document.body.appendChild(footer);

    // ══════════════════════════════════════
    // 5. KEYBOARD EASTER EGG
    //    Type "LOAD" for a ZX loading animation
    // ══════════════════════════════════════
    let keyBuffer = '';

    document.addEventListener('keydown', function (e) {
      // Don't capture if user is typing in an input
      if (e.target.tagName === 'INPUT' || e.target.tagName === 'TEXTAREA') return;

      keyBuffer += e.key.toUpperCase();
      if (keyBuffer.length > 10) keyBuffer = keyBuffer.slice(-10);

      if (keyBuffer.includes('LOAD')) {
        keyBuffer = '';
        showLoadingScreen();
      }
    });

    function showLoadingScreen() {
      const overlay = document.createElement('div');
      Object.assign(overlay.style, {
        position: 'fixed',
        inset: '0',
        background: '#c0c0c0',
        zIndex: '99999',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        fontFamily: "'Press Start 2P', monospace",
        fontSize: '12px',
        color: '#000',
      });

      // ZX loading stripes
      const stripes = document.createElement('div');
      Object.assign(stripes.style, {
        width: '200px',
        height: '120px',
        border: '3px solid #000',
        overflow: 'hidden',
        marginBottom: '20px',
        position: 'relative',
      });

      // Animate colored bars
      const colors = ['#d70000', '#d7d700', '#00d700', '#00d7d7', '#0000d7', '#d700d7', '#d7d7d7', '#000000'];
      for (let i = 0; i < colors.length; i++) {
        const bar = document.createElement('div');
        Object.assign(bar.style, {
          width: '100%',
          height: (120 / colors.length) + 'px',
          background: colors[i],
          animation: 'zx-bar-flash 0.15s ' + (i * 0.04) + 's infinite alternate',
        });
        stripes.appendChild(bar);
      }
      overlay.appendChild(stripes);

      const text = document.createElement('div');
      text.textContent = 'LOADING...';
      text.style.animation = 'zx-cursor 0.5s step-end infinite';
      overlay.appendChild(text);

      // Add keyframe if not exists
      if (!document.getElementById('zx-bar-flash-style')) {
        const style = document.createElement('style');
        style.id = 'zx-bar-flash-style';
        style.textContent = '@keyframes zx-bar-flash { 0% { opacity: 1; } 100% { opacity: 0.3; } }';
        document.head.appendChild(style);
      }

      document.body.appendChild(overlay);

      // Remove after 2.5 seconds
      setTimeout(function () {
        overlay.style.transition = 'opacity 0.3s';
        overlay.style.opacity = '0';
        setTimeout(function () { overlay.remove(); }, 300);
      }, 2500);
    }

    // ══════════════════════════════════════
    // 6. UPDATE PAGE TITLE
    // ══════════════════════════════════════
    const origTitle = document.title;
    setInterval(function () {
      const titleEl = document.querySelector(
        '.now-playing-title, .now-playing-details .now-playing-title'
      );
      if (titleEl && titleEl.textContent.trim()) {
        document.title = '♫ ' + titleEl.textContent.trim() + ' — KEYGEN FM';
      }
    }, 5000);
    

  });
})();

// Replace "Powered by AzuraCast" — waits for Vue to render
(function() {
  function replaceFooter() {
    // Try multiple selectors to find it
    var allLinks = document.querySelectorAll('a');
    for (var i = 0; i < allLinks.length; i++) {
      var el = allLinks[i];
      if (el.textContent.toLowerCase().includes('azuracast') && 
          el.href && el.href.includes('azuracast')) {
        // Found it — replace parent or the link itself
        var parent = el.parentElement;
        if (parent) {
          parent.innerHTML = '<a href="https://kodatek.app" target="_blank" ' +
            'style="font-family:\'Press Start 2P\',monospace;font-size:7px;' +
            'color:#606060;text-decoration:none;">' +
            '© KEYGEN FM — kodatek.app</a>';
        }
        return true;
      }
    }
    return false;
  }

  // Poll until Vue renders the footer (up to 10 seconds)
  var attempts = 0;
  var timer = setInterval(function() {
    if (replaceFooter() || attempts > 40) {
      clearInterval(timer);
    }
    attempts++;
  }, 250);

  // Also re-check on Vue re-renders
  var obs = new MutationObserver(function() {
    replaceFooter();
  });
  obs.observe(document.body, { childList: true, subtree: true });
})();

var schema = document.createElement('script');
schema.type = 'application/ld+json';
schema.text = JSON.stringify({
  "@context": "https://schema.org",
  "@type": "RadioStation",
  "name": "Keygen FM",
  "url": "https://keygen-fm.kodatek.app",
  "description": "24/7 chiptune, keygen, cracktro and demoscene internet radio from Finland",
  "genre": ["Chiptune", "8-bit", "Keygen", "Demoscene", "Electronic"],
  "broadcastFrequency": "Internet Only",
  "areaServed": "Worldwide",
  "broadcaster": {
    "@type": "Organization",
    "name": "Kodatek"
  }
});
document.head.appendChild(schema);
