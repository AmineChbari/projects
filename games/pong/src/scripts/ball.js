import ballImgSrc from './assets/images/ball.png';

export default class Ball {

  static BALL_WIDTH = 12;

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
    const nx = this.x + this.deltaX;
    const ny = this.y + this.deltaY;

    if (nx + Ball.BALL_WIDTH > canvas.width) return 'right';
    if (nx < 0) return 'left';

    if (ny + Ball.BALL_WIDTH > canvas.height || ny < 0) {
      this.deltaY = -this.deltaY;
    }

    if (theObst1 && theObst2 && (this.collisionWith(theObst1) || this.collisionWith(theObst2))) {
      this.deltaX = -this.deltaX;
      if (Math.abs(this.deltaX) < 14) this.deltaX *= 1.08;
      this.deltaY = this.setRandomDirection();
    }

    this.x += this.deltaX;
    this.y += this.deltaY;
    return true;
  }

  collisionWith(obst) {
    const p1x = Math.max(this.x, obst.x);
    const p1y = Math.max(this.y, obst.y);
    const p2x = Math.min(this.x + Ball.BALL_WIDTH, obst.x + obst.width);
    const p2y = Math.min(this.y + Ball.BALL_WIDTH, obst.y + obst.height);
    return p1x < p2x && p1y < p2y;
  }

  #createImage(imageSource) {
    const newImg = new Image();
    newImg.src = imageSource;
    return newImg;
  }
}
