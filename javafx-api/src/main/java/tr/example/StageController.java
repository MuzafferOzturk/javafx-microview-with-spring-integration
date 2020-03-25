package tr.example;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tr.example.util.ControllerUtil;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StageController implements Serializable {
    private Class<?> starterClass;
    private Stage primaryStage;
    private static StageController instance;
    Logger logger = Logger.getLogger(StageController.class.getName());

    public static StageController getInstance(){
        if(instance == null)
            instance = new StageController();
        return instance;
    }

    private StageController() {
    }

    public void setStarterClass(Class<?> starterClass) {
        this.starterClass = starterClass;
    }

    public void startStage(Stage stage){
        primaryStage = stage;
        FXMLLoader fxmlLoader = ControllerUtil.getInstance().createLoader(starterClass);
        try{
            Parent node = fxmlLoader.load();
            if(node != null)
                stage.setScene(new Scene(node));
        }
        catch (Exception ex){
            logger.log(Level.SEVERE, ex.toString());
        }
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public void addStage(Class<?> controller){
        FXMLLoader fxmlLoader = ControllerUtil.getInstance().createLoader(controller);
        Platform.runLater(() -> {
            Stage stage = new Stage();
            Scene scene = null;
            try{
                scene = new Scene(fxmlLoader.load());
            }
            catch (Exception ex){
                logger.log(Level.SEVERE, ex.toString());
            }
            stage.setScene(scene);
            stage.initModality(Modality.NONE);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initOwner(primaryStage);
            stage.setAlwaysOnTop(true);
            stage.show();
        });

    }


}
