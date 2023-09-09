import Ball from './ball';

/* TYPE Animation */
export default class Animation {

  constructor(canvas) {
    this.myCanvas = canvas;
    this.mycontext = this.myCanvas.getContext("2d");
    this.Ball = null;
    this.req = null;
  }


  alea(n) {
    const choices = [n, -n];
    // Generate a random index either 0 or 1
    const randomIndex = Math.floor(Math.random() * choices.length);
    // Return the choice at the random index
    return choices[randomIndex];
  }

  alea2(n) {
    return Math.floor(Math.random() * (n+1) );
  }


  addBall(){
    let deltaX,deltaY;
      deltaX = this.alea(5);
      deltaY = this.alea2(3);        // changi hadi bach kora tmchi f kola mera f jiha 
    this.Ball = new Ball(380,200,deltaX,deltaY);
  }

  animate(){
    this.mycontext.clearRect(0, 0, this.myCanvas.width, this.myCanvas.height);
    // exécution du traitement pour calculer le déplacement
    this.Ball.move(this.myCanvas);      
    // exécution du traitement pour le dessin
    this.Ball.draw(this.mycontext);
    // à la fin de la fonction on renouvelle la requête pour la prochaine animation
    this.req = window.requestAnimationFrame(this.animate.bind(this)); 
  }

  /* start the animation or stop it if previously running */
  startAndStop() {
    if (this.req) {
      window.cancelAnimationFrame(this.req);
      this.req = null;
    } else {
      this.req = window.requestAnimationFrame(this.animate.bind(this));
    }
  }
}
