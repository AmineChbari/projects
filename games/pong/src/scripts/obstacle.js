
export default class Obstacle {
    constructor(x,y,width,height){
       this.x=x; 
       this.y=y;
       this.width= width;
       this.height= height;
    }

    draw(context,color) {
        context.fillStyle = color; // Set the desired color here, e.g., "red", "#00FF00", "rgb(255, 0, 0)"
        context.fillRect(this.x, this.y, this.width, this.height);
      }
      

    moveLeft() {              
        this.deltaX = this.deltaX - 10;   // le déplacement se fera vers la gauche, par pas de 10px
    }
    moveRight() {
        this.deltaX = this.deltaX + 10;   // le déplacement se fera vers la droite, par pas de 10px
    }
    moveUp() {              
        this.deltaY = this.deltaY - 10;   // le déplacement se fera vers la gauche, par pas de 10px
    }
    moveDown() {
        this.deltaY = this.deltaY + 10;   // le déplacement se fera vers la droite, par pas de 10px
    }
    stopMoving() {
        this.deltaX = 0;
        this.deltaY = 0;
    }
    move(box) {              // déplace sans sortir des limites de *box*
        this.x = Math.max(0, Math.min(box.width - this.width, this.x + this.deltaX));
        this.y = Math.max(4, Math.min(box.height - this.height - 4, this.y + this.deltaY));
    }
}