package com.conway;

import javafx.event.ActionEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MyAppController {
    VBox mainNode;

    public MyAppController(VBox mainBox) {
        
              // create a menu 
            Menu m = new Menu("Menu"); 
    
            // create menuitems 
            MenuItem m1 = new MenuItem("menu item 1"); 
            MenuItem m2 = new MenuItem("menu item 2"); 
            MenuItem m3 = new MenuItem("Quit"); 

            // adde event handlers
            m3.setOnAction(t -> { handleQuit(t); }  );
            m2.setOnAction(t -> { handle(t); }      );
            m1.setOnAction(t -> { handle(t); }      );


            // add menu items to menu 
            m.getItems().add(m1); 
            m.getItems().add(m2); 
            m.getItems().add(m3); 
    
            // create a menubar 
            MenuBar mb = new MenuBar(); 
  
            // add menu to menubar 
            mb.getMenus().add(m); 
            HBox menuBox = new HBox(mb);

            mainBox.getChildren().add(menuBox);

    }
    
    public void handle(ActionEvent t) {
        
    } 
    public void handleQuit(ActionEvent t) {
        System.exit(1);
    }
}
