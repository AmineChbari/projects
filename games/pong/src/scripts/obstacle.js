export default class Obstacle {
  constructor(x, y, width, height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.deltaX = 0;
    this.deltaY = 0;
  }

  draw(context, color) {
    context.fillStyle = color;
    context.fillRect(this.x, this.y, this.width, this.height);
  }

  moveLeft()   { this.deltaX -= 10; }
  moveRight()  { this.deltaX += 10; }
  moveUp()     { this.deltaY -= 10; }
  moveDown()   { this.deltaY += 10; }
  stopMoving() { this.deltaX = 0; this.deltaY = 0; }

  move(box) {
    this.x = Math.max(0, Math.min(box.width  - this.width,  this.x + this.deltaX));
    this.y = Math.max(4, Math.min(box.height - this.height - 4, this.y + this.deltaY));
  }
}
