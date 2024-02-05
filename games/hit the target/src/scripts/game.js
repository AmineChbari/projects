
export default class Game {

   #canvas;
   // à compléter

   constructor(canvas) {
      this.#canvas = canvas;
      // à compléter
   }

   /** donne accès au canvas correspondant à la zone de jeu */
   get canvas() {
      return this.#canvas;
   }
}



