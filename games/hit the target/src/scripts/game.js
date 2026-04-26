/* ══════════════════════════════════════════════
   Hit the Target — Sherwood Forest Edition
   Full canvas-procedural rendering (no PNG imports)
   ══════════════════════════════════════════════ */

const CW         = 500;
const CH         = 580;
const GROUND_Y   = 545;
const ARCHER_Y   = 496;
const PIXEL_FONT = '"Press Start 2P", monospace';

/* Forest palette */
const C_FOREST_DEEP  = '#0a2818';
const C_FOREST_MID   = '#1A4D2E';
const C_FOREST_LIGHT = '#4F7C2E';
const C_GOLD         = '#D4AF37';
const C_GOLD_BRIGHT  = '#FFD700';
const C_CRIMSON      = '#B22222';
const C_WOOD         = '#8B4513';
const C_WOOD_LIGHT   = '#C68642';
const C_WOOD_DARK    = '#4A2511';
const C_PARCHMENT    = '#F5DEB3';
const C_RAVEN        = '#1A1A1A';

function overlap(a, b) {
  return a.x < b.x + b.w && a.x + a.w > b.x &&
         a.y < b.y + b.h && a.y + a.h > b.y;
}

/* ── Firefly factory ───────────────────────── */
function makeFirefly() {
  return {
    x:     Math.random() * CW,
    y:     20 + Math.random() * (GROUND_Y - 120),
    vy:   -(0.08 + Math.random() * 0.22),
    vx:    (Math.random() - 0.5) * 0.18,
    r:     Math.random() * 1.4 + 0.6,
    a:     0.4 + Math.random() * 0.6,
    phase: Math.random() * Math.PI * 2
  };
}

/* ══════════════════════════════════════════════
   Arrow (projectile)
   ══════════════════════════════════════════════ */
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
    /* Feather fletching (tail) */
    ctx.fillStyle = '#E8E0C8';
    ctx.fillRect(this.x - 5, this.y + 18, 4, 6);
    ctx.fillRect(this.x + 1, this.y + 18, 4, 6);
    ctx.fillStyle = '#C8B8A0';
    ctx.fillRect(this.x - 4, this.y + 19, 2, 4);
    ctx.fillRect(this.x + 2, this.y + 19, 2, 4);
    /* Wooden shaft */
    ctx.fillStyle = C_WOOD;
    ctx.fillRect(this.x - 2, this.y + 3, 4, 19);
    /* Wood grain highlight */
    ctx.fillStyle = C_WOOD_LIGHT;
    ctx.fillRect(this.x - 1, this.y + 3, 1, 17);
    /* Gold arrowhead */
    ctx.fillStyle = C_GOLD_BRIGHT;
    ctx.beginPath();
    ctx.moveTo(this.x,     this.y - 8);
    ctx.lineTo(this.x - 4, this.y + 4);
    ctx.lineTo(this.x + 4, this.y + 4);
    ctx.closePath(); ctx.fill();
    /* Shading on left half of head */
    ctx.fillStyle = C_GOLD;
    ctx.beginPath();
    ctx.moveTo(this.x,     this.y - 8);
    ctx.lineTo(this.x - 4, this.y + 4);
    ctx.lineTo(this.x,     this.y + 1);
    ctx.closePath(); ctx.fill();
    ctx.restore();
  }
}

/* ══════════════════════════════════════════════
   Target (archery bullseye)
   ══════════════════════════════════════════════ */
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

  draw(ctx) {
    if (!this.active) return;
    ctx.save();
    /* Outer glow (target suspended from above) */
    const glow = ctx.createRadialGradient(this.x, this.y, Target.R * 0.5, this.x, this.y, Target.R * 1.4);
    glow.addColorStop(0, 'transparent');
    glow.addColorStop(1, 'rgba(255,200,50,0.08)');
    ctx.fillStyle = glow;
    ctx.beginPath(); ctx.arc(this.x, this.y, Target.R * 1.4, 0, Math.PI * 2); ctx.fill();

    /* Classic archery rings: white → black → blue → red → gold */
    const rings = [
      { r: Target.R,        c: '#FFFFFF' },
      { r: Target.R * 0.80, c: '#1A1A1A' },
      { r: Target.R * 0.62, c: '#1E5FA8' },
      { r: Target.R * 0.43, c: C_CRIMSON },
      { r: Target.R * 0.24, c: C_GOLD_BRIGHT },
    ];
    for (const ring of rings) {
      ctx.fillStyle = ring.c;
      ctx.beginPath(); ctx.arc(this.x, this.y, ring.r, 0, Math.PI * 2); ctx.fill();
    }
    /* Inner gold bullseye */
    ctx.fillStyle = '#FFF0A0';
    ctx.beginPath(); ctx.arc(this.x, this.y, Target.R * 0.10, 0, Math.PI * 2); ctx.fill();

    /* Subtle ring dividers */
    ctx.strokeStyle = 'rgba(0,0,0,0.3)'; ctx.lineWidth = 1;
    for (const ring of rings) {
      ctx.beginPath(); ctx.arc(this.x, this.y, ring.r, 0, Math.PI * 2); ctx.stroke();
    }
    /* Cross-hairs on gold zone */
    ctx.strokeStyle = 'rgba(180,130,0,0.4)'; ctx.lineWidth = 0.5;
    ctx.beginPath();
    ctx.moveTo(this.x - Target.R * 0.24, this.y); ctx.lineTo(this.x + Target.R * 0.24, this.y);
    ctx.moveTo(this.x, this.y - Target.R * 0.24); ctx.lineTo(this.x, this.y + Target.R * 0.24);
    ctx.stroke();

    /* Hit flash ring */
    if (this.hitTimer > 0) {
      ctx.globalAlpha  = this.hitTimer / 20;
      ctx.strokeStyle  = C_GOLD_BRIGHT;
      ctx.shadowColor  = C_GOLD_BRIGHT; ctx.shadowBlur = 20;
      ctx.lineWidth    = 3;
      ctx.beginPath(); ctx.arc(this.x, this.y, Target.R + 9, 0, Math.PI * 2); ctx.stroke();
    }
    ctx.restore();
  }
}

/* ══════════════════════════════════════════════
   Raven Bird
   ══════════════════════════════════════════════ */
class Bird {
  static W = 76; static H = 44;

  constructor(fromLeft, y) {
    this.fromLeft  = fromLeft;
    this.x         = fromLeft ? -Bird.W - 10 : CW + 10;
    this.y         = y;
    this.spd       = 1.8 + Math.random() * 2.2;
    this.active    = true;
    this.flapPhase = Math.random() * Math.PI * 2;
  }
  move() {
    this.x += this.fromLeft ? this.spd : -this.spd;
    this.flapPhase += 0.14;
    if (this.x > CW + Bird.W + 10 || this.x < -Bird.W - 10) this.active = false;
  }
  get rect() { return { x: this.x, y: this.y, w: Bird.W, h: Bird.H }; }

  draw(ctx) {
    if (!this.active) return;
    const cx   = this.x + Bird.W / 2;
    const cy   = this.y + Bird.H / 2;
    const flap = Math.sin(this.flapPhase) * 9;

    ctx.save();
    /* Mirror for right-to-left birds */
    if (!this.fromLeft) {
      ctx.translate(cx * 2, 0);
      ctx.scale(-1, 1);
    }

    /* Wings */
    ctx.fillStyle  = C_RAVEN;
    ctx.shadowColor = 'rgba(0,0,0,0.45)'; ctx.shadowBlur = 5;

    /* Left (main) wing */
    ctx.beginPath();
    ctx.moveTo(cx, cy + 2);
    ctx.quadraticCurveTo(cx - 22, cy - 8  + flap, cx - Bird.W / 2 + 4, cy - 1 + flap);
    ctx.quadraticCurveTo(cx - 20, cy + 10,        cx,                   cy + 3);
    ctx.closePath(); ctx.fill();

    /* Right (inner) wing — shorter */
    ctx.beginPath();
    ctx.moveTo(cx, cy + 2);
    ctx.quadraticCurveTo(cx + 16, cy - 7 - flap * 0.6, cx + Bird.W / 2 - 8, cy + flap * 0.4);
    ctx.quadraticCurveTo(cx + 14, cy + 8,               cx,                  cy + 2);
    ctx.closePath(); ctx.fill();

    ctx.shadowBlur = 0;

    /* Body */
    ctx.fillStyle = '#222222';
    ctx.beginPath();
    ctx.ellipse(cx, cy + 2, 12, 9, -0.1, 0, Math.PI * 2); ctx.fill();

    /* Head */
    ctx.fillStyle = C_RAVEN;
    ctx.beginPath(); ctx.arc(cx + 12, cy - 2, 7, 0, Math.PI * 2); ctx.fill();

    /* Beak */
    ctx.fillStyle = '#3A3A3A';
    ctx.beginPath();
    ctx.moveTo(cx + 18,  cy - 2);
    ctx.lineTo(cx + 27,  cy + 0.5);
    ctx.lineTo(cx + 18,  cy + 2.5);
    ctx.closePath(); ctx.fill();
    /* Beak lower mandible highlight */
    ctx.fillStyle = '#555';
    ctx.beginPath();
    ctx.moveTo(cx + 18,  cy + 0.5);
    ctx.lineTo(cx + 26,  cy + 0.5);
    ctx.lineTo(cx + 18,  cy + 2.5);
    ctx.closePath(); ctx.fill();

    /* Red eye */
    ctx.fillStyle = '#CC1111';
    ctx.beginPath(); ctx.arc(cx + 14, cy - 3, 2.2, 0, Math.PI * 2); ctx.fill();
    ctx.fillStyle = '#fff';
    ctx.beginPath(); ctx.arc(cx + 14.6, cy - 3.6, 0.9, 0, Math.PI * 2); ctx.fill();

    /* Tail feathers */
    ctx.fillStyle = C_RAVEN;
    ctx.beginPath();
    ctx.moveTo(cx - 9, cy + 2);
    ctx.lineTo(cx - 22, cy + 13);
    ctx.lineTo(cx - 15, cy + 4);
    ctx.lineTo(cx - 25, cy + 9);
    ctx.lineTo(cx - 12, cy + 3);
    ctx.closePath(); ctx.fill();

    ctx.restore();
  }
}

/* ══════════════════════════════════════════════
   Arrow Bundle (falling quiver power-up)
   ══════════════════════════════════════════════ */
class ArrowBundle {
  static W = 44; static H = 44;

  constructor(x) {
    this.x = x; this.y = -ArrowBundle.H;
    this.vy = 1.4; this.active = true;
  }
  move() { this.y += this.vy; if (this.y > CH + 10) this.active = false; }
  get rect() { return { x: this.x - ArrowBundle.W / 2, y: this.y, w: ArrowBundle.W, h: ArrowBundle.H }; }

  draw(ctx) {
    if (!this.active) return;
    const rx = this.x - ArrowBundle.W / 2;
    const ry = this.y;
    ctx.save();
    ctx.shadowColor = C_GOLD; ctx.shadowBlur = 16;

    /* Three arrows sticking out */
    const arrows = [
      { ox: rx + 11, oy: ry + 0  },
      { ox: rx + 21, oy: ry - 2  },
      { ox: rx + 31, oy: ry + 1  }
    ];
    for (const { ox, oy } of arrows) {
      ctx.shadowBlur = 0;
      /* Shaft */
      ctx.fillStyle = C_WOOD;
      ctx.fillRect(ox - 1, oy + 3, 3, 14);
      ctx.fillStyle = C_WOOD_LIGHT;
      ctx.fillRect(ox - 1, oy + 3, 1, 12);
      /* Fletching */
      ctx.fillStyle = '#E8E0C8';
      ctx.fillRect(ox - 4, oy + 13, 3, 4);
      ctx.fillRect(ox + 2, oy + 13, 3, 4);
      /* Gold tip */
      ctx.shadowColor = C_GOLD_BRIGHT; ctx.shadowBlur = 6;
      ctx.fillStyle = C_GOLD_BRIGHT;
      ctx.beginPath();
      ctx.moveTo(ox,     oy - 2);
      ctx.lineTo(ox - 3, oy + 4);
      ctx.lineTo(ox + 3, oy + 4);
      ctx.closePath(); ctx.fill();
    }

    ctx.shadowColor = C_GOLD; ctx.shadowBlur = 14;

    /* Quiver body (trapezoid) */
    ctx.fillStyle = C_WOOD;
    ctx.beginPath();
    ctx.moveTo(rx + 5,  ry + 18);
    ctx.lineTo(rx + 39, ry + 18);
    ctx.lineTo(rx + 37, ry + 43);
    ctx.lineTo(rx + 7,  ry + 43);
    ctx.closePath(); ctx.fill();

    /* Highlight stripe */
    ctx.fillStyle = C_WOOD_LIGHT;
    ctx.fillRect(rx + 8, ry + 19, 5, 21);

    /* Shadow stripe */
    ctx.fillStyle = C_WOOD_DARK;
    ctx.fillRect(rx + 30, ry + 19, 6, 21);

    /* Top rim */
    ctx.fillStyle = C_WOOD_DARK;
    ctx.fillRect(rx + 4, ry + 15, 36, 4);

    /* Bottom cap */
    ctx.fillStyle = C_WOOD_DARK;
    ctx.fillRect(rx + 6, ry + 39, 32, 5);

    /* Belt strap */
    ctx.shadowBlur = 0;
    ctx.fillStyle = C_WOOD_DARK;
    ctx.fillRect(rx + 4, ry + 28, 36, 4);
    /* Gold buckle */
    ctx.fillStyle = C_GOLD;
    ctx.fillRect(rx + 17, ry + 28, 10, 4);
    ctx.fillStyle = C_WOOD_DARK;
    ctx.fillRect(rx + 20, ry + 29, 4, 2);

    ctx.restore();
  }
}

/* ══════════════════════════════════════════════
   Flash message
   ══════════════════════════════════════════════ */
class Flash {
  constructor(x, y, text, color = C_GOLD_BRIGHT, dur = 50) {
    this.x = x; this.y = y; this.text = text;
    this.color = color; this.life = dur; this.maxLife = dur;
  }
  update() { this.y -= 0.6; this.life--; }
  get alive() { return this.life > 0; }

  draw(ctx) {
    ctx.save();
    ctx.globalAlpha = this.life / this.maxLife;
    ctx.font        = `bold 11px ${PIXEL_FONT}`;
    ctx.textAlign   = 'center';
    ctx.strokeStyle = '#000'; ctx.lineWidth = 3;
    ctx.strokeText(this.text, this.x, this.y);
    ctx.fillStyle   = this.color;
    ctx.shadowColor = this.color; ctx.shadowBlur = 8;
    ctx.fillText(this.text, this.x, this.y);
    ctx.restore();
  }
}

/* ══════════════════════════════════════════════
   Game
   ══════════════════════════════════════════════ */
export default class Game {
  #canvas; #ctx; #req = null; #running = false;

  #archerX; #keys;
  #arrows; #targets; #birds; #bundles; #flashes;
  #fireflies; #trees;
  #score; #lives; #nbArrows;
  #tTarget; #tBird; #tBundle;
  #frame = 0;

  constructor(canvas) {
    this.#canvas    = canvas;
    this.#ctx       = canvas.getContext('2d');
    this.#fireflies = Array.from({ length: 42 }, makeFirefly);
    this.#trees     = this.#buildTrees();
    this.#reset();
    this.#setupControls();
    this.#drawIdle();
  }

  get canvas() { return this.#canvas; }

  /* ── Pre-compute tree positions ───────────── */
  #buildTrees() {
    return [
      /* Left cluster */
      { x: -6,  h: 310, w: 88  },
      { x: 28,  h: 258, w: 72  },
      { x: 58,  h: 218, w: 58  },
      { x: 84,  h: 180, w: 46  },
      /* Right cluster */
      { x: CW + 6,  h: 310, w: 88  },
      { x: CW - 28, h: 258, w: 72  },
      { x: CW - 58, h: 218, w: 58  },
      { x: CW - 84, h: 180, w: 46  },
    ];
  }

  /* ── State reset ──────────────────────────── */
  #reset() {
    this.#archerX = CW / 2;
    this.#keys    = { left: false, right: false };
    this.#arrows  = []; this.#targets = []; this.#birds = [];
    this.#bundles = []; this.#flashes = [];
    this.#score   = 0; this.#lives = 3; this.#nbArrows = 10;
    this.#tTarget = 0; this.#tBird = 120; this.#tBundle = 0;
    this.#frame   = 0;
    this.#updateHUD();
  }

  /* ── Input ────────────────────────────────── */
  #setupControls() {
    this.#canvas.addEventListener('click', () => {
      if (this.#running) this.#shoot();
    });
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
    document.getElementById('stopAndStartGame')
            .addEventListener('click', () => this.#toggle());
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
      btn.textContent = '▶ RESUME';
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

  /* ── Game loop ────────────────────────────── */
  #loop() {
    this.#update();
    this.#render();
    this.#frame++;
    if (this.#running) this.#req = requestAnimationFrame(this.#loop.bind(this));
  }

  /* ── Logic update ─────────────────────────── */
  #update() {
    if (this.#keys.left  && this.#archerX > 28)      this.#archerX -= 5;
    if (this.#keys.right && this.#archerX < CW - 28) this.#archerX += 5;

    /* Spawn target every ~110 frames */
    if (++this.#tTarget >= 110) {
      this.#tTarget = 0;
      const y  = 55 + Math.random() * 150;
      const dx = (Math.random() > 0.5 ? 1 : -1) * (1.2 + Math.random() * 2.2);
      this.#targets.push(
        new Target(Target.R + Math.random() * (CW - Target.R * 2), y, dx)
      );
    }
    /* Spawn raven every ~180 frames */
    if (++this.#tBird >= 180) {
      this.#tBird = 0;
      this.#birds.push(new Bird(Math.random() > 0.5, 215 + Math.random() * 145));
    }
    /* Spawn quiver bundle when low on arrows */
    if (++this.#tBundle >= 280) {
      this.#tBundle = 0;
      if (this.#nbArrows < 8)
        this.#bundles.push(new ArrowBundle(50 + Math.random() * (CW - 100)));
    }

    /* Animate fireflies */
    for (const f of this.#fireflies) {
      f.x += f.vx; f.y += f.vy;
      f.phase += 0.038;
      f.a = 0.25 + Math.abs(Math.sin(f.phase)) * 0.75;
      if (f.y < -5)  { Object.assign(f, makeFirefly()); f.y = GROUND_Y - 15; }
      if (f.x < -4)  f.x = CW + 2;
      if (f.x > CW + 4) f.x = -2;
    }

    this.#arrows.forEach(a => a.move());
    this.#targets.forEach(t => t.move());
    this.#birds.forEach(b => b.move());
    this.#bundles.forEach(b => b.move());
    this.#flashes.forEach(f => f.update());

    this.#arrows  = this.#arrows.filter(a => a.active);
    this.#birds   = this.#birds.filter(b => b.active);
    this.#bundles = this.#bundles.filter(b => b.active);
    this.#flashes = this.#flashes.filter(f => f.alive);

    /* Arrow ↔ Target */
    for (const arrow of this.#arrows) {
      for (const target of this.#targets) {
        if (!arrow.active || !target.active) continue;
        if (Math.hypot(arrow.x - target.x, arrow.y - target.y) < Target.R) {
          const pts       = target.scoreAt(arrow.x, arrow.y);
          this.#score    += pts;
          target.hitTimer = 20;
          target.active   = false;
          arrow.active    = false;
          this.#flashes.push(new Flash(arrow.x, arrow.y, `+${pts}`));
          this.#updateHUD();
        }
      }
    }

    /* Arrow ↔ Raven (arrow stolen) */
    for (const arrow of this.#arrows) {
      for (const bird of this.#birds) {
        if (!arrow.active || !bird.active) continue;
        if (overlap(arrow.rect, bird.rect)) {
          arrow.active = false;
          this.#flashes.push(
            new Flash(arrow.x, arrow.y - 10, 'STOLEN!', C_CRIMSON, 55)
          );
        }
      }
    }

    /* Raven ↔ Archer (lose a life) */
    const archerRect = { x: this.#archerX - 22, y: ARCHER_Y - 5, w: 44, h: 50 };
    for (const bird of this.#birds) {
      if (!bird.active) continue;
      if (overlap(archerRect, bird.rect)) {
        bird.active    = false;
        this.#lives    = Math.max(0, this.#lives - 1);
        this.#flashes.push(
          new Flash(this.#archerX, ARCHER_Y - 20, '-1 LIFE', C_CRIMSON, 65)
        );
        this.#updateHUD();
        if (this.#lives <= 0) { this.#endGame(); return; }
      }
    }

    /* Archer ↔ Bundle (collect +3 arrows) */
    for (const bundle of this.#bundles) {
      if (!bundle.active) continue;
      if (overlap(archerRect, bundle.rect)) {
        bundle.active   = false;
        this.#nbArrows  = Math.min(this.#nbArrows + 3, 20);
        this.#flashes.push(
          new Flash(this.#archerX, ARCHER_Y - 24, '+3 ARROWS', C_GOLD_BRIGHT, 55)
        );
        this.#updateHUD();
      }
    }

    if (this.#nbArrows <= 0 && this.#arrows.length === 0) this.#endGame();
  }

  /* ── Render ───────────────────────────────── */
  #render() {
    const ctx = this.#ctx;
    this.#drawBackground(ctx);
    this.#drawFireflies(ctx);
    this.#bundles.forEach(b => b.draw(ctx));
    this.#targets.forEach(t => t.draw(ctx));
    this.#birds.forEach(b => b.draw(ctx));
    this.#arrows.forEach(a => a.draw(ctx));
    this.#drawArcher(ctx);
    this.#flashes.forEach(f => f.draw(ctx));
  }

  /* ── Background: forest canopy + moon ──────── */
  #drawBackground(ctx) {
    /* Sky/forest gradient */
    const grad = ctx.createLinearGradient(0, 0, 0, CH);
    grad.addColorStop(0,    '#060F09');
    grad.addColorStop(0.25, '#0a2818');
    grad.addColorStop(0.55, '#1A4D2E');
    grad.addColorStop(0.80, '#0F2E1A');
    grad.addColorStop(1,    '#07180D');
    ctx.fillStyle = grad;
    ctx.fillRect(0, 0, CW, CH);

    /* Moon glow halo */
    const mx = Math.round(CW * 0.78), my = 52;
    const halo = ctx.createRadialGradient(mx, my, 0, mx, my, 68);
    halo.addColorStop(0,   'rgba(255,245,200,0.22)');
    halo.addColorStop(0.5, 'rgba(200,190,130,0.08)');
    halo.addColorStop(1,   'transparent');
    ctx.fillStyle = halo;
    ctx.fillRect(mx - 70, 0, 140, 140);

    /* Moon disc (crescent effect via two circles) */
    ctx.fillStyle = '#F5DEB3';
    ctx.beginPath(); ctx.arc(mx, my, 14, 0, Math.PI * 2); ctx.fill();
    ctx.fillStyle = '#0a2818'; /* shadow circle for crescent */
    ctx.beginPath(); ctx.arc(mx - 4, my - 3, 12, 0, Math.PI * 2); ctx.fill();
    ctx.fillStyle = '#F5DEB3';
    ctx.beginPath(); ctx.arc(mx, my, 11, 0, Math.PI * 2); ctx.fill();

    /* Tree silhouettes (very dark pine shapes) */
    ctx.fillStyle = '#050E08';
    for (const t of this.#trees) {
      ctx.beginPath();
      ctx.moveTo(t.x,            GROUND_Y - t.h);
      ctx.lineTo(t.x - t.w / 2, GROUND_Y);
      ctx.lineTo(t.x + t.w / 2, GROUND_Y);
      ctx.closePath(); ctx.fill();
    }
    /* Second layer — slightly lighter inner trees */
    ctx.fillStyle = '#091808';
    for (const t of this.#trees) {
      const scale = 0.55;
      ctx.beginPath();
      ctx.moveTo(t.x,                   GROUND_Y - t.h * scale);
      ctx.lineTo(t.x - (t.w * scale)/2, GROUND_Y);
      ctx.lineTo(t.x + (t.w * scale)/2, GROUND_Y);
      ctx.closePath(); ctx.fill();
    }

    /* Ground */
    ctx.fillStyle = '#0B1F0E';
    ctx.fillRect(0, GROUND_Y, CW, CH - GROUND_Y);
    /* Grass edge — triple line */
    ctx.fillStyle = C_FOREST_MID;
    ctx.fillRect(0, GROUND_Y,     CW, 5);
    ctx.fillStyle = C_FOREST_LIGHT;
    ctx.fillRect(0, GROUND_Y,     CW, 2);
    ctx.fillStyle = '#8BC34A';
    ctx.fillRect(0, GROUND_Y,     CW, 1);

    /* Dirt path strip */
    ctx.fillStyle = 'rgba(180,150,80,0.06)';
    ctx.fillRect(CW * 0.12, GROUND_Y + 4, CW * 0.76, 9);
  }

  /* ── Fireflies ─────────────────────────────── */
  #drawFireflies(ctx) {
    ctx.save();
    for (const f of this.#fireflies) {
      const grd = ctx.createRadialGradient(f.x, f.y, 0, f.x, f.y, f.r * 5);
      grd.addColorStop(0,   `rgba(255, 248, 100, ${f.a * 0.9})`);
      grd.addColorStop(0.35, `rgba(180, 230, 60, ${f.a * 0.45})`);
      grd.addColorStop(1,   'transparent');
      ctx.fillStyle = grd;
      ctx.beginPath(); ctx.arc(f.x, f.y, f.r * 5, 0, Math.PI * 2); ctx.fill();
      ctx.fillStyle = `rgba(255, 255, 190, ${f.a})`;
      ctx.beginPath(); ctx.arc(f.x, f.y, f.r, 0, Math.PI * 2); ctx.fill();
    }
    ctx.restore();
  }

  /* ── Robin Hood archer ─────────────────────── */
  #drawArcher(ctx) {
    const x   = this.#archerX;
    const top = ARCHER_Y - 20;   /* top of 44×62 sprite box */

    ctx.save();

    /* Ground shadow ellipse */
    const shd = ctx.createRadialGradient(x, GROUND_Y + 1, 0, x, GROUND_Y + 1, 28);
    shd.addColorStop(0, 'rgba(0,0,0,0.45)'); shd.addColorStop(1, 'transparent');
    ctx.fillStyle = shd;
    ctx.beginPath(); ctx.ellipse(x, GROUND_Y + 1, 28, 7, 0, 0, Math.PI * 2); ctx.fill();

    /* ── Boots ── */
    ctx.fillStyle = '#1E0E04';
    ctx.fillRect(x - 12, top + 58, 11, 7);   /* left  */
    ctx.fillRect(x + 1,  top + 58, 11, 7);   /* right */
    ctx.fillStyle = '#2E1A08';
    ctx.fillRect(x - 11, top + 58, 3, 5);
    ctx.fillRect(x + 2,  top + 58, 3, 5);

    /* ── Legs ── */
    ctx.fillStyle = C_WOOD_DARK;
    ctx.fillRect(x - 10, top + 46, 8, 14);
    ctx.fillRect(x + 2,  top + 46, 8, 14);
    /* Leg highlight */
    ctx.fillStyle = '#5C3018';
    ctx.fillRect(x - 9,  top + 46, 2, 12);
    ctx.fillRect(x + 3,  top + 46, 2, 12);

    /* ── Belt ── */
    ctx.fillStyle = C_WOOD;
    ctx.fillRect(x - 12, top + 42, 24, 5);
    ctx.fillStyle = C_GOLD;
    ctx.fillRect(x - 4,  top + 43, 8, 3);    /* buckle */
    ctx.fillStyle = C_WOOD_DARK;
    ctx.fillRect(x - 1,  top + 44, 2, 1);    /* buckle pin */

    /* ── Tunic (green, with shading) ── */
    ctx.fillStyle = C_FOREST_LIGHT;
    ctx.fillRect(x - 12, top + 25, 24, 19);
    ctx.fillStyle = '#6A9C3A';                /* lighter centre */
    ctx.fillRect(x - 7,  top + 26, 6, 17);
    ctx.fillStyle = '#3A6020';                /* right shadow */
    ctx.fillRect(x + 5,  top + 26, 5, 17);
    /* V-neck */
    ctx.fillStyle = '#2A4A10';
    ctx.fillRect(x - 2,  top + 25, 4, 6);

    /* ── Left arm / sleeve (bow arm) ── */
    ctx.fillStyle = '#3A6020';                /* sleeve */
    ctx.fillRect(x - 22, top + 28, 11, 7);
    ctx.fillStyle = '#D4A076';                /* skin wrist */
    ctx.fillRect(x - 23, top + 30, 4, 4);

    /* ── Right arm / sleeve (draw arm) ── */
    ctx.fillStyle = '#3A6020';
    ctx.fillRect(x + 11, top + 28, 9, 7);
    ctx.fillStyle = '#D4A076';               /* skin */
    ctx.fillRect(x + 18, top + 30, 5, 4);

    /* ── Bow (curved wood arc) ── */
    ctx.strokeStyle = '#6B2E08';
    ctx.lineWidth   = 4;
    ctx.lineCap     = 'round';
    ctx.shadowColor = '#3A1004'; ctx.shadowBlur = 4;
    ctx.beginPath();
    ctx.moveTo(x - 10, top + 22);
    ctx.quadraticCurveTo(x - 30, top + 43, x - 10, top + 62);
    ctx.stroke();
    ctx.shadowBlur = 0;
    /* Bow grain */
    ctx.strokeStyle = C_WOOD_LIGHT;
    ctx.lineWidth   = 1.5;
    ctx.setLineDash([3, 5]);
    ctx.beginPath();
    ctx.moveTo(x - 11, top + 24);
    ctx.quadraticCurveTo(x - 28, top + 43, x - 11, top + 60);
    ctx.stroke();
    ctx.setLineDash([]);
    /* Bowstring */
    ctx.strokeStyle = '#E8D8B0';
    ctx.lineWidth   = 1.5;
    ctx.beginPath();
    ctx.moveTo(x - 10, top + 22);
    ctx.lineTo(x - 17, top + 43);   /* pulled back to draw hand */
    ctx.lineTo(x - 10, top + 62);
    ctx.stroke();
    /* Bow tip caps (gold) */
    ctx.fillStyle = C_GOLD;
    ctx.fillRect(x - 13, top + 20, 5, 4);
    ctx.fillRect(x - 13, top + 60, 5, 4);

    /* ── Head ── */
    ctx.fillStyle = '#D4A076';
    ctx.beginPath(); ctx.arc(x, top + 17, 10, 0, Math.PI * 2); ctx.fill();
    /* Chin shading */
    ctx.fillStyle = '#BE8E60';
    ctx.beginPath(); ctx.arc(x + 1, top + 20, 7, 0.15, Math.PI - 0.15); ctx.fill();
    /* Eyes */
    ctx.fillStyle = '#1A1A1A';
    ctx.fillRect(x - 5, top + 14, 3, 2);
    ctx.fillRect(x + 2, top + 14, 3, 2);
    /* Eye glints */
    ctx.fillStyle = '#FFFFFF';
    ctx.fillRect(x - 4, top + 14, 1, 1);
    ctx.fillRect(x + 3, top + 14, 1, 1);
    /* Mouth (small smirk) */
    ctx.fillStyle = '#A0603A';
    ctx.fillRect(x - 2, top + 21, 5, 1);

    /* ── Robin Hood hat ── */
    /* Brim */
    ctx.fillStyle = '#244A18';
    ctx.fillRect(x - 14, top + 7, 28, 5);
    /* Crown layers (pixel stepped triangle) */
    ctx.fillStyle = C_FOREST_LIGHT;
    ctx.fillRect(x - 10, top + 2,  20, 6);
    ctx.fillStyle = '#5A8A2A';
    ctx.fillRect(x - 8,  top - 3,  16, 6);
    ctx.fillStyle = C_FOREST_LIGHT;
    ctx.fillRect(x - 5,  top - 9,  10, 7);
    ctx.fillRect(x - 3,  top - 14, 6,  6);
    ctx.fillRect(x - 1,  top - 17, 2,  4);   /* tip */
    /* Hat band */
    ctx.fillStyle = '#244A18';
    ctx.fillRect(x - 9, top + 3,  18, 3);
    /* Hat feather (crimson, sweeping right) */
    ctx.fillStyle = C_CRIMSON;
    ctx.beginPath();
    ctx.moveTo(x + 8, top + 6);
    ctx.quadraticCurveTo(x + 26, top - 10, x + 18, top - 18);
    ctx.quadraticCurveTo(x + 24, top -  4, x + 10, top +  4);
    ctx.closePath(); ctx.fill();
    /* Feather highlight */
    ctx.fillStyle = '#D44444';
    ctx.beginPath();
    ctx.moveTo(x + 10, top + 4);
    ctx.quadraticCurveTo(x + 22, top - 6, x + 17, top - 16);
    ctx.quadraticCurveTo(x + 21, top - 3, x + 12, top + 3);
    ctx.closePath(); ctx.fill();

    ctx.restore();
  }

  /* ── Idle screen ──────────────────────────── */
  #drawIdle() {
    const ctx = this.#ctx;
    /* Draw living background + archer */
    this.#drawBackground(ctx);
    this.#drawFireflies(ctx);
    this.#drawArcher(ctx);

    /* Dark vignette overlay */
    ctx.save();
    ctx.fillStyle = 'rgba(5, 12, 5, 0.60)';
    ctx.fillRect(0, 0, CW, CH);

    /* Parchment scroll panel */
    const bx = 48, by = Math.round(CH / 2) - 68, bw = CW - 96, bh = 136;
    ctx.shadowColor = '#000'; ctx.shadowBlur = 24;
    ctx.fillStyle   = C_PARCHMENT;
    ctx.fillRect(bx, by, bw, bh);
    ctx.shadowBlur  = 0;

    /* Aged edges */
    ctx.fillStyle = '#D4B87A';
    ctx.fillRect(bx, by,         bw, 6);
    ctx.fillRect(bx, by + bh - 6, bw, 6);

    /* Border frame */
    ctx.strokeStyle = C_WOOD_DARK; ctx.lineWidth = 4;
    ctx.strokeRect(bx, by, bw, bh);
    ctx.strokeStyle = C_GOLD; ctx.lineWidth = 1.5;
    ctx.strokeRect(bx + 6, by + 6, bw - 12, bh - 12);

    /* Corner rivets */
    for (const [rx, ry] of [
      [bx + 12, by + 12], [bx + bw - 12, by + 12],
      [bx + 12, by + bh - 12], [bx + bw - 12, by + bh - 12]
    ]) {
      ctx.fillStyle = C_WOOD_DARK;
      ctx.beginPath(); ctx.arc(rx, ry, 5.5, 0, Math.PI * 2); ctx.fill();
      ctx.fillStyle = '#8A8A8A';
      ctx.beginPath(); ctx.arc(rx - 1, ry - 1, 2.2, 0, Math.PI * 2); ctx.fill();
    }

    /* Main label */
    ctx.textAlign    = 'center';
    ctx.textBaseline = 'middle';
    ctx.font         = `bold 13px ${PIXEL_FONT}`;
    ctx.fillStyle    = C_CRIMSON;
    ctx.shadowColor  = C_WOOD_DARK; ctx.shadowBlur = 6;
    ctx.fillText('PRESS START', CW / 2, by + 52);

    /* Sub-instructions */
    ctx.shadowBlur = 0;
    ctx.font       = `5.5px ${PIXEL_FONT}`;
    ctx.fillStyle  = C_WOOD_DARK;
    ctx.fillText('← →  MOVE     SPACE  SHOOT     CLICK  SHOOT', CW / 2, by + 92);

    /* Decorative arrow ornaments (horizontal, pointing inward) */
    const ay  = by + 52;
    const arL = bx + 26, arR = bx + bw - 26;
    for (const [ax, dir] of [[arL, 1], [arR, -1]]) {
      ctx.fillStyle = C_WOOD;
      ctx.fillRect(ax - dir * 12, ay - 2, dir * 20, 4);
      ctx.fillStyle = C_GOLD_BRIGHT;
      ctx.beginPath();
      ctx.moveTo(ax + dir * 8, ay);
      ctx.lineTo(ax + dir * 2, ay - 6);
      ctx.lineTo(ax + dir * 2, ay + 6);
      ctx.closePath(); ctx.fill();
    }

    ctx.restore();
  }

  /* ── Game over ────────────────────────────── */
  #endGame() {
    this.#running = false;
    cancelAnimationFrame(this.#req); this.#req = null;
    this.#lives   = 0;

    const ctx = this.#ctx;
    ctx.save();

    /* Dark overlay */
    ctx.fillStyle = 'rgba(3, 8, 3, 0.84)';
    ctx.fillRect(0, 0, CW, CH);

    /* Parchment scroll */
    const bx = 56, by = Math.round(CH / 2) - 95, bw = CW - 112, bh = 190;
    ctx.shadowColor = '#000'; ctx.shadowBlur = 28;
    ctx.fillStyle   = '#EDD99A';
    ctx.fillRect(bx, by, bw, bh);
    ctx.shadowBlur  = 0;

    /* Worn top/bottom edges */
    ctx.fillStyle = '#C4A060';
    ctx.fillRect(bx, by,          bw, 8);
    ctx.fillRect(bx, by + bh - 8, bw, 8);

    /* Borders */
    ctx.strokeStyle = C_WOOD_DARK; ctx.lineWidth = 4;
    ctx.strokeRect(bx, by, bw, bh);
    ctx.strokeStyle = C_GOLD; ctx.lineWidth = 1.5;
    ctx.strokeRect(bx + 6, by + 6, bw - 12, bh - 12);

    /* Corner rivets */
    for (const [rx, ry] of [
      [bx + 14, by + 14], [bx + bw - 14, by + 14],
      [bx + 14, by + bh - 14], [bx + bw - 14, by + bh - 14]
    ]) {
      ctx.fillStyle = C_WOOD_DARK;
      ctx.beginPath(); ctx.arc(rx, ry, 6.5, 0, Math.PI * 2); ctx.fill();
      ctx.fillStyle = '#9A9A9A';
      ctx.beginPath(); ctx.arc(rx - 1, ry - 1, 2.6, 0, Math.PI * 2); ctx.fill();
    }

    ctx.textAlign    = 'center';
    ctx.textBaseline = 'middle';

    /* GAME OVER */
    ctx.font        = `18px ${PIXEL_FONT}`;
    ctx.fillStyle   = C_CRIMSON;
    ctx.shadowColor = C_WOOD_DARK; ctx.shadowBlur = 8;
    ctx.fillText('GAME  OVER', CW / 2, by + 48);

    /* Score */
    ctx.shadowBlur = 0;
    ctx.font       = `10px ${PIXEL_FONT}`;
    ctx.fillStyle  = C_WOOD_DARK;
    ctx.fillText(`SCORE : ${this.#score}`, CW / 2, by + 94);

    /* Gold divider with arrow ornament */
    ctx.fillStyle = C_GOLD;
    ctx.fillRect(bx + 22, by + 115, bw - 44, 2);
    /* Centre arrow */
    ctx.beginPath();
    ctx.moveTo(CW / 2,     by + 110);
    ctx.lineTo(CW / 2 - 5, by + 116);
    ctx.lineTo(CW / 2 + 5, by + 116);
    ctx.closePath();
    ctx.fillStyle = C_GOLD_BRIGHT; ctx.fill();

    /* Replay prompt */
    ctx.font      = `5.5px ${PIXEL_FONT}`;
    ctx.fillStyle = C_CRIMSON;
    ctx.fillText('PRESS START TO PLAY AGAIN', CW / 2, by + 148);

    ctx.restore();
    document.getElementById('stopAndStartGame').textContent = '↺ PLAY AGAIN';
  }

  /* ── HUD update ───────────────────────────── */
  #updateHUD() {
    document.getElementById('score').textContent    = this.#score;
    document.getElementById('nbArrows').textContent = this.#nbArrows;
    for (let i = 1; i <= 3; i++) {
      const el = document.getElementById(`life-${i}`);
      if (el) el.style.opacity = i <= this.#lives ? '1' : '0.15';
    }
  }
}
