package com.conway.MyApp;

import com.conway.GameBoard.*;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MyAppView {
    private VBox root;
    private MenuItem menueItemQuit;
    private Label statusLabel;
    private Label fpsCounterLabel;

    private GridPane gameBoard;

    public MyAppView(GameBoardView gbv){

        
        gameBoard= gbv.getView();
        buildUI();
    }
    
    private void buildUI(){
        root = new VBox();
        
        
            Menu menu         = new Menu("Menu"); 
            HBox menuToolHBox = new HBox(buildMenuBar(menu));
            
            fpsCounterLabel = new Label();
            menuToolHBox.getChildren().add(fpsCounterLabel);
        root.getChildren().add(menuToolHBox);

        if(gameBoard !=null){
            root.getChildren().add(gameBoard);
        }
        
        
            statusLabel = new Label("initializing");
            HBox statusBox = new HBox(statusLabel);

        root.getChildren().add(statusBox);


    }

    private MenuBar buildMenuBar(Menu menue){
        menueItemQuit = new MenuItem("Quit"); 
        menue.getItems().add(menueItemQuit); 

        return new MenuBar(menue); 
        
    }


    public void setfpsCounterLabel(String newLabel){
        fpsCounterLabel.setText(newLabel);
    }

    public Parent getRoot(){
        return root;
    }

    public MenuItem getQMenuItem(){
        return menueItemQuit;
    }

    public void setBoard(GridPane gp){
        this.gameBoard = gp;
    }

}
