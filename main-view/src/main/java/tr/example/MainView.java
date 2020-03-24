package tr.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.integration.annotation.ServiceActivator;

@SpringBootApplication
public class MainView extends Application {
    Logger logger = LogManager.getLogger(MainView.class);
    @Override
    public void init() throws Exception {
                new SpringApplicationBuilder().
                        sources(MainView.class)
                        .run("");
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        StageController.getInstance().setStarterClass(MainViewController.class);
        StageController.getInstance().startStage(primaryStage);
    }


    @ServiceActivator(inputChannel = "mainViewChannel")
    public void getController(Class<?> controller){
        try{
            StageController.getInstance().addStage(controller);
        }
        catch (Exception ex){
            logger.error(ex.toString());
        }
    }

}
