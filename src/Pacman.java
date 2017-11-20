import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class Pacman extends Application {

   private final static int SCREEN_WIDTH = 1000;
   private final static int SCREEN_HEIGHT = 800;
   private final static int GAME_WIDTH = 770;
   private final static int GAME_HEIGHT = 770;
   private MainMenu menu;
   private Player player;
   private Map map;
   private ArrayList<Ghost> ghosts;
   private ArrayList<String> input;
   private String title;
   private String gameOverMessage;
   private String winMessage;
   private boolean win;
   private int highScore;
   private MediaPlayer deathSound;
   private MediaPlayer intro;

   public static void main(String[] args) {
      launch(args);
   }

   @Override
   public void start(Stage primaryStage) throws Exception {
      Group root = new Group();
      Scene scene = new Scene(root);
      Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
      root.getChildren().add(canvas);
      GraphicsContext gc = canvas.getGraphicsContext2D();

      menu = new MainMenu(root);

      // Initialize all the variables necessary for the game
      initializeVariables(gc);
      initializeGameObjects();
      // Get player input from the keyboard
      getPlayerInput(input, scene);

      // Play the intro music
      intro.play();
      new AnimationTimer()
      {
         public void handle(long currentNanoTime)
         {
            // Request focus to the main game instead of menu
            canvas.requestFocus();
            checkNewGame();
            // game logic, including handling the input, checking collisions and updating scores
            if (!player.isDead() && !win && !(menu.isPause())) {
               handlePlayerInput();
               moveGhosts();
               handleCollision();
               checkFinish();
            }

            // update the screen and render everything to it
            renderEverything(gc, currentNanoTime);
         }
      }.start();

      primaryStage.setScene(scene);
      primaryStage.setTitle("Pacman");
      primaryStage.show();
   }

   private void checkNewGame() {
      if (menu.isNewGamePressed() == true) {
         initializeGameObjects();
         menu.setNewGamePressed(false);
      }
   }

   private void initializeVariables(GraphicsContext gc) {
      // initialize the user input ArrayList and start the nanoTime for right frame animation
      input = new ArrayList<>();
      ghosts = new ArrayList<>();
      win = false;
      menu.setPause(true);
      highScore = 0;

      // Initialize the font for the right-hand size on-screen information
      Font font = Font.font( "Helvetica", FontWeight.NORMAL, 24 );
      gc.setFont(font);
      gc.setLineWidth(1);
      title = "PAC-MAN";
      gameOverMessage = "\t               Game Over!\n\n" +
                        "\tYou have been eaten by the ghosts!\n" +
                        "\t       Click New Game start over.";
      winMessage = "\t     Congratulations, You won!\n\n" +
                   "\tAll the points have been collected.\n" +
                   "\t   Click New Game to start over.";

      try {
         // load sounds
         Media death = new Media(getClass().getResource("assets/sounds/die.mp3").toString());
         deathSound = new MediaPlayer(death);
         deathSound.setOnEndOfMedia(() -> deathSound.stop());
         Media theIntro = new Media(getClass().getResource("assets/sounds/intro.mp3").toString());
         intro = new MediaPlayer(theIntro);
         intro.setVolume(0.5);
         intro.setOnEndOfMedia(() -> intro.stop());
      }
      catch (Exception e){
         System.out.println("Failed loading the sound assets. Program terminating.");
         System.exit(1);
      }
   }

   private void initializeGameObjects() {
      // Created a player in a given position
      player = new Player(405, 725);
      win = false;
      player.setDead(false);
      menu.setPause(true);
      // Load all the necessary textures for the game and catch an error if it's not found in the files
      try {
         player.setImage("assets/sprites/pacman2.png");
         // Initialize the map from the file, depending on user's setting
         if (menu.getMapSelected().equals("map1")) {
            map = new Map("assets/maps/level1.txt");
         } else {
            map = new Map("assets/maps/level2.txt");
         }
         // Prepare and initialize all the ghost
         ghosts.clear();
         ghosts.add(new Ghost(320, 360, ghostAI.RED, "assets/sprites/GhostRed.png"));
         ghosts.add(new Ghost(360, 360, ghostAI.GREEN, "assets/sprites/GhostGreen.png"));
         ghosts.add(new Ghost(400, 360, ghostAI.PINK, "assets/sprites/GhostPink.png"));
         ghosts.add(new Ghost(440, 360, ghostAI.BROWN, "assets/sprites/GhostBrown.png"));
         intro.play();
      } catch (Exception e) {
         System.out.println("Failed loading and initializing assets. Program terminating.");
         System.exit(1);
      }
   }

   private void getPlayerInput(ArrayList<String> input, Scene scene) {
      // Get player input and store in the input array list for further handling
      scene.setOnKeyPressed(
              e -> {
                 menu.setPause(false);
                 String code = e.getCode().toString();
                 if (!input.contains(code) )
                    input.add(code);
              });

      scene.setOnKeyReleased(
              e -> {
                 String code = e.getCode().toString();
                 input.remove(code);
              });
   }

   private void handlePlayerInput() {
      // Handle the player input depending on what's in the array list and set the pacman
      // direction for the right animation frame. If no movement then set direction to NONE
      player.setDirection(Direction.NONE);

      if (input.contains("LEFT")) {
         player.setPosition(-5, 0);
         player.setDirection(Direction.LEFT);
      }
      if (input.contains("RIGHT")) {
         player.setPosition(5, 0);
         player.setDirection(Direction.RIGHT);
      }
      if (input.contains("UP")) {
         player.setPosition(0, -5);
         player.setDirection(Direction.UP);
      }
      if (input.contains("DOWN")) {
         player.setPosition(0, 5);
         player.setDirection(Direction.DOWN);
      }
   }

   private void moveGhosts() {
      for (int i = 0; i < ghosts.size(); i++) {
         ghosts.get(i).checkGhostNextMoveForCollision();
         for (int j = 0; j < ghosts.size(); j++) {
            if (i == j) {
               // do nothing so we don't compare the same ghosts for collision
            } else {
               // check for potential collision with other ghosts
               if (ghosts.get(i).getFutureBoundary().intersects(ghosts.get(j).getBoundary())) {
                  // Set collision found so we don't move the ghost but change its direction
                  ghosts.get(i).setGhostCollision(true);
               }
            }
         }
         ghosts.get(i).moveGhost();
      }
   }

   private void handleCollision() {
      // Deal with cases where player can 'teleport' from one place to another when there's no wall
      handleWallMovement();

      // Iterate through all the tiles and deal with those respectively
      Iterator<Tile> tilesIterator = map.getTilesList().iterator();

      while (tilesIterator.hasNext()) {
         Tile tile = tilesIterator.next();
         // Check if the tile is a wall, and handle the collision accordingly if so
         if (tile.getType() == TileType.WALL &&
                 player.getBoundary().intersects(tile.getWallBoundary())) {
            handleWallCollision();
         }
         // Check if the tile is a point (or magic point) and handle this accordingly
         else if (tile.getType() == TileType.POINT &&
                 player.getBoundary().intersects(tile.getPointBoundary())) {
            handlePointsCollision(tile);
            map.setTotalPoints(1);
         }
         else if (tile.getType() == TileType.MAGIC_POINT &&
                 player.getBoundary().intersects(tile.getPointBoundary())) {
            handleMagicPointsCollision(tile);
            map.setTotalPoints(1);
         }

         // Check if ghost enters into the wall and change it's direction
         for (Ghost ghost: ghosts) {
            if (tile.getType() == TileType.WALL &&
                    ghost.getBoundary().intersects(tile.getWallBoundary())) {
               ghost.setWallCollision(true);
               ghost.moveBack();
            }
            if (ghost.getBoundary().intersects(player.getBoundary())) {
               if (!player.isInvincible()) {
                  player.setDead(true);
                  deathSound.play();
               }
               else {
                  player.setScore(150);
                  respawnGhosts(ghost);
               }
            }
         }
      }
   }

   private void handleWallMovement() {
      // If player walks out of the stage when there is no walls, teleport the player to the same location
      // on the other end of the screen
      if (player.getPosX() > GAME_WIDTH) {
         player.setPosition(-GAME_WIDTH, 0);
      }
      else if (player.getPosX() < 0) {
         player.setPosition(GAME_WIDTH, 0);
      }
      else if (player.getPosY() > GAME_HEIGHT) {
         player.setPosition(0, -GAME_HEIGHT);
      }
      else if (player.getPosY() < 0) {
         player.setPosition(0, GAME_HEIGHT);
      }
   }

   private void handleWallCollision() {
      // If player walks into the wall return the position to what it was before the event happened
      if (input.contains("LEFT"))
         player.setPosition(+5,0);
      if (input.contains("RIGHT"))
         player.setPosition(-5,0);
      if (input.contains("UP"))
         player.setPosition(0,+5);
      if (input.contains("DOWN"))
         player.setPosition(0,-5);
   }

   private void handlePointsCollision(Tile tile) {
      // Change the image and type of a tile when player eats a point
      tile.setType(TileType.BLANK);
      tile.setImage(map.getImage()[0]);
      player.setScore(10);
   }

   private void handleMagicPointsCollision(Tile tile) {
      // Same as for PointsCollection, but more scores and sets pacman to invincible mode so he can
      // eat ghosts
      tile.setType(TileType.BLANK);
      tile.setImage(map.getImage()[0]);
      player.setScore(100);
      // set the player to invincible mode and change all the ghosts to eatable
      player.setInvincible(true);
      for (Ghost ghost: ghosts) {
         ghost.setEatable(true);
      }

      // Start a new timer which will change all the ghosts and player back to normal after 5sec
      Timer timer = new java.util.Timer();
      timer.schedule(new TimerTask() {
         public void run() {
            Platform.runLater(() -> {
               for (Ghost ghost: ghosts) {
                  ghost.setEatable(false);
               }
               player.setInvincible(false);
            });
         }
      }, 5000);
   }

   private void respawnGhosts(Ghost ghost) {
      // For each eaten ghost, start a new timer to count to 3. While the timer is counting, the ghost
      // if put in the special 'container' where it cannot escape from. After the timer is over
      // the ghost goes back to it's initial point.
      Timer timer = new java.util.Timer();
      if (ghost.getGhostType() == ghostAI.RED) {
         ghost.setPosition(40, 280);
         // Start a new timer which will count the 3sec respawn time for a ghost
         timer.schedule(new TimerTask() {
            public void run() {
               Platform.runLater(() -> {
                  ghost.setPosition(320, 360);

               });
            }
         }, 3000);
      }
      else if (ghost.getGhostType() == ghostAI.GREEN) {
         ghost.setPosition(680, 440);
         // Start a new timer which will count the 3sec respawn time for a ghost
         timer.schedule(new TimerTask() {
            public void run() {
               Platform.runLater(() -> {
                  ghost.setPosition(360, 360);

               });
            }
         }, 3000);
      }
      else if (ghost.getGhostType() == ghostAI.PINK) {
         ghost.setPosition(40, 440);
         // Start a new timer which will count the 3sec respawn time for a ghost
         timer.schedule(new TimerTask() {
            public void run() {
               Platform.runLater(() -> {
                  ghost.setPosition(400, 360);

               });
            }
         }, 3000);
      }
      else if (ghost.getGhostType() == ghostAI.BROWN) {
         ghost.setPosition(680, 280);
         // Start a new timer which will count the 3sec respawn time for a ghost
         timer.schedule(new TimerTask() {
            public void run() {
               Platform.runLater(() -> {
                  ghost.setPosition(440, 360);

               });
            }
         }, 3000);
      }
   }

   private void checkFinish() {
      // Checks if all the points have been eaten. When map is created all the points are being summed together
      // and stored in the totalPoints variable. As player eats the dots, a number is subtracted from that variable.
      // If it's less or equal to 0, it means all the points have been collected and the game is over.
      if (map.getTotalPoints() <= 0) {
         win = true;
      }
   }

   private void renderEverything(GraphicsContext gc, long currentNanoTime) {
      // Paint the main screen Black
      gc.setFill(Color.rgb(0, 0, 0));
      gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

      // Update player score and render it in the right location
      gc.setFill(Color.YELLOW);
      gc.setStroke(Color.YELLOW);
      String score = "Score: " + player.getScore();
      gc.fillText(score, 815, 150);
      gc.strokeText(score, 815, 150);
      // render high score
      updateHighScore();
      String highscore = "High Score: " + highScore;
      gc.fillText(highscore, 815, 180);
      gc.strokeText(highscore, 815, 180);
      // Render title
      gc.fillText(title, 850, 50);
      gc.strokeText(title, 850, 50);

      // Render the map, the player and the ghosts
      map.render(gc);
      player.render(gc);
      for (Ghost ghost: ghosts) {
         ghost.render(gc, currentNanoTime);
      }

      if (player.isDead()) {
         printGameOver(gc);
      }
      if (win) {
         printWin(gc);
      }
   }

   private void updateHighScore() {
      int currentScore = player.getScore();
      if (currentScore > highScore) {
         highScore = currentScore;
      }
   }

   private void printGameOver(GraphicsContext gc) {
      Font font = Font.font("Helvetica", FontWeight.NORMAL, 48);
      gc.setFont(font);
      gc.fillText(gameOverMessage, 0, 270);
      font = Font.font( "Helvetica", FontWeight.NORMAL, 24 );
      gc.setFont(font);
   }

   private void printWin(GraphicsContext gc) {
      Font font = Font.font("Helvetica", FontWeight.NORMAL, 48);
      gc.setFont(font);
      gc.fillText(winMessage, 0, 270);
      font = Font.font( "Helvetica", FontWeight.NORMAL, 24 );
      gc.setFont(font);
   }
}
