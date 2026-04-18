import Game from './game.js';
import '../style/style.css';

window.addEventListener('load', () => {
  const canvas = document.getElementById('playfield');
  new Game(canvas);
});
