package com.conway.MyApp;

import com.conway.*;

import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class MyAppController {
    private MyAppView view;
    private conwayApp model;
    
    VBox mainNode;
    Label fpsCounterLabel;
    Label statusLabel;

    public MyAppController(MyAppView myView, conwayApp myModel) {
        this.model = myModel;
        this.view = myView;
        if(model !=null && model.appController !=null)
            model.appController.fpsCounterLabel.setText("starting");
        
        //Register main application events
        if(view.getQMenuItem() == null){
            System.out.println("getQMenuItem is null" );
        }
        else{
            view.getQMenuItem().setOnAction(t -> { handleQuit(t); }  );
        }

    }
     

    public void handleQuit(ActionEvent t) {
        this.cleanupBeforeExit();
        System.exit(1);
    }

    public void cleanupBeforeExit() {
        

    }
    public void updateFPS(double fps){
        fpsCounterLabel.setText(String.valueOf(fps));
    }


    public void setStatus(String newStatus) {
        statusLabel.setText(newStatus);
    }
}
