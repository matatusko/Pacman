import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Tile {

   private final static int TILE_SIZE = 40;
   private TileType type;
   private Image image;
   private int posX;
   private int posY;

   public Tile(int textureType, Image image, int posX, int posY) {
      this.image = image;
      this.posX = posX;
      this.posY = posY;
      initializeType(textureType);
   }

   public void render(GraphicsContext gc) {
      gc.drawImage(image, posX, posY);
   }

   public TileType getType() {
      return type;
   }

   public void setType(TileType type) {
      this.type = type;
   }

   public void setImage(Image image) {
      this.image = image;
   }

   public Rectangle2D getWallBoundary() {

      return new Rectangle2D(posX, posY, TILE_SIZE, TILE_SIZE);
   }

   public Rectangle2D getPointBoundary() {

      return new Rectangle2D(posX, posY, TILE_SIZE / 2, TILE_SIZE / 2);
   }

   private void initializeType(int textureType) {
      if (textureType == 0) {
         this.type = TileType.BLANK;
      }
      else if (textureType == 1) {
         this.type = TileType.WALL;
      }
      else if (textureType == 2) {
         this.type = TileType.POINT;
      }
      else if (textureType == 3) {
         this.type = TileType.MAGIC_POINT;
      }
   }
}
