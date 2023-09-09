import Animation from "./animation";
import KeyManager from './keyManager';

export default class AnimationWithObstacle extends Animation {
   
    constructor(canvas,ObstacleL,ObstacleR){
        super(canvas);
        this.myObstacleL = ObstacleL;
        this.myObstacleR = ObstacleR;
        this.keyManager = new KeyManager();
        this.addBall();
    }

    animate(){
        this.mycontext.clearRect(0, 0, this.myCanvas.width, this.myCanvas.height);

        const x = this.myCanvas.width /2; // X-coordinate of the line

        // Set the line properties
        this.mycontext.strokeStyle = 'white'; // Color of the line
        this.mycontext.lineWidth = 2; // Width of the line

        // Draw the vertical line
        this.mycontext.beginPath();
        this.mycontext.moveTo(x, 0); // Start point of the line
        this.mycontext.lineTo(x, this.myCanvas.height); // End point of the line
        this.mycontext.stroke();


        this.myObstacleL.handleMoveKeys(this.keyManager);
        this.myObstacleL.move(this.myCanvas);
        this.myObstacleL.draw(this.mycontext,"white");
        this.myObstacleR.handleMoveKeys(this.keyManager);
        this.myObstacleR.move(this.myCanvas);
        this.myObstacleR.draw(this.mycontext,"white");
        // exécution du traitement pour calculer le déplacement
        if (!this.Ball.move(this.myCanvas,this.myObstacleL,this.myObstacleR)) {
            this.addBall();
        }
        //this.Ball = (!this.Ball.collisionWith(this.myObstacleL) && !this.Ball.collisionWith(this.myObstacleR))?this.Ball:alert("GAME OVER!");
        // exécution du traitement pour le dessin
        this.Ball.draw(this.mycontext);
        // à la fin de la fonction on renouvelle la requête pour la prochaine animation
        this.req = window.requestAnimationFrame(this.animate.bind(this));
    }

    keyDownActionHandler(event) {
        switch (event.key) {
            case "w":
                this.keyManager.upPressedL();
                break;
            case "W":
                this.keyManager.upPressedL();
                break;                
            case "s":
                this.keyManager.downPressedL();
                break;
            case "S":
                this.keyManager.downPressedL();
                break;
            case "ArrowUp":
            case "Up":
                this.keyManager.upPressedR();
                break;
            case "ArrowDown":
            case "Down":
                this.keyManager.downPressedR();
                break;
            default: return;
        }
        event.preventDefault();
    }

    keyUpActionHandler(event) {
        switch (event.key) {
            case "w":
                this.keyManager.upReleasedL();
                break;
            case "s":
                this.keyManager.downReleasedL();
                break;
            case "W":
                this.keyManager.upReleasedL();
                break;
            case "S":
                this.keyManager.downReleasedL();
                break;
            case "ArrowUp":
            case "Up":
                this.keyManager.upReleasedR();
                break;
            case "ArrowDown":
            case "Down":
                this.keyManager.downReleasedR();
                break;
            default: return;
        }
        event.preventDefault();
    }


}