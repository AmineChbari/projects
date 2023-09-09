
// la source de l'image à utiliser pour la balle
import ballImgSrc from './assets/images/ball.png';

/* TYPE Ball */
export default class Ball {

	static BALL_WIDTH = 48;

  constructor(px, py, dx=3, dy=-2) {
    this.x = px;
    this.y = py;
    this.deltaX = dx;
    this.deltaY = dy;
    this.speedMultiplier = 1;
    this.image = this.#createImage(ballImgSrc);
    this.setRandomDirection();
  }

  /* draw this ball, using the given drawing 2d context */
  draw(context) {
    context.drawImage(this.image, this.x, this.y);
  }

  setRandomDirection() {
    // Generate random values for deltaX and deltaY
    //this.deltaX = (Math.random() > 0.5 ? 1 : -1) * (Math.random() * 3 + 1); // Random value between -1 and 1, multiplied by a random speed factor
    return (Math.random() > 0.5 ? 1 : -1) * (Math.random() * 3 + 1); // Random value between -1 and 1, multiplied by a random speed factor
    
}

  move(canvas,theObst1,theObst2) {
    const rx = this.x + this.deltaX + Ball.BALL_WIDTH;
    const ry = this.y + this.deltaY + Ball.BALL_WIDTH;
    if (rx > canvas.width || this.x + this.deltaX < 0) {
      return false;
    }

    if (ry > canvas.height || this.y + this.deltaY < 0) {
      this.deltaY = -this.deltaY;
    }

    if (this.collisionWith(theObst1) || this.collisionWith(theObst2)){
      this.deltaY = this.setRandomDirection();
      // this.deltaY = this.deltaY;
       this.deltaX = -this.deltaX;

      this.speedMultiplier += 0.01;
        
        this.deltaX *= this.speedMultiplier;
        this.deltaY *= this.speedMultiplier;
    }

    this.x += this.deltaX;
    this.y += this.deltaY;
    return true;
  }

  collisionWith(Obst){
    let P1_x = Math.max(this.x,Obst.x);
    let P1_y = Math.max(this.y,Obst.y);
    let P2_x = Math.min(this.x + Ball.BALL_WIDTH,Obst.x + Obst.width);
    let P2_y = Math.min(this.y + Ball.BALL_WIDTH,Obst.y + Obst.height);
    if (P1_x < P2_x && P1_y < P2_y) return true;
  }

  /* crée l'objet Image à utiliser pour dessiner cette balle */
  #createImage(imageSource) {
	  const newImg = new Image();
  	newImg.src = imageSource;
  	return newImg;
  }
  get width() {
    return this.image.width;
  }
  get height() {
    return this.image.height;
  }

}
