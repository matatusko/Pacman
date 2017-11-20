import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class MainMenu {

   private Button pauseButton, newGameButton, quitButton;
   private boolean pause;
   private boolean newGamePressed;
   Group root;
   private VBox vbox;
   private String mapSelected;

   public MainMenu(Group root) {
      this.root = root;
      this.newGamePressed = false;
      this.mapSelected = "map1";
      this.vbox = new VBox();
      initializeButtons();
      addActionToButtons();
      addHoverEffectsToButtons();
      addStyleToButtons();
      createRadioButtons();

      root.getChildren().addAll(pauseButton, newGameButton, quitButton);
   }

   // Initialize all the buttons put them in the right place and give them corresponding
   // functionality.
   private void initializeButtons() {
      newGameButton = new Button("New Game");
      pauseButton = new Button("Pause");
      quitButton = new Button("Quit");
   }

   private void addActionToButtons() {
      pauseButton.setOnAction(event -> {
         if (pause == true) {
            pause = false;
         }
         else {
            pause = true;
         }
      });
      newGameButton.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent event) {
            newGamePressed = true;
         }
      });
      quitButton.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent event) {
            System.exit(0);
         }
      });
   }

   private void addHoverEffectsToButtons() {
      // Set the button effects when player hovers over or out
      DropShadow shadow = new DropShadow();
      newGameButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent e) {
            newGameButton.setEffect(shadow);
         }
      });
      newGameButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent e) {
            newGameButton.setEffect(null);
         }
      });
      pauseButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent e) {
            newGameButton.setEffect(shadow);
         }
      });
      pauseButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent e) {
            newGameButton.setEffect(null);
         }
      });
      quitButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent e) {
            newGameButton.setEffect(shadow);
         }
      });
      quitButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent e) {
            newGameButton.setEffect(null);
         }
      });
   }

   private void addStyleToButtons() {
      // Style the button so it feels more as a part of a game
      pauseButton.setStyle("-fx-font: 25 arial; -fx-base: #ee2211; -fx-focus-color: transparent;");
      pauseButton.setPrefWidth(160);
      pauseButton.setPrefHeight(50);
      pauseButton.relocate(820, 600);
      newGameButton.setStyle("-fx-font: 25 arial; -fx-base: #ee2211; -fx-focus-color: transparent;");
      newGameButton.setPrefWidth(160);
      newGameButton.setPrefHeight(50);
      newGameButton.relocate(820, 650);
      quitButton.setStyle("-fx-font: 25 arial; -fx-base: #ee2211; -fx-focus-color: transparent;");
      quitButton.setPrefWidth(160);
      quitButton.setPrefHeight(50);
      quitButton.relocate(820, 700);
   }

   private void createRadioButtons() {
      final ToggleGroup mapSelector = new ToggleGroup();

      Label map2label = new Label("Map #2");
      map2label.setStyle("-fx-font-size: 25px;");

      Label map1label = new Label("Map #1");
      map1label.setStyle("-fx-font-size: 25px;");

      RadioButton map1 = new RadioButton();
      map1.setGraphic(map1label);
      map1.setToggleGroup(mapSelector);
      map1.setSelected(true);
      map1.setUserData("map1");

      RadioButton map2 = new RadioButton();
      map2.setGraphic(map2label);
      map2.setToggleGroup(mapSelector);
      map2.setUserData("map2");

      mapSelector.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
         if (mapSelector.getSelectedToggle() != null) {
            mapSelected = mapSelector.getSelectedToggle().getUserData().toString();
            newGamePressed = true;
         }
      });

      vbox.getChildren().addAll(map1, map2);
      vbox.setSpacing(10);
      vbox.relocate(820, 500);

      root.getChildren().add(vbox);
   }

   public boolean isPause() {
      return pause;
   }

   public void setPause(boolean pause) {
      this.pause = pause;
   }

   public boolean isNewGamePressed() {
      return newGamePressed;
   }

   public void setNewGamePressed(boolean newGamePressed) {
      this.newGamePressed = newGamePressed;
   }

   public String getMapSelected() {
      return mapSelected;
   }
}
