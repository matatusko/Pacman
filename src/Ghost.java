import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.File;

public class Ghost {

   private final static int GHOST_SIZE = 40;
   private int posX, futurePosX;
   private int posY, futurePosY;
   private Image ghostSpritesheet;
   private Rectangle2D[] sprites;
   private boolean eatable;
   private double frameDuration;
   private final long startNanoTime;
   private Direction direction;
   private boolean ghostCollision;
   private boolean wallCollision;
   private ghostAI ghostType;

   public Ghost(int posX, int posY, ghostAI ghostType, String filename) {
      setSpritesheet(filename);
      this.posX = posX;
      this.posY = posY;
      this.futurePosX = posX;
      this.futurePosY = posY;
      this.eatable = false;
      this.frameDuration = 0.200;
      this.startNanoTime = System.nanoTime();
      this.direction = Direction.UP;
      this.ghostCollision = false;
      this.wallCollision = false;
      this.ghostType = ghostType;
   }

   public void render(GraphicsContext gc, long currentNanoTime) {
      // Get the current time for the preparation of the next frame
      double t = (currentNanoTime - startNanoTime) / 1000000000.0;
      // Calculate the next ghost frame to render for the animation effect
      int index = (int)((t % ((sprites.length - 1) * frameDuration)) / frameDuration);
      // Render the ghost in the right animation frame
      if (!eatable) {
         gc.drawImage(ghostSpritesheet, sprites[index].getMinX(), sprites[index].getMinY(), GHOST_SIZE, GHOST_SIZE,
                 posX, posY, GHOST_SIZE, GHOST_SIZE);
      }
      else {
         gc.drawImage(ghostSpritesheet, sprites[4].getMinX(), sprites[4].getMinY(), GHOST_SIZE, GHOST_SIZE,
                 posX, posY, GHOST_SIZE, GHOST_SIZE);
      }
   }

   public Rectangle2D getBoundary() {
      return new Rectangle2D(posX, posY, GHOST_SIZE, GHOST_SIZE);
   }

   public Rectangle2D getFutureBoundary() {
      return new Rectangle2D(futurePosX, futurePosY, GHOST_SIZE, GHOST_SIZE);
   }

   private void setSpritesheet(String filename) {
      this.ghostSpritesheet = new Image(getClass().getResource(filename).toString());
      this.sprites = new Rectangle2D[5];
      for (int x = 0, index = 0; x < 5; x++, index++) {
         sprites[index] = new Rectangle2D(x * GHOST_SIZE, 0, GHOST_SIZE, GHOST_SIZE);
      }
   }

   public void checkGhostNextMoveForCollision() {
      if (direction == Direction.LEFT) {
         futurePosX -= 4;
      } else if (direction == Direction.RIGHT) {
         futurePosX += 4;
      } else if (direction == Direction.UP) {
         futurePosY -= 4;
      } else if (direction == Direction.DOWN) {
         futurePosY += 4;
      }
   }

   public void moveGhost() {
      if (!ghostCollision) {
         if (direction == Direction.LEFT) {
            posX -= 4;
         } else if (direction == Direction.RIGHT) {
            posX += 4;
         } else if (direction == Direction.UP) {
            posY -= 4;
         } else if (direction == Direction.DOWN) {
            posY += 4;
         }
         futurePosX = posX;
         futurePosY = posY;
      }
      else {
         futurePosX = posX;
         futurePosY = posY;
         setNewDirection();
         ghostCollision = false;
      }
   }

   public void moveBack() {
      if (wallCollision) {
         if (direction == Direction.LEFT) {
            posX += 4;
         } else if (direction == Direction.RIGHT) {
            posX -= 4;
         } else if (direction == Direction.UP) {
            posY += 4;
         } else if (direction == Direction.DOWN) {
            posY -= 4;
         }
         setNewDirection();
         wallCollision = false;
      }
   }

   private void setNewDirection() {
      int random = (int) Math.floor(Math.random() * 4);
      if (random == 0) {
         direction = Direction.UP;
      }
      else if (random == 1) {
         direction = Direction.DOWN;
      }
      else if (random == 2) {
         direction = Direction.RIGHT;
      }
      else if (random == 3) {
         direction = Direction.LEFT;
      }
   }

   public void setGhostCollision(boolean collision) {
      this.ghostCollision = collision;
   }

   public void setWallCollision(boolean wallCollision) {
      this.wallCollision = wallCollision;
   }

   public void setPosition(int posX, int posY) {
      this.posX = posX;
      this.posY = posY;
   }

   public void setEatable(boolean eatable) {
      this.eatable = eatable;
   }

   public ghostAI getGhostType() {
      return ghostType;
   }

   // TODO
   // Optional left if there's some time left and I feel up to a challenge:
   // Implement AI for each specific ghost as it was in the original
   /*
   public void moveGhost() {
      if (currentBehaviour == ghostAI.SCATTER) {
         scatterGhost();
      }
      else if (currentBehaviour == ghostAI.RED) {
         chaseAfterPlayer();
      }
      else if (currentBehaviour == ghostAI.PINK) {
         interceptPlayer();
      }
      else if (currentBehaviour == ghostAI.GREEN) {
         undecided();
      }
      else if (currentBehaviour == ghostAI.BROWN) {
         shyAway();
      }
      else if (currentBehaviour == ghostAI.RUNAWAY) {
         runAway();
      }
   }

   private void scatterGhost() {
      if (ghostType == ghostAI.RED) {
         topRightCorner();
      }
      else if (ghostType == ghostAI.PINK) {
         topLeftCorner();
      }
      else if (ghostType == ghostAI.BROWN) {
         bottomRightCorner();
      }
      else if (ghostType == ghostAI.GREEN) {
         bottomLeftCorner();
      }
   }

   private void chaseAfterPlayer() {

   }

   private void interceptPlayer() {

   }

   private void undecided() {

   }

   private void shyAway() {

   }

   private void runAway() {

   }

   private void topRightCorner() {

   }

   private void topLeftCorner() {

   }

   private void bottomRightCorner() {

   }

   private void bottomLeftCorner() {

   }
   */
}