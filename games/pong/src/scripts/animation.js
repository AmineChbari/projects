import Ball from './ball';

export default class Animation {

  constructor(canvas) {
    this.myCanvas = canvas;
    this.mycontext = this.myCanvas.getContext('2d');
    this.Ball = null;
    this.req = null;
  }

  alea(n) {
    return Math.random() > 0.5 ? n : -n;
  }

  addBall() {
    const deltaX = this.alea(5);
    const deltaY = this.alea(3);
    this.Ball = new Ball(380, 200, deltaX, deltaY);
  }

  animate() {
    this.mycontext.clearRect(0, 0, this.myCanvas.width, this.myCanvas.height);
    this.Ball.move(this.myCanvas);
    this.Ball.draw(this.mycontext);
    this.req = window.requestAnimationFrame(this.animate.bind(this));
  }

  startAndStop() {
    if (this.req) {
      window.cancelAnimationFrame(this.req);
      this.req = null;
    } else {
      if (!this.Ball) this.addBall();
      this.req = window.requestAnimationFrame(this.animate.bind(this));
    }
  }
}
