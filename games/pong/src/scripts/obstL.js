import Obst from "./obstacle";

export default class ObstacleL extends Obst {
   
    constructor(x,y,width,height){
        super(x,y,width,height);
    }

    handleMoveKeys(keyManager) {
        this.stopMoving();    // on réinitialise les déplacements
        if (keyManager.upL)  // touche flèche haut pressée ?
           this.moveUp();
        if (keyManager.downL) // touche flèche bas pressée ?
           this.moveDown();           
    }
}