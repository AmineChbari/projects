/*
   A KeyManager can be used to store the pressed keys.
   It provides accessors for left, right, up and down keys.
   Methods leftPressed()/leftReleased() must be called when left key is pressed or releasee.
   Simular methods exist for right, up and down.
*/
export default class KeyManager {
   // #left;
   // #right;
   #upL; 
   #downL;
   #upR; 
   #downR;

   constructor() {
      // this.#left = false;
      // this.#right = false;
      this.#upL = false;
      this.#downL = false;
      this.#upR = false;
      this.#downR = false;
   }
   // /* accessor for left key, true when pressed  */
   // get left() {
   //    return this.#left;
   // }
   // /* setter for left key */
   // set left(value) {
   //    this.#left = value;
   // }
   // /* stores that left key is pressed */
   // leftPressed() {
   //    this.#left = true;
   // }
   // /* stores that left key is no more pressed */
   // leftReleased() {
   //    this.#left = false;
   // }

   // get right() {
   //    return this.#right;
   // }
   // set right(value) {
   //    this.#right = value;
   // }
   // rightPressed() {
   //    this.#right = true;
   // }
   // rightReleased() {
   //    this.#right = false;
   // }
   
   get upL() {
      return this.#upL;
   }
   set upL(value) {
      this.#upL = value;
   }
   upPressedL() {
      this.#upL = true;
   }
   upReleasedL() {
      this.#upL = false;
   }

   get upR() {
      return this.#upR;
   }
   set upR(value) {
      this.#upR = value;
   }
   upPressedR() {
      this.#upR = true;
   }
   upReleasedR() {
      this.#upR = false;
   }

   get downL() {
      return this.#downL;
   }
   set downL(value) {
      this.#downL = value;
   }
   downPressedL() {
      this.#downL = true;
   }
   downReleasedL() {
      this.#downL = false;
   }

   get downR() {
      return this.#downR;
   }
   set downR(value) {
      this.#downR = value;
   }
   downPressedR() {
      this.#downR = true;
   }
   downReleasedR() {
      this.#downR = false;
   }

   oneKeyPressed() {
      return /*this.#left || this.#right || */ this.#upL || this.#downL || this.#upR || this.#downR;
   }
   noKeyPressed() {
      return ! this.oneKeyPressed();
   }
}