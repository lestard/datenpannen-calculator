package de.hszg.datenpannen.main.view.menu;

import de.hszg.datenpannen.main.view.dialogues.AboutView;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Presenter für das Menü. Hier werden alle Aktionen, die über das Menü
 * ausführbar sind, koordiniert.
 */
public class MenuPresenter{

    public void about(){
        AboutView aboutView = new AboutView();
        Parent aboutNode = aboutView.getView();

        Stage aboutStage = new Stage();
        aboutStage.setScene(new Scene(aboutNode));
        aboutStage.show();
    }

    public void close(){
        Platform.exit();
    }
}
