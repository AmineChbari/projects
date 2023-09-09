
import Ball from './ball';           // peut être commenté après la Ex1 Q2 faite
import Animation from './animation';
import ObstacleL from './obstL';
import ObstacleR from './obstR';

import './assets/style/style-balles.css';
import AnimationWithObstacle from './animationWithObstacle';


/* setup */
const init = () => {
  const canvas = document.getElementById("terrain");
  const context = canvas.getContext('2d');
  const canvasWidth = canvas.width;
  const canvasHeight = canvas.height;
  
  
const lineX = canvasWidth / 2; // X-coordinate of the middle line

const text = 'PING PONG GAME'; // Two-word text to be written on the line
const fontSize = 36; // Increased font size in pixels

context.font = `${fontSize}px Arial`;
context.textAlign = 'center';
context.textBaseline = 'middle';
context.fillStyle = 'white';

// Position the text at the center of the canvas vertically
const textY = canvasHeight / 2;

// Write the text on the line
context.fillText(text, lineX, textY);

  // commenter les 2 lignes suivantes après la Ex1 Q2
  // const ball = new Ball(50,50);
  // document.getElementById("stopStartBall").addEventListener("click", () => ball.draw(canvas.getContext('2d'))  );

  // décommenter les deux lignes suivantes à partir la question Ex1 Q4
  const Obst1 = new ObstacleR(985,200,10,100);
  const Obst2 = new ObstacleL(5,200,10,100);
  const anm = new AnimationWithObstacle(canvas,Obst1,Obst2);
  document.getElementById("stopStartBall").addEventListener("click", () => anm.startAndStop());
  window.addEventListener('keydown', anm.keyDownActionHandler.bind(anm));
  window.addEventListener('keyup', anm.keyUpActionHandler.bind(anm));
}

window.addEventListener("DOMContentLoaded",init);



//
console.log('le bundle a été généré');
