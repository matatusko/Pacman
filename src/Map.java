import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Map {

   private final static int MAP_SIZE = 20;
   private final static int TILE_SIZE = 40;
   private int[][] map;
   private Image[] image;
   private ArrayList<Tile> tilesList;
   private int totalPoints;

   public Map(String filename) {
      totalPoints = 0;
      readFile(filename);
      loadImages();
      setTiles();
   }

   private void readFile(String filename) {

      try {

         InputStreamReader fr = new InputStreamReader(getClass().getResourceAsStream(filename));
         BufferedReader br = new BufferedReader(fr);
         Scanner scanner = new Scanner(br);

         map = new int[MAP_SIZE][MAP_SIZE];
         while (scanner.hasNext()) {
            for (int i = 0; i < MAP_SIZE; i++) {
               for (int j = 0; j < MAP_SIZE; j++) {
                  map[i][j] = scanner.nextInt();
               }
            }
         }

         scanner.close();
         br.close();

      } catch (FileNotFoundException e) {
         System.out.println("File not found: " + filename);
      } catch (IOException e) {
         System.out.println("IO-Error open/close of file" + filename);
      }
   }

   private void loadImages() {
      image = new Image[4];
      image[0] = new Image(getClass().getResource("assets/sprites/blank.png").toString());
      image[1] = new Image(getClass().getResource("assets/sprites/wall.png").toString());
      image[2] = new Image(getClass().getResource("assets/sprites/point.png").toString());
      image[3] = new Image(getClass().getResource("assets/sprites/magicPoint.png").toString());
   }

   private void setTiles() {
      tilesList = new ArrayList<Tile>();
      for (int x = 0; x < MAP_SIZE; x++) {
         for (int y = 0; y < MAP_SIZE; y++) {
            int textureIndex = map[y][x];
            if (textureIndex == 2 || textureIndex == 3) {
               totalPoints++;
            }
            Tile tile = new Tile(textureIndex, image[textureIndex], x*TILE_SIZE, y*TILE_SIZE);
            tilesList.add(tile);
         }
      }
   }

   public void render(GraphicsContext gc) {
      for (Tile tile: tilesList ) {
         tile.render(gc);
      }
   }

   public Image[] getImage() {
      return image;
   }

   public ArrayList<Tile> getTilesList() {
      return tilesList;
   }

   public int getTotalPoints() {
      return totalPoints;
   }

   public void setTotalPoints(int totalPoints) {
      this.totalPoints -= totalPoints;
   }
}
