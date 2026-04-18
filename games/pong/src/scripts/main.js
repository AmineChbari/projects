import AnimationWithObstacle from './animationWithObstacle';
import ObstacleL from './obstL';
import ObstacleR from './obstR';
import './assets/style/style-balles.css';

const init = () => {
  const canvas = document.getElementById('terrain');

  const paddleL = new ObstacleL(5,   200, 12, 100);
  const paddleR = new ObstacleR(983, 200, 12, 100);
  const game    = new AnimationWithObstacle(canvas, paddleL, paddleR);

  document.getElementById('stopStartBall').addEventListener('click', () => game.startAndStop());
  window.addEventListener('keydown', game.keyDownActionHandler.bind(game));
  window.addEventListener('keyup',   game.keyUpActionHandler.bind(game));
};

window.addEventListener('DOMContentLoaded', init);
