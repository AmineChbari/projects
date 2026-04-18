import arcImgSrc     from './assets/images/arc.png';
import cibleImgSrc   from './assets/images/cible.png';
import flechesImgSrc from './assets/images/fleches.png';
import birdLRImgSrc  from './assets/images/oiseau-voleur-gauche-droite.png';
import birdRLImgSrc  from './assets/images/oiseau-voleur-droite-gauche.png';

const CW          = 500;
const CH          = 580;
const GROUND_Y    = 545;
const ARCHER_Y    = 496;
const PIXEL_FONT  = '"Press Start 2P", monospace';

function loadImg(src) { const i = new Image(); i.src = src; return i; }

function overlap(a, b) {
  return a.x < b.x + b.w && a.x + a.w > b.x && a.y < b.y + b.h && a.y + a.h > b.y;
}

// ── Arrow ─────────────────────────────────────
class Arrow {
  constructor(x, y) {
    this.x = x; this.y = y;
    this.vy = -11; this.active = true;
  }
  move() { this.y += this.vy; if (this.y < -30) this.active = false; }
  get rect() { return { x: this.x - 3, y: this.y, w: 6, h: 24 }; }
  draw(ctx) {
    if (!this.active) return;
    ctx.save();
    ctx.fillStyle = '#FFE600'; ctx.shadowColor = '#FFE600'; ctx.shadowBlur = 10;
    ctx.fillRect(this.x - 2, this.y, 4, 22);
    ctx.beginPath();
    ctx.moveTo(this.x, this.y - 8); ctx.lineTo(this.x - 4, this.y + 1); ctx.lineTo(this.x + 4, this.y + 1);
    ctx.closePath(); ctx.fill();
    ctx.restore();
  }
}

// ── Target ────────────────────────────────────
class Target {
  static R = 34;
  constructor(x, y, dx) {
    this.x = x; this.y = y; this.dx = dx;
    this.active = true; this.hitTimer = 0;
  }
  move() {
    this.x += this.dx;
    if (this.x - Target.R < 0 || this.x + Target.R > CW) this.dx = -this.dx;
    if (this.hitTimer > 0) this.hitTimer--;
  }
  scoreAt(ax, ay) {
    const d = Math.hypot(ax - this.x, ay - this.y);
    if (d < Target.R * 0.15) return 50;
    if (d < Target.R * 0.35) return 30;
    if (d < Target.R * 0.60) return 20;
    if (d < Target.R)        return 10;
    return 0;
  }
  draw(ctx, img) {
    if (!this.active) return;
    ctx.save();
    if (img && img.complete && img.naturalWidth > 0) {
      ctx.drawImage(img, this.x - Target.R, this.y - Target.R, Target.R * 2, Target.R * 2);
    } else {
      const rings = ['#fff','#000','#0af','#f30','#ffe600'];
      const radii = [34, 26, 19, 12, 5];
      for (let i = 0; i < rings.length; i++) {
        ctx.fillStyle = rings[i];
        ctx.beginPath(); ctx.arc(this.x, this.y, radii[i], 0, Math.PI * 2); ctx.fill();
      }
    }
    if (this.hitTimer > 0) {
      ctx.globalAlpha = this.hitTimer / 20;
      ctx.strokeStyle = '#FFE600'; ctx.shadowColor = '#FFE600'; ctx.shadowBlur = 18;
      ctx.lineWidth = 3;
      ctx.beginPath(); ctx.arc(this.x, this.y, Target.R + 10, 0, Math.PI * 2); ctx.stroke();
    }
    ctx.restore();
  }
}

// ── Bird ──────────────────────────────────────
class Bird {
  static W = 76; static H = 44;
  constructor(fromLeft, y) {
    this.fromLeft = fromLeft;
    this.x = fromLeft ? -Bird.W - 10 : CW + 10;
    this.y = y;
    this.spd = 1.8 + Math.random() * 2.2;
    this.active = true;
  }
  move() {
    this.x += this.fromLeft ? this.spd : -this.spd;
    if (this.x > CW + Bird.W + 10 || this.x < -Bird.W - 10) this.active = false;
  }
  get rect() { return { x: this.x, y: this.y, w: Bird.W, h: Bird.H }; }
  draw(ctx, imgLR, imgRL) {
    if (!this.active) return;
    const img = this.fromLeft ? imgLR : imgRL;
    ctx.save();
    if (img && img.complete && img.naturalWidth > 0) {
      ctx.drawImage(img, this.x, this.y, Bird.W, Bird.H);
    } else {
      ctx.fillStyle = '#ff8800';
      ctx.fillRect(this.x, this.y, Bird.W, Bird.H / 2);
    }
    ctx.restore();
  }
}

// ── ArrowBundle ───────────────────────────────
class ArrowBundle {
  static W = 44; static H = 44;
  constructor(x) {
    this.x = x; this.y = -ArrowBundle.H;
    this.vy = 1.4; this.active = true;
  }
  move() { this.y += this.vy; if (this.y > CH + 10) this.active = false; }
  get rect() { return { x: this.x - ArrowBundle.W / 2, y: this.y, w: ArrowBundle.W, h: ArrowBundle.H }; }
  draw(ctx, img) {
    if (!this.active) return;
    const rx = this.x - ArrowBundle.W / 2;
    ctx.save();
    if (img && img.complete && img.naturalWidth > 0) {
      ctx.drawImage(img, rx, this.y, ArrowBundle.W, ArrowBundle.H);
    } else {
      ctx.fillStyle = '#39FF14';
      ctx.fillRect(rx, this.y, ArrowBundle.W, ArrowBundle.H);
    }
    ctx.strokeStyle = '#39FF14'; ctx.shadowColor = '#39FF14'; ctx.shadowBlur = 14;
    ctx.lineWidth = 2;
    ctx.strokeRect(rx, this.y, ArrowBundle.W, ArrowBundle.H);
    ctx.restore();
  }
}

// ── Flash message ─────────────────────────────
class Flash {
  constructor(x, y, text, color = '#FFE600', dur = 50) {
    this.x = x; this.y = y; this.text = text;
    this.color = color; this.life = dur; this.maxLife = dur;
  }
  update() { this.y -= 0.6; this.life--; }
  get alive() { return this.life > 0; }
  draw(ctx) {
    ctx.save();
    ctx.globalAlpha = this.life / this.maxLife;
    ctx.font = `bold 11px ${PIXEL_FONT}`;
    ctx.textAlign = 'center';
    ctx.fillStyle = this.color; ctx.shadowColor = this.color; ctx.shadowBlur = 10;
    ctx.fillText(this.text, this.x, this.y);
    ctx.restore();
  }
}

// ── Game ──────────────────────────────────────
export default class Game {
  #canvas; #ctx; #req = null; #running = false;

  #archerX; #keys;
  #arrows; #targets; #birds; #bundles; #flashes; #stars;
  #score; #lives; #nbArrows;
  #tTarget; #tBird; #tBundle;

  #imgArc; #imgCible; #imgFleches; #imgBirdLR; #imgBirdRL;

  constructor(canvas) {
    this.#canvas   = canvas;
    this.#ctx      = canvas.getContext('2d');
    this.#imgArc   = loadImg(arcImgSrc);
    this.#imgCible = loadImg(cibleImgSrc);
    this.#imgFleches = loadImg(flechesImgSrc);
    this.#imgBirdLR  = loadImg(birdLRImgSrc);
    this.#imgBirdRL  = loadImg(birdRLImgSrc);
    this.#stars = Array.from({ length: 55 }, () => ({
      x: Math.random() * CW, y: Math.random() * 220,
      r: Math.random() * 1.5 + 0.5, a: 0.4 + Math.random() * 0.6
    }));
    this.#reset();
    this.#setupControls();
    this.#drawIdle();
  }

  get canvas() { return this.#canvas; }

  #reset() {
    this.#archerX = CW / 2;
    this.#keys    = { left: false, right: false };
    this.#arrows  = []; this.#targets = []; this.#birds = [];
    this.#bundles = []; this.#flashes = [];
    this.#score = 0; this.#lives = 3; this.#nbArrows = 10;
    this.#tTarget = 0; this.#tBird = 120; this.#tBundle = 0;
    this.#updateHUD();
  }

  #setupControls() {
    this.#canvas.addEventListener('click', () => { if (this.#running) this.#shoot(); });

    window.addEventListener('keydown', (e) => {
      if (e.key === 'ArrowLeft')  this.#keys.left  = true;
      if (e.key === 'ArrowRight') this.#keys.right = true;
      if ((e.key === ' ' || e.key === 'ArrowUp') && this.#running) {
        e.preventDefault(); this.#shoot();
      }
    });
    window.addEventListener('keyup', (e) => {
      if (e.key === 'ArrowLeft')  this.#keys.left  = false;
      if (e.key === 'ArrowRight') this.#keys.right = false;
    });

    document.getElementById('stopAndStartGame').addEventListener('click', () => this.#toggle());
  }

  #toggle() {
    const btn = document.getElementById('stopAndStartGame');
    if (this.#lives === 0) {
      this.#reset();
      this.#running = true;
      btn.textContent = '⏸ PAUSE';
      this.#req = requestAnimationFrame(this.#loop.bind(this));
      return;
    }
    if (this.#running) {
      this.#running = false;
      cancelAnimationFrame(this.#req); this.#req = null;
      btn.textContent = '▶ REPRENDRE';
    } else {
      this.#running = true;
      btn.textContent = '⏸ PAUSE';
      this.#req = requestAnimationFrame(this.#loop.bind(this));
    }
  }

  #shoot() {
    if (this.#nbArrows <= 0) return;
    this.#arrows.push(new Arrow(this.#archerX, ARCHER_Y - 10));
    this.#nbArrows--;
    this.#updateHUD();
  }

  #loop() {
    this.#update();
    this.#render();
    if (this.#running) this.#req = requestAnimationFrame(this.#loop.bind(this));
  }

  #update() {
    if (this.#keys.left  && this.#archerX > 28)          this.#archerX -= 5;
    if (this.#keys.right && this.#archerX < CW - 28)     this.#archerX += 5;

    // Spawn targets every ~110 frames
    if (++this.#tTarget >= 110) {
      this.#tTarget = 0;
      const y  = 55 + Math.random() * 150;
      const dx = (Math.random() > 0.5 ? 1 : -1) * (1.2 + Math.random() * 2.2);
      this.#targets.push(new Target(Target.R + Math.random() * (CW - Target.R * 2), y, dx));
    }
    // Spawn birds every ~180 frames
    if (++this.#tBird >= 180) {
      this.#tBird = 0;
      this.#birds.push(new Bird(Math.random() > 0.5, 215 + Math.random() * 145));
    }
    // Spawn bundles every ~280 frames (only when low on arrows)
    if (++this.#tBundle >= 280) {
      this.#tBundle = 0;
      if (this.#nbArrows < 8)
        this.#bundles.push(new ArrowBundle(50 + Math.random() * (CW - 100)));
    }

    // Move all entities
    this.#arrows.forEach(a => a.move());
    this.#targets.forEach(t => t.move());
    this.#birds.forEach(b => b.move());
    this.#bundles.forEach(b => b.move());
    this.#flashes.forEach(f => f.update());

    this.#arrows  = this.#arrows.filter(a => a.active);
    this.#birds   = this.#birds.filter(b => b.active);
    this.#bundles = this.#bundles.filter(b => b.active);
    this.#flashes = this.#flashes.filter(f => f.alive);

    // Arrow ↔ Target
    for (const arrow of this.#arrows) {
      for (const target of this.#targets) {
        if (!arrow.active || !target.active) continue;
        if (Math.hypot(arrow.x - target.x, arrow.y - target.y) < Target.R) {
          const pts = target.scoreAt(arrow.x, arrow.y);
          this.#score    += pts;
          target.hitTimer = 20;
          target.active   = false;
          arrow.active    = false;
          this.#flashes.push(new Flash(arrow.x, arrow.y, `+${pts}`));
          this.#updateHUD();
        }
      }
    }

    // Arrow ↔ Bird (arrow stolen)
    for (const arrow of this.#arrows) {
      for (const bird of this.#birds) {
        if (!arrow.active || !bird.active) continue;
        if (overlap(arrow.rect, bird.rect)) {
          arrow.active = false;
          this.#flashes.push(new Flash(arrow.x, arrow.y - 10, 'VOLEE!', '#FF2D78', 55));
        }
      }
    }

    // Archer ↔ Bird (lose life)
    const archerRect = { x: this.#archerX - 22, y: ARCHER_Y - 5, w: 44, h: 50 };
    for (const bird of this.#birds) {
      if (!bird.active) continue;
      if (overlap(archerRect, bird.rect)) {
        bird.active = false;
        this.#lives = Math.max(0, this.#lives - 1);
        this.#flashes.push(new Flash(this.#archerX, ARCHER_Y - 20, '-1 VIE', '#FF2D78', 65));
        this.#updateHUD();
        if (this.#lives <= 0) { this.#endGame(); return; }
      }
    }

    // Archer ↔ Bundle (collect +3 arrows)
    for (const bundle of this.#bundles) {
      if (!bundle.active) continue;
      if (overlap(archerRect, bundle.rect)) {
        bundle.active   = false;
        this.#nbArrows  = Math.min(this.#nbArrows + 3, 20);
        this.#flashes.push(new Flash(this.#archerX, ARCHER_Y - 24, '+3 FLECHES', '#39FF14', 55));
        this.#updateHUD();
      }
    }

    // Out of ammo
    if (this.#nbArrows <= 0 && this.#arrows.length === 0) this.#endGame();
  }

  #render() {
    const ctx = this.#ctx;

    // Sky gradient
    const sky = ctx.createLinearGradient(0, 0, 0, CH);
    sky.addColorStop(0, '#020b18'); sky.addColorStop(0.65, '#0d1f3c'); sky.addColorStop(1, '#152810');
    ctx.fillStyle = sky; ctx.fillRect(0, 0, CW, CH);

    // Stars
    ctx.save();
    for (const s of this.#stars) {
      ctx.globalAlpha = s.a; ctx.fillStyle = '#fff';
      ctx.beginPath(); ctx.arc(s.x, s.y, s.r, 0, Math.PI * 2); ctx.fill();
    }
    ctx.restore();

    // Ground
    ctx.fillStyle = '#152810'; ctx.fillRect(0, GROUND_Y, CW, CH - GROUND_Y);
    ctx.fillStyle = '#2a5020'; ctx.fillRect(0, GROUND_Y, CW, 5);

    // Bundles, targets, birds, arrows
    this.#bundles.forEach(b => b.draw(ctx, this.#imgFleches));
    this.#targets.forEach(t => t.draw(ctx, this.#imgCible));
    this.#birds.forEach(b => b.draw(ctx, this.#imgBirdLR, this.#imgBirdRL));
    this.#arrows.forEach(a => a.draw(ctx));
    this.#drawArcher(ctx);
    this.#flashes.forEach(f => f.draw(ctx));
  }

  #drawArcher(ctx) {
    ctx.save();
    const x = this.#archerX;
    if (this.#imgArc && this.#imgArc.complete && this.#imgArc.naturalWidth > 0) {
      ctx.drawImage(this.#imgArc, x - 22, ARCHER_Y - 20, 44, 55);
    } else {
      ctx.strokeStyle = '#39FF14'; ctx.shadowColor = '#39FF14';
      ctx.shadowBlur = 10; ctx.lineWidth = 3;
      ctx.beginPath();
      ctx.arc(x, ARCHER_Y + 10, 20, Math.PI * 1.2, Math.PI * 1.8);
      ctx.stroke();
    }
    // Glow at feet
    const g = ctx.createRadialGradient(x, GROUND_Y, 0, x, GROUND_Y, 28);
    g.addColorStop(0, 'rgba(57,255,20,0.28)'); g.addColorStop(1, 'transparent');
    ctx.fillStyle = g;
    ctx.beginPath(); ctx.ellipse(x, GROUND_Y, 28, 7, 0, 0, Math.PI * 2); ctx.fill();
    ctx.restore();
  }

  #drawIdle() {
    const ctx = this.#ctx;
    const sky = ctx.createLinearGradient(0, 0, 0, CH);
    sky.addColorStop(0, '#020b18'); sky.addColorStop(1, '#0d1f3c');
    ctx.fillStyle = sky; ctx.fillRect(0, 0, CW, CH);
    ctx.save();
    for (const s of this.#stars) {
      ctx.globalAlpha = s.a; ctx.fillStyle = '#fff';
      ctx.beginPath(); ctx.arc(s.x, s.y, s.r, 0, Math.PI * 2); ctx.fill();
    }
    ctx.globalAlpha = 1;
    ctx.textAlign = 'center'; ctx.textBaseline = 'middle';
    ctx.font = `14px ${PIXEL_FONT}`;
    ctx.fillStyle = '#39FF14'; ctx.shadowColor = '#39FF14'; ctx.shadowBlur = 22;
    ctx.fillText('PRESS START', CW / 2, CH / 2 - 22);
    ctx.font = `7px ${PIXEL_FONT}`;
    ctx.fillStyle = 'rgba(57,255,20,0.5)'; ctx.shadowBlur = 0;
    ctx.fillText('← → DEPLACER   ESPACE TIRER', CW / 2, CH / 2 + 22);
    ctx.restore();
  }

  #endGame() {
    this.#running = false;
    cancelAnimationFrame(this.#req); this.#req = null;
    this.#lives = 0;

    const ctx = this.#ctx;
    ctx.save();
    ctx.fillStyle = 'rgba(0,0,0,0.82)'; ctx.fillRect(0, 0, CW, CH);
    ctx.textAlign = 'center'; ctx.textBaseline = 'middle';

    ctx.font = `22px ${PIXEL_FONT}`;
    ctx.fillStyle = '#FF2D78'; ctx.shadowColor = '#FF2D78'; ctx.shadowBlur = 22;
    ctx.fillText('GAME OVER', CW / 2, CH / 2 - 52);

    ctx.font = `13px ${PIXEL_FONT}`;
    ctx.fillStyle = '#FFE600'; ctx.shadowColor = '#FFE600'; ctx.shadowBlur = 12;
    ctx.fillText(`SCORE : ${this.#score}`, CW / 2, CH / 2 + 4);

    ctx.font = `7px ${PIXEL_FONT}`;
    ctx.fillStyle = 'rgba(57,255,20,0.65)'; ctx.shadowBlur = 0;
    ctx.fillText('PRESS START TO PLAY AGAIN', CW / 2, CH / 2 + 52);
    ctx.restore();

    document.getElementById('stopAndStartGame').textContent = '↺ REJOUER';
  }

  #updateHUD() {
    document.getElementById('score').textContent    = this.#score;
    document.getElementById('nbArrows').textContent = this.#nbArrows;
    for (let i = 1; i <= 3; i++) {
      const el = document.getElementById(`life-${i}`);
      if (el) el.style.opacity = i <= this.#lives ? '1' : '0.15';
    }
  }
}
