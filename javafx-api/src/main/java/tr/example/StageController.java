package tr.example;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tr.example.util.ControllerUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StageController implements Serializable {
    private Class<?> starterClass;
    private Stage primaryStage;
    private static StageController instance;
    private Map<Class<?>, Stage> stageHistory = new HashMap<>();
    private Map<Class<?>, View> viewsMap = new HashMap<>();
    Logger logger = Logger.getLogger(StageController.class.getName());

    public static StageController getInstance(){
        if(instance == null)
            instance = new StageController();
        return instance;
    }

    private StageController() {
    }

    public void setStarterClass(Class<?> starterClass) {
        try{
            this.starterClass = starterClass;
            viewsMap.put(starterClass, ControllerUtil.getInstance().getControllerProperties(starterClass));
        }
        catch (Exception ex){
            logger.log(Level.SEVERE, ex.toString());
        }
    }

    public void startStage(Stage stage){
        primaryStage = stage;
        stageHistory.put(starterClass, stage);
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

    public void addStage(Class<?> controller) throws InstantiationException, IllegalAccessException {
        View viewProperties = ControllerUtil.getInstance().getControllerProperties(controller);
        if(viewProperties != null){
            viewsMap.put(controller, viewProperties);
            Parent parent = stageHistory.get(starterClass).getScene().getRoot();
            if(parent instanceof Pane)
                ((Pane) parent).getChildren().add(createImageView(controller));
        }
    }

    public void openStage(Class<?> controller){
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
            stageHistory.put(controller, stage);
        });
    }

    public void stageClose(Class<?> controller){
        stageHistory.get(controller).close();
        stageHistory.remove(controller);
    }

    public void checkStateForStage(Class<?> controller){
        if(stageHistory.get(controller) != null)
            stageClose(controller);
        else
            openStage(controller);
    }

    public ImageView createImageView(Class<?> controller){
        View view = viewsMap.get(controller);
        ImageView imageView = new ImageView();
        imageView.setPickOnBounds(true);
        imageView.setImage(new Image(String.valueOf(controller.getClass().getResource(view.image()))));
        imageView.setOnMouseClicked(e -> checkStateForStage(controller));
        return imageView;
    }



}
