import Animation from './animation';
import KeyManager from './keyManager';

const WIN_SCORE    = 7;
const NEON_GREEN   = '#39ff14';
const NEON_CYAN    = '#00f5ff';
const NEON_YELLOW  = '#ffe600';
const NEON_PINK    = '#ff2d78';
const PIXEL_FONT   = '"Press Start 2P", monospace';

export default class AnimationWithObstacle extends Animation {

  constructor(canvas, obstacleL, obstacleR) {
    super(canvas);
    this.myObstacleL = obstacleL;
    this.myObstacleR = obstacleR;
    this.keyManager  = new KeyManager();
    this.scoreL      = 0;
    this.scoreR      = 0;
    this.gameOver    = false;
    this.winner      = null;
    this.addBall();
    this.#drawIdle();
  }

  #drawBackground() {
    const ctx = this.mycontext;
    const { width: w, height: h } = this.myCanvas;

    ctx.fillStyle = '#000';
    ctx.fillRect(0, 0, w, h);

    // Pixel dashed center line
    ctx.save();
    ctx.setLineDash([8, 8]);
    ctx.strokeStyle = 'rgba(57,255,20,0.25)';
    ctx.lineWidth = 3;
    ctx.beginPath();
    ctx.moveTo(w / 2, 0);
    ctx.lineTo(w / 2, h);
    ctx.stroke();
    ctx.restore();
  }

  #drawScores() {
    const ctx = this.mycontext;
    const { width: w } = this.myCanvas;

    ctx.save();
    ctx.font        = `80px ${PIXEL_FONT}`;
    ctx.textAlign   = 'center';
    ctx.textBaseline = 'top';
    ctx.fillStyle   = 'rgba(57,255,20,0.1)';
    ctx.fillText(this.scoreL, w / 4, 20);
    ctx.fillText(this.scoreR, (3 * w) / 4, 20);
    ctx.restore();
  }

  #drawControls() {
    const ctx = this.mycontext;
    const { width: w, height: h } = this.myCanvas;

    ctx.save();
    ctx.font      = `10px ${PIXEL_FONT}`;
    ctx.textAlign = 'center';
    ctx.fillStyle = 'rgba(57,255,20,0.2)';
    ctx.fillText('W / S', w / 4, h - 16);
    ctx.fillText('UP / DN', (3 * w) / 4, h - 16);
    ctx.restore();
  }

  #drawIdle() {
    this.#drawBackground();
    const ctx = this.mycontext;
    const { width: w, height: h } = this.myCanvas;

    ctx.save();
    ctx.textAlign    = 'center';
    ctx.textBaseline = 'middle';
    ctx.font         = `22px ${PIXEL_FONT}`;
    ctx.fillStyle    = NEON_GREEN;
    ctx.shadowColor  = NEON_GREEN;
    ctx.shadowBlur   = 16;
    ctx.fillText('PRESS START', w / 2, h / 2);
    ctx.restore();
  }

  #drawGameOver() {
    const ctx = this.mycontext;
    const { width: w, height: h } = this.myCanvas;

    ctx.save();
    ctx.fillStyle = 'rgba(0,0,0,0.82)';
    ctx.fillRect(0, 0, w, h);

    ctx.textAlign    = 'center';
    ctx.textBaseline = 'middle';

    // Winner text
    ctx.font        = `28px ${PIXEL_FONT}`;
    ctx.fillStyle   = NEON_YELLOW;
    ctx.shadowColor = NEON_YELLOW;
    ctx.shadowBlur  = 20;
    ctx.fillText(`${this.winner}`, w / 2, h / 2 - 52);

    ctx.font        = `18px ${PIXEL_FONT}`;
    ctx.fillStyle   = NEON_PINK;
    ctx.shadowColor = NEON_PINK;
    ctx.shadowBlur  = 16;
    ctx.fillText('WINS !', w / 2, h / 2 - 14);

    // Scores
    ctx.font        = `10px ${PIXEL_FONT}`;
    ctx.fillStyle   = NEON_CYAN;
    ctx.shadowColor = NEON_CYAN;
    ctx.shadowBlur  = 8;
    ctx.fillText(`P1  ${this.scoreL}  —  ${this.scoreR}  P2`, w / 2, h / 2 + 30);

    // Prompt
    ctx.font        = `9px ${PIXEL_FONT}`;
    ctx.fillStyle   = 'rgba(57,255,20,0.6)';
    ctx.shadowBlur  = 0;
    ctx.fillText('PRESS START TO PLAY AGAIN', w / 2, h / 2 + 72);
    ctx.restore();
  }

  #drawPaddle(ctx, x, y, w, h, color) {
    ctx.save();
    ctx.fillStyle   = color;
    ctx.shadowColor = color;
    ctx.shadowBlur  = 12;
    ctx.fillRect(x, y, w, h);
    ctx.restore();
  }

  animate() {
    this.#drawBackground();
    this.#drawScores();
    this.#drawControls();

    this.myObstacleL.handleMoveKeys(this.keyManager);
    this.myObstacleL.move(this.myCanvas);
    this.#drawPaddle(this.mycontext,
      this.myObstacleL.x, this.myObstacleL.y,
      this.myObstacleL.width, this.myObstacleL.height,
      NEON_CYAN);

    this.myObstacleR.handleMoveKeys(this.keyManager);
    this.myObstacleR.move(this.myCanvas);
    this.#drawPaddle(this.mycontext,
      this.myObstacleR.x, this.myObstacleR.y,
      this.myObstacleR.width, this.myObstacleR.height,
      NEON_PINK);

    const result = this.Ball.move(this.myCanvas, this.myObstacleL, this.myObstacleR);

    if (result === 'left') {
      this.scoreR++;
      if (this.scoreR >= WIN_SCORE) return this.#endGame('PLAYER 2');
      this.addBall();
    } else if (result === 'right') {
      this.scoreL++;
      if (this.scoreL >= WIN_SCORE) return this.#endGame('PLAYER 1');
      this.addBall();
    }

    this.#drawBall();
    this.req = window.requestAnimationFrame(this.animate.bind(this));
  }

  #drawBall() {
    const ctx  = this.mycontext;
    const b    = this.Ball;
    const size = 12;
    const cx   = b.x + size / 2;
    const cy   = b.y + size / 2;

    ctx.save();
    ctx.fillStyle   = NEON_YELLOW;
    ctx.shadowColor = NEON_YELLOW;
    ctx.shadowBlur  = 16;
    ctx.fillRect(cx - size / 2, cy - size / 2, size, size);
    ctx.restore();
  }

  #endGame(winner) {
    this.winner  = winner;
    this.gameOver = true;
    window.cancelAnimationFrame(this.req);
    this.req = null;
    this.#drawBackground();
    this.#drawScores();
    this.#drawGameOver();
    document.getElementById('stopStartBall').textContent = '↺ REJOUER';
  }

  startAndStop() {
    if (this.gameOver) {
      this.scoreL   = 0;
      this.scoreR   = 0;
      this.gameOver = false;
      this.winner   = null;
      this.addBall();
      document.getElementById('stopStartBall').textContent = '⏸ PAUSE';
      this.req = window.requestAnimationFrame(this.animate.bind(this));
      return;
    }

    if (this.req) {
      window.cancelAnimationFrame(this.req);
      this.req = null;
      document.getElementById('stopStartBall').textContent = '▶ REPRENDRE';
    } else {
      document.getElementById('stopStartBall').textContent = '⏸ PAUSE';
      this.req = window.requestAnimationFrame(this.animate.bind(this));
    }
  }

  keyDownActionHandler(event) {
    switch (event.key) {
      case 'w': case 'W': this.keyManager.upPressedL();   break;
      case 's': case 'S': this.keyManager.downPressedL(); break;
      case 'ArrowUp':     this.keyManager.upPressedR();   break;
      case 'ArrowDown':   this.keyManager.downPressedR(); break;
      default: return;
    }
    event.preventDefault();
  }

  keyUpActionHandler(event) {
    switch (event.key) {
      case 'w': case 'W': this.keyManager.upReleasedL();   break;
      case 's': case 'S': this.keyManager.downReleasedL(); break;
      case 'ArrowUp':     this.keyManager.upReleasedR();   break;
      case 'ArrowDown':   this.keyManager.downReleasedR(); break;
      default: return;
    }
    event.preventDefault();
  }
}
