
// importation de la classe Game.js
import Game from './game.js';


// mise en place de l'action des clics sur les boutons + les gestionnaires du clavier pour contrôler le panier
const init = () => {
   const canvas = document.getElementById("playfield");
   const game = new Game(canvas);
}

window.addEventListener("load", init);

//
console.log('le bundle a été généré');
