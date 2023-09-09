import Obst from "./obstacle";

export default class ObstacleR extends Obst {
   
    constructor(x,y,width,height){
        super(x,y,width,height);
    }

    handleMoveKeys(keyManager) {
        this.stopMoving();    // on réinitialise les déplacements
        if (keyManager.upR)  // touche flèche haut pressée ?
           this.moveUp();
        if (keyManager.downR) // touche flèche bas pressée ?
           this.moveDown();            
    }
}