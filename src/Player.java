import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.File;

public class Player {

   private final static int PLAYER_SIZE = 30;
   private Image pacmanSpritesheet;
   private Rectangle2D[] sprites;
   private double posX;
   private double posY;
   private boolean dead;
   private boolean invincible;
   private int score;
   private Direction direction;
   private int animationCounter;

   public Player(double posX, double posY) {
      this.posX = posX;
      this.posY = posY;
      this.dead = false;
      this.score = 0;
      this.invincible = false;
      this.direction = Direction.LEFT;
      this.animationCounter = 0;
   }

   public double getPosY() {
      return posY;
   }

   public void setImage(String filename) throws Exception {
      this.pacmanSpritesheet = new Image(getClass().getResource(filename).toString());
      this.sprites = new Rectangle2D[10];
      for (int y = 0, index = 0; y < 5; y++) {
         for (int x = 0; x < 2; x++, index++) {
            sprites[index] = new Rectangle2D(x * PLAYER_SIZE, y * PLAYER_SIZE, PLAYER_SIZE, PLAYER_SIZE);
         }
      }
   }

   public void setPosition(double posX, double posY) {
      this.posX += posX;
      this.posY += posY;
   }

   public double getPosX() {
      return posX;
   }

   public int getScore() {
      return score;
   }

   public void setScore(int score) {
      this.score += score;
   }

   public void setDead(boolean dead) {
      this.dead = dead;
   }

   public boolean isDead() {
      return dead;
   }

   public void render(GraphicsContext gc) {
      if (!dead) {
         setAnimationCounter();
         if (direction == Direction.UP) {
            renderUpAnimation(gc);
         } else if (direction == Direction.DOWN) {
            renderDownAnimation(gc);
         } else if (direction == Direction.LEFT) {
            renderLeftAnimation(gc);
         } else if (direction == Direction.RIGHT) {
            renderRightAnimation(gc);
         } else {
            renderStable(gc);
         }
      }
      else {
         renderDead(gc);
      }
   }

   public Rectangle2D getBoundary() {
      return new Rectangle2D(posX, posY, PLAYER_SIZE, PLAYER_SIZE);
   }

   public boolean isInvincible() {
      return invincible;
   }

   public void setInvincible(boolean invincible) {
      this.invincible = invincible;
   }

   public void setDirection(Direction direction) {
      this.direction = direction;
   }

   private void setAnimationCounter() {
      animationCounter++;
      if (animationCounter >= 10) {
         animationCounter = 0;
      }
   }

   private void renderUpAnimation(GraphicsContext gc) {
      if (animationCounter < 5) {
         gc.drawImage(pacmanSpritesheet, sprites[6].getMinX(), sprites[6].getMinY(), PLAYER_SIZE, PLAYER_SIZE,
                 posX, posY, PLAYER_SIZE, PLAYER_SIZE);
      }
      else {
         gc.drawImage(pacmanSpritesheet, sprites[7].getMinX(), sprites[7].getMinY(), PLAYER_SIZE, PLAYER_SIZE,
                 posX, posY, PLAYER_SIZE, PLAYER_SIZE);
      }
   }

   private void renderDownAnimation(GraphicsContext gc) {
      if (animationCounter < 5) {
         gc.drawImage(pacmanSpritesheet, sprites[4].getMinX(), sprites[4].getMinY(), PLAYER_SIZE, PLAYER_SIZE,
                 posX, posY, PLAYER_SIZE, PLAYER_SIZE);
      }
      else {
         gc.drawImage(pacmanSpritesheet, sprites[5].getMinX(), sprites[5].getMinY(), PLAYER_SIZE, PLAYER_SIZE,
                 posX, posY, PLAYER_SIZE, PLAYER_SIZE);
      }
   }

   private void renderLeftAnimation(GraphicsContext gc) {
      if (animationCounter < 5) {
         gc.drawImage(pacmanSpritesheet, sprites[2].getMinX(), sprites[2].getMinY(), PLAYER_SIZE, PLAYER_SIZE,
                 posX, posY, PLAYER_SIZE, PLAYER_SIZE);
      }
      else {
         gc.drawImage(pacmanSpritesheet, sprites[3].getMinX(), sprites[3].getMinY(), PLAYER_SIZE, PLAYER_SIZE,
                 posX, posY, PLAYER_SIZE, PLAYER_SIZE);
      }
   }

   private void renderRightAnimation(GraphicsContext gc) {
      if (animationCounter < 5) {
         gc.drawImage(pacmanSpritesheet, sprites[0].getMinX(), sprites[0].getMinY(), PLAYER_SIZE, PLAYER_SIZE,
                 posX, posY, PLAYER_SIZE, PLAYER_SIZE);
      }
      else {
         gc.drawImage(pacmanSpritesheet, sprites[1].getMinX(), sprites[1].getMinY(), PLAYER_SIZE, PLAYER_SIZE,
                 posX, posY, PLAYER_SIZE, PLAYER_SIZE);
      }
   }

   private void renderStable(GraphicsContext gc) {
      gc.drawImage(pacmanSpritesheet, sprites[8].getMinX(), sprites[8].getMinY(), PLAYER_SIZE, PLAYER_SIZE,
              posX, posY, PLAYER_SIZE, PLAYER_SIZE);
   }

   private void renderDead(GraphicsContext gc) {
      gc.drawImage(pacmanSpritesheet, sprites[9].getMinX(), sprites[9].getMinY(), PLAYER_SIZE, PLAYER_SIZE,
              posX, posY, PLAYER_SIZE, PLAYER_SIZE);
   }
}
