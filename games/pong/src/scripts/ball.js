import ballImgSrc from './assets/images/ball.png';

export default class Ball {

  static BALL_WIDTH = 12;
  static MAX_SPEED  = 18;   // vitesse max absolue (x ou y)

  constructor(px, py, dx = 3, dy = -2) {
    this.x = px;
    this.y = py;
    this.deltaX = dx;
    this.deltaY = dy;
    this.image = this.#createImage(ballImgSrc);
  }

  draw(context) {
    context.drawImage(this.image, this.x, this.y);
  }

  setRandomDirection() {
    return (Math.random() > 0.5 ? 1 : -1) * (Math.random() * 2 + 2);
  }

  // Returns 'left' | 'right' if ball exited the canvas, true otherwise
  move(canvas, theObst1, theObst2) {
    // 1. Top/bottom wall bounce
    const ny = this.y + this.deltaY;
    if (ny + Ball.BALL_WIDTH > canvas.height || ny < 0) {
      this.deltaY = -this.deltaY;
      // Légère accélération sur chaque rebond mur (+3%, plafond MAX_SPEED)
      if (Math.abs(this.deltaY) < Ball.MAX_SPEED) {
        this.deltaY *= 1.03;
      }
    }

    // 2. Paddle collision — checked BEFORE boundary exit to prevent tunneling
    if (theObst1 && theObst2 && (this.collisionWith(theObst1) || this.collisionWith(theObst2))) {
      this.deltaX = -this.deltaX;
      if (Math.abs(this.deltaX) < Ball.MAX_SPEED) this.deltaX *= 1.08;
      this.deltaY = this.setRandomDirection();
    }

    // 3. Left/right boundary exit — only scored if paddle was NOT hit
    const nx = this.x + this.deltaX;
    if (nx + Ball.BALL_WIDTH > canvas.width) return 'right';
    if (nx < 0) return 'left';

    this.x += this.deltaX;
    this.y += this.deltaY;
    return true;
  }

  // Swept AABB collision — checks the entire path the ball travels this frame
  collisionWith(obst) {
    const ballLeft   = Math.min(this.x, this.x + this.deltaX);
    const ballRight  = Math.max(this.x + Ball.BALL_WIDTH, this.x + Ball.BALL_WIDTH + this.deltaX);
    const ballTop    = Math.min(this.y, this.y + this.deltaY);
    const ballBottom = Math.max(this.y + Ball.BALL_WIDTH, this.y + Ball.BALL_WIDTH + this.deltaY);

    const p1x = Math.max(ballLeft,   obst.x);
    const p1y = Math.max(ballTop,    obst.y);
    const p2x = Math.min(ballRight,  obst.x + obst.width);
    const p2y = Math.min(ballBottom, obst.y + obst.height);
    return p1x < p2x && p1y < p2y;
  }

  #createImage(imageSource) {
    const newImg = new Image();
    newImg.src = imageSource;
    return newImg;
  }
}
