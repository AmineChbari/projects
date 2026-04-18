import Animation from './animation';
import KeyManager from './keyManager';

const WIN_SCORE = 7;
const PADDLE_COLOR = 'white';

export default class AnimationWithObstacle extends Animation {

  constructor(canvas, obstacleL, obstacleR) {
    super(canvas);
    this.myObstacleL = obstacleL;
    this.myObstacleR = obstacleR;
    this.keyManager = new KeyManager();
    this.scoreL = 0;
    this.scoreR = 0;
    this.gameOver = false;
    this.winner = null;
    this.addBall();
  }

  #drawBackground() {
    const ctx = this.mycontext;
    const { width: w, height: h } = this.myCanvas;

    ctx.fillStyle = '#05080f';
    ctx.fillRect(0, 0, w, h);

    // Dashed center line
    ctx.save();
    ctx.setLineDash([12, 12]);
    ctx.strokeStyle = 'rgba(255,255,255,0.2)';
    ctx.lineWidth = 2;
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
    ctx.font = 'bold 80px monospace';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'top';
    ctx.fillStyle = 'rgba(255,255,255,0.12)';
    ctx.fillText(this.scoreL, w / 4, 16);
    ctx.fillText(this.scoreR, (3 * w) / 4, 16);
    ctx.restore();
  }

  #drawControls() {
    const ctx = this.mycontext;
    const { width: w, height: h } = this.myCanvas;

    ctx.save();
    ctx.font = '13px monospace';
    ctx.textAlign = 'center';
    ctx.fillStyle = 'rgba(255,255,255,0.25)';
    ctx.fillText('W / S', w / 4, h - 18);
    ctx.fillText('↑ / ↓', (3 * w) / 4, h - 18);
    ctx.restore();
  }

  #drawGameOver() {
    const ctx = this.mycontext;
    const { width: w, height: h } = this.myCanvas;

    ctx.save();
    ctx.fillStyle = 'rgba(0,0,0,0.75)';
    ctx.fillRect(0, 0, w, h);

    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';

    ctx.font = 'bold 56px monospace';
    ctx.fillStyle = '#FFD700';
    ctx.fillText(`${this.winner} GAGNE !`, w / 2, h / 2 - 36);

    ctx.font = '20px monospace';
    ctx.fillStyle = 'rgba(255,255,255,0.7)';
    ctx.fillText('Appuyer sur START pour rejouer', w / 2, h / 2 + 28);
    ctx.restore();
  }

  animate() {
    this.#drawBackground();
    this.#drawScores();
    this.#drawControls();

    this.myObstacleL.handleMoveKeys(this.keyManager);
    this.myObstacleL.move(this.myCanvas);
    this.myObstacleL.draw(this.mycontext, PADDLE_COLOR);

    this.myObstacleR.handleMoveKeys(this.keyManager);
    this.myObstacleR.move(this.myCanvas);
    this.myObstacleR.draw(this.mycontext, PADDLE_COLOR);

    const result = this.Ball.move(this.myCanvas, this.myObstacleL, this.myObstacleR);

    if (result === 'left') {
      this.scoreR++;
      if (this.scoreR >= WIN_SCORE) return this.#endGame('JOUEUR 2');
      this.addBall();
    } else if (result === 'right') {
      this.scoreL++;
      if (this.scoreL >= WIN_SCORE) return this.#endGame('JOUEUR 1');
      this.addBall();
    }

    this.Ball.draw(this.mycontext);
    this.req = window.requestAnimationFrame(this.animate.bind(this));
  }

  #endGame(winner) {
    this.winner = winner;
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
      this.scoreL = 0;
      this.scoreR = 0;
      this.gameOver = false;
      this.winner = null;
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
